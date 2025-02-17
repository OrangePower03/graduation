package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.VerifyRequestBody;
import com.example.backend.constants.RedisConstants;
import com.example.backend.domain.dto.user.LoginDTO;
import com.example.backend.domain.dto.user.RegisterDTO;
import com.example.backend.domain.entity.SysRole;
import com.example.backend.domain.entity.SysUser;
import com.example.backend.domain.vo.PageVO;
import com.example.backend.domain.vo.user.PersonVO;
import com.example.backend.domain.vo.user.UserInfoVO;
import com.example.backend.domain.vo.user.RegisterVO;
import com.example.backend.mapper.SysRoleMapper;
import com.example.backend.mapper.SysUserMapper;
import com.example.backend.security.LoginUser;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.bean.BeanCopyUtils;
import com.example.backend.utils.object.StringUtils;
import com.example.backend.utils.redis.StringRedisUtils;
import com.example.backend.utils.security.JwtUtils;
import com.example.backend.utils.security.SecurityUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.backend.constants.UserConstants.*;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> implements IService<SysUser> {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private StringRedisUtils redisCache;

    @VerifyRequestBody
    public @NonNull RegisterVO register(RegisterDTO register) {
        // 校验格式
        AssertUtils.isTrue(StringUtils.isMatch(register.getPhone(), PHONE_FORMAT), AppHttpCode.PHONE_FORMAT_ERROR);
        AssertUtils.isTrue(StringUtils.isMatch(register.getUsername(), USERNAME_FORMAT), AppHttpCode.USERNAME_FORMAT_ERROR);
        AssertUtils.isTrue(StringUtils.isMatch(register.getPassword(), PASSWORD_FORMAT), AppHttpCode.PASSWORD_FORMAT_ERROR);
        AssertUtils.isTrue(StringUtils.isMatch(register.getIdNumber(), ID_NUMBER_FORMAT), AppHttpCode.ID_NUMBER_FORMAT_ERROR);
        AssertUtils.isEquals(register.getPassword(), register.getRPassword(), AppHttpCode.PASSWORD_NOT_EQUALS_ERROR);
        // todo 校验名字和身份证 ?

        // 校验用户名、手机、身份证号是否已存在
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getUsername, register.getUsername());
        AssertUtils.isTrue(count(userWrapper) == 0, AppHttpCode.USERNAME_EXISTS);
        userWrapper.clear();
        userWrapper.eq(SysUser::getPhone, register.getPhone());
        AssertUtils.isTrue(count(userWrapper) == 0, AppHttpCode.PHONE_EXISTS);
        userWrapper.clear();
        userWrapper.eq(SysUser::getIdNumber, register.getIdNumber());
        AssertUtils.isTrue(count(userWrapper) == 0, AppHttpCode.ID_NUMBER_EXISTS);

        // 保存用户
        int bornYear = Integer.parseInt(register.getIdNumber().substring(6, 14));
        int nowYear = new GregorianCalendar().get(Calendar.YEAR);
        int age = nowYear - bornYear;
        SysUser user = BeanCopyUtils.copyBean(register, SysUser.class);
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setAge(age);
        user.setRoleId(age >= ELDER_AGE_BOUNDARY ? ELDER_ROLE_ID : YOUNGSTER_ROLE_ID);
        save(user);
        return BeanCopyUtils.copyBean(user, RegisterVO.class);
    }

    @VerifyRequestBody
    public @NonNull UserInfoVO login(LoginDTO login) {
        UsernamePasswordAuthenticationToken authentication = new
                UsernamePasswordAuthenticationToken(login.getIdentification(), login.getPassword());
        Authentication auth = manager.authenticate(authentication);
        AssertUtils.nonNull(auth, AppHttpCode.UNAUTHORIZED_ERROR);
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        String userId = String.valueOf(loginUser.getUser().getId());

        Map<String, Object> payload = new HashMap<>();
        String token = JwtUtils.createJWT(userId, payload);
        redisCache.setWithExpire(RedisConstants.REDIS_TOKEN_KEY + userId,
                                    loginUser,
                                    RedisConstants.REDIS_TOKEN_EXPIRE);
        UserInfoVO userInfo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);
        userInfo.setToken(token);
        userInfo.setRoleName(loginUser.getRole().getName());
        return userInfo;
    }

    public void logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.remove(RedisConstants.REDIS_TOKEN_KEY + userId);
    }

    public @NonNull UserInfoVO addPersonRelation(@NonNull Long relationUserId) {
        SysUser relationUser = this.getById(relationUserId);
        AssertUtils.nonNull(relationUser, AppHttpCode.USER_NOT_FOUND_ERROR);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysRole userRole = loginUser.getRole();
        SysUser user = loginUser.getUser();
        if (YOUNGSTER_ROLE_ID.equals(userRole.getId())) {
            // 如果是年轻人，则只能添加老人
            AssertUtils.isTrue(ELDER_ROLE_ID.equals(relationUser.getRoleId()), AppHttpCode.RELATION_USER_ROLE_ERROR);
            int addCount = this.baseMapper.addRelation(user.getId(), relationUserId);
            AssertUtils.isTrue(addCount > 0, AppHttpCode.RELATION_EXISTS_ERROR);
        } else if (ELDER_ROLE_ID.equals(userRole.getId())) {
            // 如果是老人，则只能添加年轻人
            AssertUtils.isTrue(YOUNGSTER_ROLE_ID.equals(relationUser.getRoleId()), AppHttpCode.RELATION_USER_ROLE_ERROR);
            int addCount = this.baseMapper.addRelation(relationUserId, user.getId());
            AssertUtils.isTrue(addCount > 0, AppHttpCode.RELATION_NOT_FOUND_ERROR);
        } else {
            throw new IllegalArgumentException("用户的角色错误");
        }
        return BeanCopyUtils.copyBean(relationUser, UserInfoVO.class);
    }

    public @NonNull UserInfoVO removePersonRelation(@NonNull Long relationUserId) {
        SysUser relationUser = this.getById(relationUserId);
        AssertUtils.nonNull(relationUser, AppHttpCode.USER_NOT_FOUND_ERROR);
        Long userId = SecurityUtils.getUserId();
        Long roleId = SecurityUtils.getUserRole().getId();
        if (YOUNGSTER_ROLE_ID.equals(roleId)) {
            this.baseMapper.removeRelation(userId, relationUserId);
        } else {
            this.baseMapper.removeRelation(relationUserId, userId);
        }
        return BeanCopyUtils.copyBean(relationUser, UserInfoVO.class);
    }

    public @NonNull List<PersonVO> listRelationPerson(@NonNull Integer status) {
        Long userId = SecurityUtils.getUserId();
        Long roleId = SecurityUtils.getUserRole().getId();
        List<Long> idList;
        if (YOUNGSTER_ROLE_ID.equals(roleId)) {
            idList = this.baseMapper.getElderIdByYoungsterId(userId, status);
        } else {
            idList = this.baseMapper.getYoungsterIdByElderId(userId, status);
        }
        List<SysUser> relationUsers = this.listByIds(idList);
        return BeanCopyUtils.copyBeans(relationUsers, PersonVO.class);
    }

    public @NonNull PersonVO getExactPerson(String idNumber, String name) {
        AssertUtils.isTrue(StringUtils.isMatch(idNumber, ID_NUMBER_FORMAT), AppHttpCode.ID_NUMBER_FORMAT_ERROR);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getIdNumber, idNumber).eq(SysUser::getName, name);
        SysUser user = this.getOne(wrapper);
        AssertUtils.nonNull(user, AppHttpCode.USER_NOT_FOUND_ERROR);
        AssertUtils.isTrue(ELDER_ROLE_ID.equals(user.getRoleId()), AppHttpCode.USER_NOT_FOUND_ERROR);
        return BeanCopyUtils.copyBean(user, PersonVO.class);
    }
}

