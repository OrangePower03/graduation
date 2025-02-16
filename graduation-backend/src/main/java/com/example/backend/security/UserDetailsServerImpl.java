package com.example.backend.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.constants.RedisConstants;
import com.example.backend.domain.entity.SysRole;
import com.example.backend.domain.entity.SysUser;
import com.example.backend.exception.GlobalException;
import com.example.backend.mapper.SysRoleMapper;
import com.example.backend.mapper.SysUserMapper;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.mp.SqlUtils;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.redis.StringRedisUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServerImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private StringRedisUtils redisCache;

    @Override
    public UserDetails loadUserByUsername(String uniqueIdentifier) throws UsernameNotFoundException {
        AssertUtils.nonNull(uniqueIdentifier, AppHttpCode.USERNAME_OR_PASSWORD_ERROR);
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getUsername, uniqueIdentifier)
                .or()
                .eq(SysUser::getIdNumber, uniqueIdentifier)
                .or()
                .eq(SysUser::getPhone, uniqueIdentifier);
        SysUser user = sysUserMapper.selectOne(userWrapper);
        if (ObjectUtils.isNull(user)) {
            log.error("用户不存在");
            throw new GlobalException(AppHttpCode.USERNAME_OR_PASSWORD_ERROR);
        }
        SysRole role = sysRoleMapper.selectById(user.getRoleId());
        AssertUtils.nonNull(role, AppHttpCode.UNAUTHORIZED_ERROR);
        return new LoginUser(user, role);
    }
}
