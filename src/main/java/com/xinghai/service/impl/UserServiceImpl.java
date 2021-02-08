package com.xinghai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinghai.base.result.PageTableRequest;
import com.xinghai.base.result.ResponseCode;
import com.xinghai.base.result.Results;
import com.xinghai.dto.UserDto;
import com.xinghai.entity.RoleUser;
import com.xinghai.entity.User;
import com.xinghai.mapper.RoleUserMapper;
import com.xinghai.mapper.UserMapper;
import com.xinghai.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleUserMapper roleUserMapper;

    @Override
    public Results findUsersByPage(Integer page, Integer limit) {
        return Results.success(userMapper.countAllUser().intValue(),userMapper.findUsersByPage(page,limit));
    }

    /**
     * 新增用户
     * @param user
     * @param roleId
     * @return
     */
    @Transactional
    @Override
    public Results saveUser(User user, Long roleId) {
        int userStatus = userMapper.insert(user);
        if (userStatus>0){
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(user.getId());
            roleUser.setRoleId(roleId);
            int roleUserStatus = roleUserMapper.insertRoleUser(roleUser);
            if (roleUserStatus > 0){
                return Results.success();
            }else {
                return Results.failure();
            }

        }
        return Results.failure();
    }

    @Override
    public int findUserByPhone(String telephone) {
        return userMapper.getUserByPhone(telephone);
    }

    @Override
    public User findUserById(Long id) {
        return userMapper.selectById(id);
    }


    /**
     * 修改用户
     * @param user
     * @param roleId
     * @return
     */
    @Transactional
    @Override
    public Results editUser(User user, Long roleId) {
        int userStatus = userMapper.updateById(user);
        if (userStatus>0){
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(user.getId());
            roleUser.setRoleId(roleId);
            int roleUserStatus = roleUserMapper.updateRoleUser(roleUser);
            if (roleUserStatus > 0){
                return Results.success();
            }else {
                return Results.failure();
            }

        }
        return Results.failure();
    }

    @Override
    public Results delUser(Long id) {
        QueryWrapper<RoleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        int delRoleUserCount = roleUserMapper.delete(queryWrapper);
        if (delRoleUserCount>0){
            int delUserCount = userMapper.deleteById(id);
            if (delUserCount>0){
                return Results.success();
            }
        }

        return Results.failure();
    }

    @Override
    public Results findUserByUserName(String username, Integer page, Integer limit) {
        int count = userMapper.findUserByUserNameCount(username).intValue();
        List<User> userList = userMapper.findUserByUserName(username, page, limit);
        return Results.success(count,userList);
    }

    @Override
    public User getUserByUserName(String username) {
        return userMapper.getUserByUserName(username);
    }

    @Override
    public Results editUserPassword(String username, String oldPassword, String newPassword) {
        User user = userMapper.getUserByUserName(username);
        //查看密码是否存在
        if (null == user){
            return Results.failure(5,"用户不存在");
        }
        String encode = new BCryptPasswordEncoder().encode(oldPassword);
        String password = user.getPassword();
        System.out.println(encode+">>"+password);
        //将旧加密和旧密码比较
        if (! encode.equals(password)){
            return Results.failure(5,"旧密码错误");
        }

        int editPassWordCount = userMapper.editUserPassword(user.getId(),new BCryptPasswordEncoder().encode(newPassword));
        if (editPassWordCount > 0){
            return Results.success(1,"修改成功");
        }
        return Results.failure();
    }
}
