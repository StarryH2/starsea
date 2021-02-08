package com.xinghai.service;

import com.xinghai.base.result.Results;
import com.xinghai.dto.UserDto;
import com.xinghai.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
public interface UserService extends IService<User> {

    Results findUsersByPage(Integer page, Integer limit);

    Results saveUser(User user, Long roleId);

    int findUserByPhone(String telephone);

    User findUserById(Long id);

    Results editUser(User user, Long roleId);

    Results delUser(Long id);


    Results findUserByUserName(String username, Integer page, Integer limit);


    User getUserByUserName(String username);

    Results editUserPassword(String username, String oldPassword, String newPassword);
}
