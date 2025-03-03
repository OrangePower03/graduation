package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpConstants;
import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.constants.PermissionConstants;
import com.example.graduationFrontend.domain.dto.user.LoginDTO;
import com.example.graduationFrontend.domain.dto.user.RegisterDTO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import com.example.graduationFrontend.domain.vo.user.RegisterVO;
import com.example.graduationFrontend.domain.vo.user.UserInfoVO;
import com.example.graduationFrontend.exception.ErrorException;
import com.example.graduationFrontend.utils.DataUtils;
import com.example.graduationFrontend.utils.JsonUtils;
import okhttp3.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserController extends BaseController {
    @GetMapping("/")
    public String showHomePage(HttpSession session, Model model) {
        try {
            UserInfoVO userInfo = DataUtils.getUserInfo(session);
            switch (userInfo.getRoleName()) {
                case PermissionConstants.ADMIN_KEY: return "/home/adminHome";
                case PermissionConstants.ELDER_KEY: return "/home/elderHome";
                case PermissionConstants.YOUNGSTER_KEY: return "/home/youngsterHome";
                default: return "/home/adminHome";
            }

        } catch (ErrorException e) {
            model.addAttribute("loginDTO", new LoginDTO());
            return "login";
        }

    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/logout", token, null, HttpMethod.DELETE, null);
        ResponseResult<Void> result = sendRequest(request);
        if (result.isSuccess()) {
            DataUtils.removeUserInfo(session);
        }
        return "redirect:/login";
    }

    /*---------------处理http--------------*/

    @GetMapping("/token")
    @ResponseBody
    public Map<String, String> getToken(HttpSession session) {
        return Map.of("token", DataUtils.getUserToken(session));
    }

    @PostMapping("/login")
    public String handleLogin(LoginDTO loginDTO, HttpServletResponse response, HttpSession session, Model model) {

        Request request = buildRequest("/login", Map.of(), null, HttpMethod.POST, loginDTO);
        ResponseResult<UserInfoVO> result = sendRequest(request, UserInfoVO.class);
        if (result.isSuccess()) {
            DataUtils.saveUserInfo(session, result.getData());
            return "/home/adminHome";
        } else {
            model.addAttribute("error", result);
            model.addAttribute("loginDTO", new LoginDTO());
            return "login";
        }
    }

    @PostMapping("/register")
    public String handleRegister(RegisterDTO registerDTO, Model model) {
        Request request = buildRequest("/register", Map.of(), null, HttpMethod.POST, registerDTO);
        ResponseResult<RegisterVO> result = sendRequest(request, RegisterVO.class);
        if (result.isSuccess()) {
            return "login";
        } else {
            model.addAttribute("error", result);
            model.addAttribute("registerDTO", new RegisterDTO());
            return "register";
        }
    }
}
