package com.imooc.aspect;

import com.imooc.enums.ResultEnum;
import com.imooc.exception.GirlException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 廖师兄
 * 2017-01-15 12:31
 */
@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    //用户身份验证用这个搞， throw new GirlException(ResultEnum.USERAACOUNT_ERROR);
    @Order(1)
    @Pointcut("execution(public * com.imooc.controller.GirlController.*(..))")
    public void log() {
    }

    @Order(2)
    @Pointcut("execution(public * com.imooc.controller.GirlController.*(..))")
    public void log2() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        logger.info("url={}", request.getRequestURL());

        //method
        logger.info("method={}", request.getMethod());

        //ip
        logger.info("ip={}", request.getRemoteAddr());

        //类方法
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        //参数
        logger.info("args={}", joinPoint.getArgs());

        //处理方式①
        //如果不符合业务逻辑，那么就抛出异常（自定义异常处理，很棒！），返回给客户端。
        //throw new GirlException(ResultEnum.USERAACOUNT_ERROR);

        //处理方式②
        //程序异常择抛出系统异常
        try{
            int i = 1/3;

        }catch (Exception ex){
            throw ex;
        }

    }


    @After("log()")
    public void doAfter() {
        logger.info("222222222222");
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        logger.info("response={}", object.toString());
    }

}
