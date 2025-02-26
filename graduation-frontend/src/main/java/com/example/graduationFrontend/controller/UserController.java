package com.example.graduationFrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController extends BaseController {
    @GetMapping("/")
    public String showLoginPage(Model model) {
        return "login";  // 返回 Thymeleaf 模板 login.html
    }

    // 处理登录表单提交
    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password, Model model) {
        // 简单的用户名和密码验证（示例中只是硬编码）
        if ("admin".equals(username) && "password".equals(password)) {
            model.addAttribute("message", "登录成功！");
            return "home";  // 登录成功后跳转到 home 页面
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";  // 如果登录失败，返回登录页面
        }
    }
}
