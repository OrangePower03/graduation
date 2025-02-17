package com.example.backend.controller;

import com.example.backend.domain.dto.user.RegisterDTO;
import com.example.backend.domain.vo.user.RegisterVO;
import com.example.backend.service.SysRoleService;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Autowired
    private SysRoleService roleService;

}
