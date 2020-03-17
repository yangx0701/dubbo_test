package com.yx.us.mapper;

import com.yx.api.entity.Permission;
import com.yx.api.entity.Role;
import com.yx.api.entity.User;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select("select ur.role_id as id, " +
            "r.name as name, " +
            "r.description as description " +
            " from  user_role ur left join role r on ur.role_id = r.id " +
            "where  ur.user_id = #{userId}")
    @Results(
            value = {
                    @Result(id = true,property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "description", column = "description"),
                    @Result(property = "permissionList", column = "id",javaType= List.class,
                            many = @Many(select = "com.yx.user.mapper.UserMapper.findPermissionByRoleId",fetchType = FetchType.DEFAULT))
            }
    )
    List<Role> findRoleByUserId(int userId);

    @Select("select p.id, p.name, p.url, p.description from role_permission rp " +
            "left join permission p on rp.permission_id = p.id " +
            "where rp.role_id = #{roleId}")
    List<Permission> findPermissionByRoleId(int roleId);

    @Select("select * from user where username = #{username} or tel_no = #{username}")
    User getUserByUsername(String username);

    @Select("select id from user where username = #{username}")
    Integer getIdByUsername(String username);
}
