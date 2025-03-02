package com.example.graduationFrontend.utils;

import com.example.graduationFrontend.domain.vo.user.UserInfoVO;
import com.example.graduationFrontend.exception.ErrorException;

import javax.servlet.http.HttpSession;

public class DataUtils {
    public static void saveUserInfo(HttpSession session, UserInfoVO userInfo) {
        session.setAttribute("userInfo", userInfo);
    }

    public static UserInfoVO getUserInfo(HttpSession session) {
        UserInfoVO userInfo = (UserInfoVO) session.getAttribute("userInfo");
        if (userInfo == null) {
            throw new ErrorException("用户未登录");
        }
        return userInfo;
    }

    public static String getUserToken(HttpSession session) {
        UserInfoVO userInfo = getUserInfo(session);
        return userInfo.getToken();
    }

    public static void removeUserInfo(HttpSession session) {
        session.removeAttribute("userInfo");
    }

}
