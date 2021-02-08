package com.xinghai.controller;


import com.xinghai.base.result.Results;
import com.xinghai.entity.Permission;
import com.xinghai.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/listAllPermission")
    @ResponseBody
    public Results findPermissionAll(){
        return permissionService.findPermissionAll();
    }

    @GetMapping("/findMenuAll")
    @ResponseBody
    public Results findMenuAll(){
        return permissionService.findMenuAll();
    }


    @PreAuthorize("hasAnyAuthority('sys:menu:add')")
    @RequestMapping("/add")
    public String add(Model model, Permission permission){
        model.addAttribute(new Permission());
        return "permission/permission-add";
    }

    @PreAuthorize("hasAnyAuthority('sys:menu:add')")
    @PostMapping("/addPermission")
    @ResponseBody
    public Results addPermission(@RequestBody Permission permission){
        return permissionService.addPermission(permission);
    }

    @PreAuthorize("hasAnyAuthority('sys:menu:edit')")
    @RequestMapping("/edit")
    public String edit(Model model, Long id){
        model.addAttribute(permissionService.findPermissionById(id));
        return "permission/permission-add";
    }

    @PreAuthorize("hasAnyAuthority('sys:menu:edit')")
    @PostMapping("/editPermission")
    @ResponseBody
    public Results editPermission(@RequestBody Permission permission){
        return permissionService.editPermission(permission);
    }

    @PreAuthorize("hasAnyAuthority('sys:menu:del')")
    @RequestMapping("/delPermission")
    @ResponseBody
    public Results delPermission(Long id){
        return permissionService.delPermission(id);
    }

    @RequestMapping("/findMenu")
    @ResponseBody
    public Results findMenu(Long userId){
        return permissionService.findMenuByUserId(userId);
    }
}
