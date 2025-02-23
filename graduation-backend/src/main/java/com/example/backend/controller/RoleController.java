package com.example.backend.controller;

import com.example.backend.domain.dto.user.RoleDTO;
import com.example.backend.domain.vo.user.RoleVO;
import com.example.backend.service.SysRoleService;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Autowired
    private SysRoleService roleService;

    @PreAuthorize("@MA.isAdmin()")
    @PostMapping
    public ResponseResult<Void> addRole(@RequestBody RoleDTO role) {
        roleService.addRole(role);
        return ok();
    }

    @PreAuthorize("@MA.isAdmin()")
    @GetMapping
    public ResponseResult<List<RoleVO>> getRole(String name) {
        return ok(roleService.getRole(name));
    }

    @PreAuthorize("@MA.isAdmin()")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ok();
    }

    @PutMapping("/{id}")
    public ResponseResult<Void> updateRole(@PathVariable Long id, @RequestBody RoleDTO role) {
        roleService.updateRole(id, role);
        return ok();
    }

}
