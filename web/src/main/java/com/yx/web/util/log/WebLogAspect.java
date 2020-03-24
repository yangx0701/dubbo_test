package com.yx.web.util.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    //开始时间
    private long startTime = 0L;

    //结束时间
    private long endTime = 0L;

    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.yx.web.controller.*.*(..))")'
     */
    @Pointcut("@annotation(com.yx.web.util.log.Loggable)")
    public void pointCut(){}

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint){}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        //获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //目标方法体
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        boolean hasMethodLogAnno = method.isAnnotationPresent(Loggable.class);
        //没加注解 直接执行返回结果
        if(!hasMethodLogAnno){
            return point.proceed();
        }


        // 记录下请求内容
        logger.info("################URL : " + request.getRequestURL().toString());
        logger.info("################HTTP_METHOD : " + request.getMethod());
        logger.info("################IP : " + request.getRemoteAddr());
        logger.info("################THE ARGS OF THE CONTROLLER : " + Arrays.toString(point.getArgs()));

        //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
        logger.info("################CLASS_METHOD : " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        //logger.info("################TARGET: " + joinPoint.getTarget());//返回的是需要加强的目标类的对象
        //logger.info("################THIS: " + joinPoint.getThis());//返回的是经过加强后的代理类的对象

        //日志打印外部开关默认关闭
        String logSwitch = "";

        //方法注解实体
        Loggable methodLogAnnon = method.getAnnotation(Loggable.class);

        //处理日志参数
        handleRequestLog(logSwitch,point,methodLogAnnon,request);

        //执行目标方法内容，获取执行结果
        Object result = point.proceed();

        //处理接口响应日志
        handleResponseLog(logSwitch,methodLogAnnon,result);

        return result;
   }

    /**
     * 处理入参日志
     * @param logSwitch  开关
     * @param point      切点
     * @param methodLogAnnon  日志注解
     * @param request
     */
    private void handleRequestLog(String logSwitch, ProceedingJoinPoint point, Loggable methodLogAnnon, HttpServletRequest request) {
        startTime = System.currentTimeMillis();
    }

    /**
     * @param logSwitch         开关
     * @param methodLogAnnon    日志注解
     * @param result            接口执行结果
     */
    private void handleResponseLog(String logSwitch, Loggable methodLogAnnon, Object result) {
        endTime = System.currentTimeMillis();
        logger.info(result + "耗时" +( endTime - startTime)+"ms");
    }


}
