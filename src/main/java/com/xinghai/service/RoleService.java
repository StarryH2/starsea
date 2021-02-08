package com.xinghai.service;

import com.xinghai.base.result.Results;
import com.xinghai.dto.RoleDto;
import com.xinghai.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
public interface RoleService extends IService<Role> {

    Results<Role> findRoleAll();

    Results<Role> findRoleByPage(Integer page, Integer limit);

    Results<Role> findRoleByRoleNamePage(String roleName, Integer page, Integer limit);

    Results addRoleAndPermission(RoleDto roleDto);

    Role findRoleAndPermissionById(Long id);

    Results findPermissionByRoleId(Long id);

    Results editRoleAndPermission(RoleDto roleDto);

    Results delRoleAndRolePermission(Long id);
}
