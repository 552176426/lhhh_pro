package com.itheima.service.Impl;

import com.itheima.dao.UserDao;
import com.itheima.domain.Role;
import com.itheima.domain.UserInfo;
import com.itheima.service.UserService;
import com.itheima.utils.BCryptPasswordEncoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = null;
        try {
            userInfo = userDao.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        User user = new User(userInfo.getUsername(), "{noop}"+userInfo.getPassword(), getAuthorityies(userInfo.getRoles()));
        User user = new User(userInfo.getUsername(),
                userInfo.getPassword(),
                userInfo.getStatus()==0?false:true,
                true,
                true,
                true,
                getAuthorityies(userInfo.getRoles()));
        return user;
    }
    public List<SimpleGrantedAuthority> getAuthorityies(List<Role> roles){

        List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
        for (Role role : roles) {
            String roleName = role.getRoleName();
            list.add(new SimpleGrantedAuthority("ROLE_"+roleName));
        }
        System.out.println("UserServiceImpl.java:getAuthorityies:"+list);
        return list;
    }

    @Override
    public List<UserInfo> findAll() throws Exception {
        List<UserInfo> users = userDao.findAll();
        return users;
    }

    @Override
    public void save(UserInfo userInfo) throws Exception {
        //对密码进行加密
        userInfo.setPassword(BCryptPasswordEncoderUtils.encodePassword(userInfo.getPassword()));
        userDao.save(userInfo);
    }

    @Override
    public UserInfo findById(String id) throws Exception {
        return userDao.findById(id);
    }

    @Override
    public List<Role> findOtherRolesByUserId(String id) throws Exception {
        return userDao.findOtherRolesByUserId(id);
    }

    @Override
    public void addRoleToUser(String userId, String[] roleIds) throws Exception {
        for (String roleId : roleIds) {
            userDao.addRoleToUser(userId,roleId);
        }
    }

}
