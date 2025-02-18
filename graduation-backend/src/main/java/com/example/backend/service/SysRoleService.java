package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.domain.entity.SysRole;
import com.example.backend.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> implements IService<SysRole> {

}

