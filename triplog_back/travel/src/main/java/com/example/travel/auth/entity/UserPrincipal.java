package com.example.travel.auth.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {

    private final Long uid;

    private final String uniqueId;

    private final String pw;

    private final String email;

    private final String nickname;

    private final RoleType roleType;

    private final ProviderType providerType;

    private final Collection<GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    public UserPrincipal(Long uid, String uniqueId, String pw, String email, String nickname, RoleType roleType,
                         ProviderType providerType, Collection<GrantedAuthority> authorities) {
        this.uid = uid;
        this.uniqueId = uniqueId;
        this.pw = pw;
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = providerType;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRoleType().getCode()));

        return new UserPrincipal(
                user.getUid(), user.getUniqueId(), user.getPw(), user.getEmail(), user.getNickname(),
                user.getRoleType(), user.getProviderType(), authorities
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() { return nickname; }

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
        return true;
    }

    @Override
    public String getName() { return uniqueId; }
}