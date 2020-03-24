package com.yx.web.exception;

import com.yx.api.entity.exception.MyException;
import com.yx.web.entity.ResponseBean;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ShiroExceptionhandler {

    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseBody
    public ResponseBean unAuthExphandler(){
        return ResponseBean.fail_nodata("权限不足");
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResponseBean myExphandler(HttpServletRequest req, MyException e){
        return ResponseBean.fail_nodata(e.getMsg());
    }
}
