package com.xinghai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xinghai.base.result.Results;
import com.xinghai.entity.RoleUser;
import com.xinghai.mapper.RoleUserMapper;
import com.xinghai.service.RoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-06
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {

    @Autowired
    private RoleUserMapper roleUserMapper;

    @Override
    public Results findRoleUserByUserId(Long userId) {
        QueryWrapper<RoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<RoleUser> roleUsers = roleUserMapper.selectList(wrapper);
        RoleUser roleUser = roleUsers.get(0);
        return Results.success(roleUser);
    }
}
