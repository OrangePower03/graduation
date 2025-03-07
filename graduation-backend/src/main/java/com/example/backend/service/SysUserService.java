package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.VerifyRequestBody;
import com.example.backend.constants.RedisConstants;
import com.example.backend.domain.dto.user.LoginDTO;
import com.example.backend.domain.dto.user.RegisterDTO;
import com.example.backend.domain.dto.user.UserDTO;
import com.example.backend.domain.entity.SysRole;
import com.example.backend.domain.entity.SysUser;
import com.example.backend.domain.vo.PageVO;
import com.example.backend.domain.vo.user.*;
import com.example.backend.mapper.SysUserMapper;
import com.example.backend.security.LoginUser;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.PageUtils;
import com.example.backend.utils.bean.BeanCopyUtils;
import com.example.backend.utils.object.CollectionUtils;
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

    @Autowired
    private SysRoleService roleService;

    @VerifyRequestBody
    public @NonNull RegisterVO register(RegisterDTO register) {
        // 校验格式
        AssertUtils.isTrue(StringUtils.isMatch(register.getPhone(), PHONE_FORMAT), AppHttpCode.PHONE_FORMAT_ERROR);
        AssertUtils.isTrue(StringUtils.isMatch(register.getUsername(), USERNAME_FORMAT), AppHttpCode.USERNAME_FORMAT_ERROR);
        AssertUtils.isTrue(StringUtils.isMatch(register.getPassword(), PASSWORD_FORMAT), AppHttpCode.PASSWORD_FORMAT_ERROR);
        AssertUtils.isTrue(StringUtils.isMatch(register.getIdNumber(), ID_NUMBER_FORMAT), AppHttpCode.ID_NUMBER_FORMAT_ERROR);
        AssertUtils.isEquals(register.getPassword(), register.getRepeatPassword(), AppHttpCode.PASSWORD_NOT_EQUALS_ERROR);
        // todo 校验名字和身份证，需要企业用户才能调用接口

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
        int bornYear = Integer.parseInt(register.getIdNumber().substring(6, 10));
        int nowYear = new GregorianCalendar().get(Calendar.YEAR);
        int age = nowYear - bornYear;
        SysUser user = BeanCopyUtils.copyBean(register, SysUser.class);
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setAge(age);
        user.setRoleId(age >= ELDER_AGE_BOUNDARY ? ELDER_ROLE_ID : YOUNGSTER_ROLE_ID);
        user.setSex(register.getIdNumber().substring(16, 17).matches("^[13579]$") ? USER_SEX_MAN : USER_SEX_WOMAN);
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
            int addCount = this.baseMapper.affirmRelation(relationUserId, user.getId());
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
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
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

    public @NonNull PageVO<ListUserVO> getUser(String username, String name, String phone, Long roleId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.nonBlank(username), SysUser::getUsername, username);
        wrapper.like(StringUtils.nonBlank(name), SysUser::getName, name);
        wrapper.eq(StringUtils.nonBlank(phone), SysUser::getPhone, phone);
        wrapper.eq(StringUtils.nonNull(roleId) && roleId != -1, SysUser::getRoleId, roleId);
        Page<SysUser> page = this.page(PageUtils.getPage(), wrapper);
        PageVO<ListUserVO> res = new PageVO<>(page, ListUserVO.class);
        Map<Long, String> roleMap = CollectionUtils.toMap(roleService.getRole(null), RoleVO::getId, RoleVO::getName);
        for (ListUserVO user : res.getRows()) {
            user.setRoleName(roleMap.get(user.getRoleId()));
        }
        return res;
    }

    public PageVO<ListUserVO> deleteUser(Long id) {
        AssertUtils.isFalse(operatorMyself(id), AppHttpCode.REMOVE_MYSELF_ERROR);
        this.removeById(id);
        return getUser(null, null, null, null);
    }

    public void updateUserState(Long id, Integer status) {
        AssertUtils.isFalse(operatorMyself(id), AppHttpCode.REMOVE_MYSELF_ERROR);
        SysUser user = this.getById(id);
        AssertUtils.nonNull(user, AppHttpCode.USER_NOT_FOUND_ERROR);
        user.setStatus(status);
        this.updateById(user);
    }

    private boolean operatorMyself(Long userId) {
        return userId.equals(SecurityUtils.getUserId());
    }

    public @NonNull UserInfoVO updateUserInfo(UserDTO user) {
        AssertUtils.isTrue(StringUtils.isMatch(user.getPhone(), PHONE_FORMAT), AppHttpCode.PHONE_FORMAT_ERROR);
        SysUser sysUser = SecurityUtils.getUser();
        if (!sysUser.getPhone().equals(user.getPhone())) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getPhone, user.getPhone());
            long phoneCount = this.count(wrapper);
            AssertUtils.isTrue(phoneCount == 0, AppHttpCode.PHONE_EXISTS);
        }

        sysUser.setPhone(user.getPhone());
        sysUser.setUsername(user.getUsername());
        this.updateById(sysUser);
        return BeanCopyUtils.copyBean(sysUser, UserInfoVO.class);
    }
}
