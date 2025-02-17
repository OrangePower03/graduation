package com.example.backend.controller;

import com.example.backend.domain.dto.user.LoginDTO;
import com.example.backend.domain.dto.user.RegisterDTO;
import com.example.backend.domain.vo.user.UserInfoVO;
import com.example.backend.domain.vo.user.RegisterVO;
import com.example.backend.service.SysUserService;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
