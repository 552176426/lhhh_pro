package com.itheima.dao;

import com.itheima.domain.Permission;
import com.itheima.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleDao {

    @Select("select * from role where id in (select roleId from users_role where userId=#{userId})")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "roleName",column ="rolename"),
            @Result(property = "roleDesc",column ="roleDesc"),
            @Result(property = "permissions",column ="id",javaType = java.util.List.class,
                    many = @Many(select = "com.itheima.dao.PermissionDao.findPermissionByRoleId")),

    })
    List<Role> findRoleByUserId(String userId) throws Exception;

    @Select("select * from role")
    List<Role> findAll() throws Exception;

    @Insert("insert into role(rolename,roledesc) values(#{roleName},#{roleDesc})")
    void save(Role role) throws Exception;

    @Select("select * from role where id = #{id}")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "roleName",column = "roleName"),
            @Result(property = "roleDesc",column = "roleDesc"),
            @Result(property = "permissions",column = "id",javaType = java.util.List.class,
            many = @Many(select = "com.itheima.dao.PermissionDao.findPermissionByRoleId"))
    })
    Role findById(String id) throws Exception;

    @Select("select * from permission where id not in (select permissionid from role_permission where roleid=#{roleId})")
    List<Permission> findOtherPermissions(String roleId);

    @Insert("insert into role_permission(roleId,permissionId) values(#{roleId},#{permissionId})")
    /*@Param给参数进行命名，使得#{roleId},#{permissionId}能引用到，如果不命名则会找不到参数而抛异常*/
    void addPermissionToRole(@Param("roleId") String roleId,@Param("permissionId") String permissionId);

    @Delete("delete from users_role where roleid=#{roleId}")
    void deleteFromUser_RoleByRoleId(String roleId) throws Exception;

    @Delete(("delete from role_permission where roleId=#{roleId}"))
    void deleteFromRole_PermissionByRoleId(String roleId) throws Exception;

    @Delete("delete from role where id = #{roleId}")
    void deleteRoleById(String roleId) throws Exception;
}
