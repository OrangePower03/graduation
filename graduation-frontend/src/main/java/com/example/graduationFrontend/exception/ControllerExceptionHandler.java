package com.example.graduationFrontend.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.example.graduationFrontend")
public class ControllerExceptionHandler {
    @ExceptionHandler(ErrorException.class)
    public String handleException(ErrorException e, Model model) {
        if (e.getMessage().equals("用户未登录")) {
            return "redirect:/";
        }
        model.addAttribute("errorMessage", e.getMessage());
        return "/error";
    }
}
