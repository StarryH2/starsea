package com.xinghai.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xinghai.entity.Permission;
import com.xinghai.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LoginUser extends User implements UserDetails {

    private List<Permission> permissions;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回授权
        return permissions.parallelStream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
                .map(p -> new  SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        //判断是否过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //判断账号是否被锁定
        return getStatus() != Status.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //判断是否过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        //判断是否激活
        return true;
    }
}
