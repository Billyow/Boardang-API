package com.billyow.app.boardang.auth.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// this class will be used to implement the UserDetails interface composed with my model User


public class PrincipalUser implements UserDetails {
    @Getter
    private final String name;
    @Getter
    private final Long id;
    private final boolean isActive;
    private final String email;
    private final List<GrantedAuthority> authorities;

    public PrincipalUser(Long id, String name, boolean isActive, String email, List<GrantedAuthority> authorities) {
        this.name = name;
        this.id = id;
        this.isActive = isActive;
        this.email = email;
        this.authorities = Collections.unmodifiableList(authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // here i can return the authorities when there are any
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
