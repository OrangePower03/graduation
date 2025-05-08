package com.example.backend.security;

import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.security.SecurityUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import static com.example.backend.constants.PermissionConstants.*;

@Component("MA")
public class MethodAuthentication {
    private void doAssert(boolean ret) {
        AssertUtils.isTrue(ret, AppHttpCode.PERMISSION_DENIED_ERROR);
    }

    public boolean isRole(@NonNull String roleKey) {
        return roleKey.equals(SecurityUtils.getUserPermission());
    }

    public boolean isAdmin() {
        boolean ret = SecurityUtils.isAdmin();
        doAssert(ret);
        return ret;
    }

    public boolean isYoungster() {
        boolean ret = isRole(YOUNGSTER_KEY);
        doAssert(ret);
        return ret;
    }

    public boolean isElder() {
        boolean ret = isRole(ELDER_KEY);
        doAssert(ret);
        return ret;
    }

    public boolean isElderOrYoungster() {
        return isRole(ELDER_KEY) || isRole(YOUNGSTER_KEY);
    }

    public boolean isAdminOrYoungster() {
        return SecurityUtils.isAdmin() || isRole(YOUNGSTER_KEY);
    }
}
