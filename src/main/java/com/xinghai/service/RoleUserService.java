package com.xinghai.service;

import com.xinghai.base.result.Results;
import com.xinghai.entity.RoleUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-06
 */
public interface RoleUserService extends IService<RoleUser> {

    Results findRoleUserByUserId(Long userId);
}
