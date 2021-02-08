package com.xinghai.service;

import com.xinghai.base.result.Results;
import com.xinghai.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Service
public interface PermissionService extends IService<Permission> {

    Results findPermissionAll();

    Results findMenuAll();

    Results addPermission(Permission permission);

    Permission findPermissionById(Long id);

    Results editPermission(Permission permission);

    Results delPermission(Long id);

    Results findMenuByUserId(Long userId);
}
