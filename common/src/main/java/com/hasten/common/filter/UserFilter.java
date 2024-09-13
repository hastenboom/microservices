package com.hasten.common.filter;

import com.hasten.common.UserInfoHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hasten
 */
public class UserFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getHeader("user-id");
        System.out.println("normal service has been intercepted, the user-id is: " + userId);
        UserInfoHolder.set(userId);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        UserInfoHolder.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
