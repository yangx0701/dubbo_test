package com.yx.us.service.impl.user;

import com.yx.api.entity.User;
import com.yx.api.entity.UserLogin;
import com.yx.us.mapper.UserMapper;
import com.yx.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Timestamp;

//@Service(version = "1.0.0")  xml替代则用Spring的@Service
//@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String sayHello() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        Subject subject = SecurityUtils.getSubject();
        return "hello";
    }

    @Override
    public Object login(UserLogin user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword(), user.isRememberMe());
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
//            subject.checkRole("admin");
//            subject.checkPermissions("query", "add");
        } catch (AuthenticationException e) {
            log.debug("账号或密码错误" + usernamePasswordToken);
            return "账号或密码错误";
        } catch (AuthorizationException e) {
            log.debug("没有权限" + usernamePasswordToken);
            return "没有权限";
        } catch (Exception e) {
            log.debug("系统异常" + usernamePasswordToken);
            return "系统异常";
        }
        return "认证通过";
    }

    @Override
    public Object checkUsername(User user) {
        User tmp = new User();
        tmp.setUsername(user.getUsername());
        User userDb = userMapper.selectOne(tmp);
        if (userDb == null) {
            return true;
        }
        log.debug("用户：" + user.getUsername() + " 已存在");
        return false;
    }

    @Override
    public void saveUser(User user) {
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userMapper.insert(user);
        log.debug("用户" + user.getUsername() + " 于" + user.getCreateTime() + " 录入系统");
    }

    public boolean checkTelNo(User user) {
        User tmp = new User();
        tmp.setTelNo(user.getTelNo());
        User userDb = userMapper.selectOne(tmp);
        return null == userDb;
    }

    @Override
    @RequiresAuthentication
    public User getUserByTelNo(String telNo) throws UnauthenticatedException {
        User tmp = new User();
        tmp.setTelNo(telNo);
        User userDb = userMapper.selectOne(tmp);
        return userDb;
    }
}
