package com.xinghai.dto;

import com.xinghai.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto extends Role {

    /**
     * 菜单集合
     */
    private List<Long> permissionIds;
}
