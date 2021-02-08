package com.xinghai.mapper;

import com.xinghai.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xinghai
 * @since 2021-02-04
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     *
     * @return
     */
    @Select("select * from sys_permission t")
    List<Permission> findPermissionAll();

    @Select("select id,parent_id,name,css,href,type,permission,sort from sys_permission")
    List<Permission> findMenuAll();

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into sys_permission(parent_id, name, css, href, type, permission, sort) values(#{parentId}, #{name}, #{css}, #{href}, #{type}, #{permission}, #{sort})")
    int addPermission(Permission permission);

    int editPermission(Permission permission);

    @Select("select COUNT(1) from sys_role_permission WHERE permission_id = #{id}")
    int findRolePermissionByPerId(Long id);


    List<Permission> findMenuByUserId(Long userId);
}
