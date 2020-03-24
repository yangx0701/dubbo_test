package com.yx.web.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.yx.api.entity.Create;
import com.yx.api.entity.exception.MyException;
import com.yx.api.entity.UserLogin;
import com.yx.web.entity.ResponseBean;
import com.yx.api.entity.User;
import com.yx.api.service.UserService;
import com.yx.web.util.log.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    /*    @Reference(version = "1.0.0")
    private FileService fileService;*/

    /*    @Reference(version = "1.0.0",url = "dubbo://127.0.0.1:12345",check="false")  zookeeper去除url    xml配置用下面
    @Autowired 此注解也可用于xml配置时*/
    @Autowired
    private UserService userService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/sayHello")
    public String sayHello() {
        return userService.sayHello();
    }


    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Loggable
    @PostMapping("/login")
    public ResponseBean login(@RequestBody UserLogin user) {
        Object obj = userService.login(user);
        String res = (String) obj;
        if ("认证通过".equals(res)) {
            log.debug("用户已登录: " + user);
            return ResponseBean.success_nodata(res);
        } else {
            return ResponseBean.fail_nodata(res);
        }
    }

    @GetMapping("/validateMsg")
    public ResponseBean validateMsg(@RequestParam("validateMsg")String validateMsg) {
        System.out.println(validateMsg);

        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",true);
        return ResponseBean.success_withdata("",resultMap);
    }


    @PostMapping("/register")
    public ResponseBean register(@RequestBody @Validated(Create.class) User user, BindingResult error) {
        if (error.hasErrors()) {
            return ResponseBean.fail_nodata(Objects.requireNonNull(error.getFieldError()).getDefaultMessage());
        }
        boolean exist_name = (boolean) userService.checkUsername(user);
        boolean exist_tel =  userService.checkTelNo(user);
        if (!exist_name) {
            return ResponseBean.fail_nodata("用户名已存在");
        }
        if (!exist_tel) {
            return ResponseBean.fail_nodata("手机号已被注册");
        }
        else {
            userService.saveUser(user);
            return ResponseBean.success_nodata("注册成功");
        }
    }

    @GetMapping("/defaultKaptcha")
    public void getKaptcha(HttpServletRequest req, HttpServletResponse res) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try{
            //生产验证码字符串并保存到session
            String createText = defaultKaptcha.createText();
            req.getSession().setAttribute("verifyCode",createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge,"jpg",jpegOutputStream);
        }catch(IllegalArgumentException e){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        res.setHeader("Cache-Control", "no-store");
        res.setHeader("Pragma","no-cache");
        res.setDateHeader("Expires",0);
        res.setContentType("image/jpeg");
        ServletOutputStream resOutputStream =
                res.getOutputStream();
        resOutputStream.write(captchaChallengeAsJpeg);
        resOutputStream.flush();
        resOutputStream.close();
    }

    @GetMapping("/verifyKaptcha")
    public boolean verifyKaptcha(HttpServletRequest req,HttpServletResponse res){
        String captchaId = (String) req.getSession().getAttribute("verifyCode");
        if(captchaId==null){
            return false;
        }
        String parameter = req.getParameter("verifyCode");
        return captchaId.equalsIgnoreCase(parameter);
    }

    @GetMapping("/getUserByTelNo")
    public ResponseBean getUserByTelNo(@RequestParam("telNo") String telNo) {
        User user;
        try {
            user = userService.getUserByTelNo(telNo);
        }catch(Exception e){
            if(e.getMessage().contains("Access denied")) {
                throw new UnauthenticatedException();
            }else{
                throw new MyException("未知错误",e.getMessage());
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("用户信息", user);
        return ResponseBean.success_withdata("获取用户信息", map);
    }
}
