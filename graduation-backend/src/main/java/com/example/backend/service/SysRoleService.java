package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.domain.dto.user.RoleDTO;
import com.example.backend.domain.entity.SysRole;
import com.example.backend.domain.vo.user.RoleVO;
import com.example.backend.mapper.SysRoleMapper;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.bean.BeanCopyUtils;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> implements IService<SysRole> {
    public void addRole(RoleDTO role) {
        SysRole sysRole = BeanCopyUtils.copyBean(role, SysRole.class);
        save(sysRole);
    }

    public @NonNull List<RoleVO> getRole(String name) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ObjectUtils.nonNull(name), SysRole::getName, name);
        return BeanCopyUtils.copyBeans(list(wrapper), RoleVO.class);
    }

    public void deleteRole(Long id) {
        removeById(id);
    }

    public void updateRole(Long id, RoleDTO role) {
        SysRole sysRole = BeanCopyUtils.copyBean(role, SysRole.class);
        sysRole.setId(id);
        AssertUtils.isTrue(updateById(sysRole), AppHttpCode.ROLE_NOT_FOUND_ERROR);
    }
}

