package com.xinghai.mapper;

import com.xinghai.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户查询列表
     * @param page
     * @param limit
     * @return
     */
    @Select("select id,username,telephone,status,birthday from sys_user order by id LIMIT #{page},#{limit}")
    List<User> findUsersByPage(@Param("page") Integer page,@Param("limit") Integer limit);

    /**
     * 统计
     * @return
     */
    @Select("select count(1) from sys_user")
    Long countAllUser();

    int getUserByPhone(String telephone);

    @Select("select count(1) from sys_user where username like concat('%',#{username},'%')")
    Long findUserByUserNameCount(String username);

    @Select("select id,username,telephone,status,birthday from sys_user where username like concat('%',#{username},'%') order by id LIMIT #{page},#{limit}")
    List<User> findUserByUserName(@Param("username") String username , @Param("page") Integer page , @Param("limit") Integer limit);

    @Select("select * from sys_user WHERE username = #{username}")
    User getUserByUserName(String username);

    @Update("update sys_user set password = #{password} where id = #{id}")
    int editUserPassword(Long id, String password);
}
