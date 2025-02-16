package com.example.backend.security;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.backend.constants.UserConstants;
import com.example.backend.domain.entity.SysRole;
import com.example.backend.domain.entity.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 5201314L;

    private SysUser user;

    private SysRole role;

    public LoginUser(SysUser user, SysRole role) {
        this.user = user;
        this.role = role;
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(this.role.getPermissionKey()));
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return UserConstants.USER_STATUS_NORMAL.equals(user.getStatus());
    }
}

