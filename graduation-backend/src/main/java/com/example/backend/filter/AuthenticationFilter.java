package com.example.backend.filter;

import com.example.backend.constants.HttpConstants;
import com.example.backend.constants.HttpStatus;
import com.example.backend.constants.RedisConstants;
import com.example.backend.security.LoginUser;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.redis.StringRedisUtils;
import com.example.backend.utils.security.JwtUtils;
import com.example.backend.utils.security.SecurityUtils;
import com.example.backend.utils.web.AppHttpCode;
import com.example.backend.utils.web.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private StringRedisUtils redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpConstants.HEADER_AUTHENTICATION);
        if (ObjectUtils.nonNull(token)) {
            Claims claims;
            try {
                claims = JwtUtils.parseJWT(token);
            } catch (Exception e) {
                WebUtils.render(request, response, HttpStatus.FORBIDDEN, "解析令牌错误");
                return;
            }
            String id = claims.getSubject();
            LoginUser loginUser = redisCache.get(RedisConstants.REDIS_TOKEN_KEY + id);
            if (ObjectUtils.isNull(loginUser)) {
                WebUtils.render(request, response, AppHttpCode.UNAUTHORIZED_ERROR);
                return;
            }
            SecurityUtils.setAuthentication(loginUser, token);
        }
        filterChain.doFilter(request, response);
    }
}
