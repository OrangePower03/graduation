package com.example.backend.utils.security;

import com.example.backend.domain.entity.SysRole;
import com.example.backend.domain.entity.SysUser;
import com.example.backend.security.LoginUser;
import com.example.backend.constants.PermissionConstants;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.web.AppHttpCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AssertUtils.nonNull(authentication, AppHttpCode.UNAUTHORIZED_ERROR);
        return authentication;
    }

    public static void setAuthentication(LoginUser user, String token) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static SysUser getUser() {
        return getLoginUser().getUser();
    }

    public static Long getUserId() {
        return getUser().getId();
    }

    /**
     * 判别是否为超级管理员
     */
    public static boolean isAdmin() {
        return PermissionConstants.ADMIN_KEY.equals(getUserPermission());
    }

    public static SysRole getUserRole() {
        return getLoginUser().getRole();
    }

    public static String getUserPermission() {
        return getUserRole().getPermissionKey();
    }
}
