package com.xinghai.mapper;

import com.xinghai.dto.RoleDto;
import com.xinghai.entity.Permission;
import com.xinghai.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select count(*) from sys_role")
    Long countAllRole();


    @Select("select id,name,description,update_time from sys_role order by id limit #{page},#{limit}")
    List<Role> findRolesByPage(@Param("page") Integer page, @Param("limit") Integer limit);

    @Select("select count(1) from sys_role where name like concat('%',#{roleName},'%')")
    Long countByRoleNameRole(String roleName);

    @Select("select id,name,description,update_time from sys_role where name like concat('%',#{roleName},'%') order by id LIMIT #{page},#{limit}")
    List<Role> findRolesByRoleNamePage(@Param("roleName") String roleName,@Param("page") Integer page,@Param("limit") Integer limit);

    int saveRolePermission(@Param("roleId") Long id,@Param("perIds") List<Long> perIds);


    List<Permission> findPermissionByRoleId(Long id);

    @Delete("delete from sys_role_permission where role_id = #{roleId};")
    int delRolePermission(Long roleId);

    int editRole(RoleDto roleDto);

    @Select("select count(*) from sys_role_user where role_id = #{id}")
    Integer findRoleUserCount(Long id);
}
