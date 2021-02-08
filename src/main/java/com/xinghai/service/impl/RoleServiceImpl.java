package com.xinghai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.xinghai.base.result.ResponseCode;
import com.xinghai.base.result.Results;
import com.xinghai.dto.RoleDto;
import com.xinghai.entity.Permission;
import com.xinghai.entity.Role;
import com.xinghai.mapper.PermissionMapper;
import com.xinghai.mapper.RoleMapper;
import com.xinghai.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Results<Role> findRoleAll() {
        List<Role> roles = roleMapper.selectList(null);
        return Results.success(200,roles);
    }

    @Override
    public Results<Role> findRoleByPage(Integer page, Integer limit) {
        return Results.success(roleMapper.countAllRole().intValue(),roleMapper.findRolesByPage(page,limit));
    }

    @Override
    public Results<Role> findRoleByRoleNamePage(String roleName, Integer page, Integer limit) {
        return Results.success(roleMapper.countByRoleNameRole(roleName).intValue(),roleMapper.findRolesByRoleNamePage(roleName,page,limit));
    }

    @Transactional
    @Override
    public Results addRoleAndPermission(RoleDto roleDto) {
        //1、先保存角色
        roleMapper.insert(roleDto);
        List<Long> perIds = roleDto.getPermissionIds();
        //移除0，permission 从1开始
        perIds.remove(0L);

        //2、保存角色对应的权限
        if (!CollectionUtils.isEmpty(perIds)){
            roleMapper.saveRolePermission(roleDto.getId(),perIds);
            return Results.success();
        }

        return Results.failure();
    }

    @Override
    public Role findRoleAndPermissionById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public Results findPermissionByRoleId(Long id) {
        List<Permission> permissions = roleMapper.findPermissionByRoleId(id);
        return Results.success(200,permissions);
    }

    @Override
    public Results editRoleAndPermission(RoleDto roleDto) {
        List<Long> perIds = roleDto.getPermissionIds();
        perIds.remove(0L);

        //1、更新角色权限之前要删除改角色之前的所有权限
        roleMapper.delRolePermission(roleDto.getId());

        //2、保存角色对应的权限
        if (!CollectionUtils.isEmpty(perIds)){
            //保存角色权限信息
            int rolePerSaveCount = roleMapper.saveRolePermission(roleDto.getId(), perIds);
            if (rolePerSaveCount>0){
                //修改角色表
                roleDto.setUpdateTime(LocalDateTime.now());
                roleMapper.editRole(roleDto);
                return Results.success();
            }
        }

        return Results.failure();
    }


    @Override
    public Results delRoleAndRolePermission(Long id) {
        //1、查看是否有用户关联角色表
        Integer roleUserCount =  roleMapper.findRoleUserCount(id);
        //2、没有用户关联角色表才能删除
        if (roleUserCount <= 0){
            int delRolePermissionCount = roleMapper.delRolePermission(id);
            if (delRolePermissionCount > 0){
                int delRoleCount = roleMapper.deleteById(id);
                if (delRoleCount > 0){
                    return Results.success();
                }
            }

        }

        return Results.failure(ResponseCode.USER_ROLE_NO_CLEAR.getCode(),ResponseCode.USER_ROLE_NO_CLEAR.getMessage());
    }
}
