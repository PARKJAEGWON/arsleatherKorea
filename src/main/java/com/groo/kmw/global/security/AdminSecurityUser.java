package com.groo.kmw.global.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//시큐리티 User를 가공함
public class AdminSecurityUser extends User{

    @Getter
    private long adminId;

    public AdminSecurityUser(long adminId, String adminLoginId, String password, Collection<? extends GrantedAuthority> authorities) {
        super(adminLoginId, password, authorities);
        this.adminId = adminId;
    }

    public Authentication genAuthentication() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                this,
                this.getPassword(),
                this.getAuthorities()
        );
        return auth;
    }
}