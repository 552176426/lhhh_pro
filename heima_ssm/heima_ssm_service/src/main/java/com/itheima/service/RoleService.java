package com.itheima.service;

import com.itheima.domain.Permission;
import com.itheima.domain.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll() throws Exception;
    void save(Role role) throws Exception;
    Role findById(String id) throws Exception;
    List<Permission> findOtherPermissions(String roleId) throws Exception;

    void addPermissionToRole(String roleId,String[] permissionIds) throws Exception;

    void deleteRole(String id) throws Exception;
}
