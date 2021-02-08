package com.xinghai.controller;


import com.xinghai.base.result.PageTableRequest;
import com.xinghai.base.result.ResponseCode;
import com.xinghai.base.result.Results;
import com.xinghai.dto.UserDto;
import com.xinghai.entity.RoleUser;
import com.xinghai.entity.User;
import com.xinghai.service.RoleUserService;
import com.xinghai.service.UserService;
import com.xinghai.utils.MD5;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "分页获取用户信息",notes = "分页获取用户信息")
    @ApiImplicitParam(name = "request",value = "分页查询实体类",required = false)
    @RequestMapping("/findUserList")
    @ResponseBody
    public Results<User> findUserList(PageTableRequest pageTableRequest){
        pageTableRequest.countOffset();
        return userService.findUsersByPage(pageTableRequest.getOffset(),pageTableRequest.getLimit());
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('sys:user:add')")
    public String add(Model model){
        model.addAttribute(new User());
        return "user/user-add";
    }


    @PreAuthorize("hasAuthority('sys:user:add')")
    @RequestMapping("/addUser")
    @ResponseBody
    public Results<User> saveUser(UserDto userDto,Long roleId){
        if (null == roleId){
            return Results.success(ResponseCode.PARAMETER_MISSING);
        }

        int count = userService.findUserByPhone(userDto.getTelephone());
        if (count > 0){
            return Results.success(ResponseCode.PHONE_REPEAT);
        }

        userDto.setStatus(1);
//        userDto.setPassword(MD5.crypt(userDto.getPassword()));
        //加密密码
        userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        return userService.saveUser(userDto, roleId);
    }

    @PreAuthorize("hasAuthority('sys:user:edit')")
    @RequestMapping("/edit")
    public String edit(Model model,Long id){
        model.addAttribute(userService.findUserById(id));
        return "user/user-edit";
    }

    @PreAuthorize("hasAuthority('sys:user:edit')")
    @RequestMapping("/editUser")
    @ResponseBody
    public Results editUser(UserDto userDto,Long roleId){
        if (null == roleId){
            return Results.success(ResponseCode.PARAMETER_MISSING);
        }

        return userService.editUser(userDto, roleId);
    }

    @PreAuthorize("hasAuthority('sys:user:del')")
    @RequestMapping("/delUser")
    @ResponseBody
    public Results delUser(Long id){
        if (null == id){
            return Results.success(ResponseCode.PARAMETER_MISSING);
        }

        return userService.delUser(id);
    }


    @RequestMapping("/findUserByUserName")
    @ResponseBody
    public Results findUserByUserName(PageTableRequest tableRequest,String username){
        if (null == username){
            return Results.success(ResponseCode.PARAMETER_MISSING);
        }

        tableRequest.countOffset();

        return userService.findUserByUserName(username,tableRequest.getOffset(),tableRequest.getLimit());
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/editUserPassword")
    @ResponseBody
    public Results editUserPassword(String username,String oldPassword,String newPassword){
        return userService.editUserPassword(username,oldPassword,newPassword);
    }



}
