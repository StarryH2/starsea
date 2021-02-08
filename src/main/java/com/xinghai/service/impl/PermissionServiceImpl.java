package com.xinghai.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.xinghai.base.result.Results;
import com.xinghai.entity.Permission;
import com.xinghai.mapper.PermissionMapper;
import com.xinghai.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinghai.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {


    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public Results findPermissionAll() {
        List<Permission> datas = permissionMapper.findPermissionAll();
        JSONArray array = new JSONArray();
        TreeUtils.setPermissionsTree(0L, datas, array);
        return Results.success(array);
    }

    @Override
    public Results findMenuAll() {
        List<Permission> menuAll = permissionMapper.findMenuAll();
        return Results.success(0, menuAll);
    }

    @Override
    public Results addPermission(Permission permission) {
        int addCount = permissionMapper.addPermission(permission);

        return addCount > 0 ?Results.success() : Results.failure();
    }


    @Override
    public Permission findPermissionById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public Results editPermission(Permission permission) {
        int addCount = permissionMapper.editPermission(permission);

        return addCount > 0 ?Results.success() : Results.failure();
    }

    @Override
    public Results delPermission(Long id) {
        //1、先查询role角色表是否绑定了菜单表
        int rolePerCount = permissionMapper.findRolePermissionByPerId(id);
        if (rolePerCount > 0){
            return Results.failure(5,"删除失败");
        }
        int delPerCount = permissionMapper.deleteById(id);
        if (delPerCount > 0){
           return Results.success(1,"删除成功");
        }

        return Results.failure(5,"删除失败");
    }

    @Override
    public Results findMenuByUserId(Long userId) {
        List<Permission> permissionList = permissionMapper.findMenuByUserId(userId);
        permissionList = permissionList.stream().filter(p -> p.getType().equals(1)).collect(Collectors.toList());
        JSONArray array = new JSONArray();
        TreeUtils.setPermissionsTree(0L,permissionList,array);
        return Results.success(array);
    }
}
