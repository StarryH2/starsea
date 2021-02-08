package com.xinghai.mapper;

import com.xinghai.entity.RoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xinghai
 * @since 2021-02-06
 */
@Mapper
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    /**
     * 新增用户角色
     * @param roleUser
     * @return
     */
    @Insert("INSERT INTO sys_role_user  (user_id,role_id)  VALUES  (#{userId},#{roleId});")
    int insertRoleUser(RoleUser roleUser);

    /**
     * 修改用户角色
     * @param roleUser
     * @return
     */
    @Insert("update sys_role_user set role_id = #{roleId} WHERE user_id = #{userId};")
    int updateRoleUser(RoleUser roleUser);
}
