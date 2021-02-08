package com.xinghai.controller;


import com.xinghai.base.result.PageTableRequest;
import com.xinghai.base.result.Results;
import com.xinghai.dto.RoleDto;
import com.xinghai.entity.Role;
import com.xinghai.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/findRoleAll")
    @ResponseBody
    public Results<Role> findRoleAll(){
        return roleService.findRoleAll();
    }

    @RequestMapping("/findRoleList")
    @ResponseBody
    public Results<Role> findRoleList(PageTableRequest pageTableRequest){
        pageTableRequest.countOffset();
        return roleService.findRoleByPage(pageTableRequest.getOffset(),pageTableRequest.getLimit());
    }


    @RequestMapping("/findRoleByRoleName")
    @ResponseBody
    public Results<Role> findUserByUserName(PageTableRequest pageTableRequest,String roleName){
        pageTableRequest.countOffset();
        return roleService.findRoleByRoleNamePage(roleName,pageTableRequest.getOffset(),pageTableRequest.getLimit());
    }

    @PreAuthorize("hasAnyAuthority('sys:role:add')")
    @RequestMapping("/add")
    public String add(Model model){
        model.addAttribute("role",new Role());
        return "/role/role-add";
    }

    @PreAuthorize("hasAnyAuthority('sys:role:add')")
    @RequestMapping("/addRoleAndPermission")
    @ResponseBody
    public Results addRoleAndPermission(@RequestBody RoleDto roleDto){
        return roleService.addRoleAndPermission(roleDto);
    }

    @PreAuthorize("hasAnyAuthority('sys:role:edit')")
    @RequestMapping("/edit")
    public String edit(Model model,Long id){
        model.addAttribute(roleService.findRoleAndPermissionById(id));
        return "role/role-edit";
    }

    @PreAuthorize("hasAnyAuthority('sys:role:edit')")
    @RequestMapping("/findPermissionByRoleId")
    @ResponseBody
    public Results findPermissionByRoleId(Long id){
        return roleService.findPermissionByRoleId(id);
    }

    @PreAuthorize("hasAnyAuthority('sys:role:edit')")
    @RequestMapping("/editRoleAndPermission")
    @ResponseBody
    public Results editRoleAndPermission(@RequestBody RoleDto roleDto){
        return roleService.editRoleAndPermission(roleDto);
    }

    @PreAuthorize("hasAnyAuthority('sys:role:del')")
    @RequestMapping("/delRoleAndRolePermission")
    @ResponseBody
    public Results delRoleAndRolePermission(Long id){
        return roleService.delRoleAndRolePermission(id);
    }


}
