package com.atguigu.gmall.interceptors;

import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.util.CookieUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{

public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    //拦截代码

    //判断被拦截的请求的访问的方法的注解(是否是需要拦截的)
    HandlerMethod hm=(HandlerMethod)handler;
    LoginRequired methodAnnotation =hm.getMethodAnnotation(LoginRequired.class);
    if (methodAnnotation==null){
        return true;
    }
    boolean loginSuccess=methodAnnotation.loginSuccess();//获得该请求是否必须登陆成功
    System.out.println("进入拦截器的拦截方法");
    return true;
}
}
