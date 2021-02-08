package com.xinghai.service.impl;

import com.xinghai.dto.LoginUser;
import com.xinghai.entity.Permission;
import com.xinghai.entity.User;
import com.xinghai.mapper.PermissionMapper;
import com.xinghai.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUserName(username);
        if (null == user){
            throw new AuthenticationCredentialsNotFoundException("用户不存在...");
        }else if (user.getStatus() == User.Status.LOCKED) {
            throw new LockedException("用户被锁定,请联系管理员");
        } else if (user.getStatus() == User.Status.DISABLED) {
            throw new DisabledException("用户已作废");
        }

        LoginUser loginUser = new LoginUser();
        //将user复制到loginUser
        BeanUtils.copyProperties(user,loginUser);

        List<Permission> permissions = permissionMapper.findMenuByUserId(user.getId());

        loginUser.setPermissions(permissions);

        return loginUser;
    }
}
