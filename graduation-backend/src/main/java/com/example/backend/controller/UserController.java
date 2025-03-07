package com.example.backend.controller;

import com.example.backend.domain.dto.user.LoginDTO;
import com.example.backend.domain.dto.user.RegisterDTO;
import com.example.backend.domain.dto.user.UserDTO;
import com.example.backend.domain.vo.PageVO;
import com.example.backend.domain.vo.user.ListUserVO;
import com.example.backend.domain.vo.user.UserInfoVO;
import com.example.backend.domain.vo.user.RegisterVO;
import com.example.backend.service.SysUserService;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController extends BaseController {
    @Autowired
    private SysUserService userService;

    @PostMapping("/register")
    public ResponseResult<RegisterVO> register(@RequestBody RegisterDTO register) {
        return ok(userService.register(register));
    }

    @PostMapping("/login")
    public ResponseResult<UserInfoVO> login(@RequestBody LoginDTO login) {
        return ok(userService.login(login));
    }

    @DeleteMapping("/logout")
    public ResponseResult<Void> logout() {
        userService.logout();
        return ok();
    }

    @PutMapping("/updateUserInfo")
    public ResponseResult<UserInfoVO> updateUserInfo(@RequestBody UserDTO user) {
        return ok(userService.updateUserInfo(user));
    }

    @PreAuthorize("@MA.isAdmin()")
    @GetMapping("/user")
    public ResponseResult<PageVO<ListUserVO>> getUser(String username, String name, String phone, Long roleId) {
        return ok(userService.getUser(username, name, phone, roleId));
    }

    @PreAuthorize("@MA.isAdmin()")
    @DeleteMapping("/user/{id}")
    public ResponseResult<PageVO<ListUserVO>> deleteUser(@PathVariable Long id) {
        return ok(userService.deleteUser(id));
    }

    @PutMapping("/user/{id}/{status}")
    public ResponseResult<Void> updateUserState(@PathVariable Long id, @PathVariable Integer status) {
        userService.updateUserState(id, status);
        return ok();
    }

}
