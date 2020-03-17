package com.yx.api.service;

import com.yx.api.entity.User;
import com.yx.api.entity.UserLogin;

public interface UserService {

    String sayHello();

    /**
     * 登录
     * @param user
     * @return
     */
    Object login(UserLogin user);

    /**
     * @param obj 检测用户名是否可用
     * @return
     */
    Object checkUsername(User obj);

    /**保存用户
     * @param user
     */
    void saveUser(User user);

    /**
     * 检测手机号是否可用
     * @param user
     * @return
     */
    boolean checkTelNo(User user);

    /**
     * 根据手机号码获取用户信息
     * @param telNo
     * @return
     */
    User getUserByTelNo(String telNo);
}
