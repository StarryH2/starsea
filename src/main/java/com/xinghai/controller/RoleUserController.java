package com.xinghai.controller;


import com.xinghai.base.result.Results;
import com.xinghai.entity.RoleUser;
import com.xinghai.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xinghai
 * @since 2021-02-06
 */
@Controller
@RequestMapping("/roleuser")
public class RoleUserController {

    @Autowired
    private RoleUserService roleUserService;

    @RequestMapping("/findRoleUserByUserId")
    @ResponseBody
    public Results findRoleUserByUserId(Long userId){
        return roleUserService.findRoleUserByUserId(userId);
    }
}
