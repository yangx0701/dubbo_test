package com.yx.us.config;

import com.yx.api.entity.Role;
import com.yx.api.entity.User;
import com.yx.us.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

//@Component
public class TestConfig {

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void init(){
        User user = new User();
        user.setId(1);
        User userIndb = userMapper.selectOne(user);
        List<Role> roles  = userMapper.findRoleByUserId(1);
        System.out.println();
    }
}
