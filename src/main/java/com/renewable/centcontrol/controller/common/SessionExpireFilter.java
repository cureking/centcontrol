package com.renewable.centcontrol.controller.common;

import com.renewable.centcontrol.common.RedisTemplateUtil;
import com.renewable.centcontrol.common.constant.RedisConstant;
import com.renewable.centcontrol.pojo.User;
import com.renewable.centcontrol.util.CookieUtil;
import com.renewable.centcontrol.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description：
 * @Author: jarry
 */
//@WebFilter(urlPatterns = "/")
//@Configuration
@WebFilter(urlPatterns = "/",filterName = "sessionFilter")
@Component
public class SessionExpireFilter implements Filter {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        this.filterName = filterConfig.getFilterName();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("SessionExpireFilter working ");
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if(StringUtils.isNotEmpty(loginToken)){
            //判断logintoken是否为空或者""；
            //如果不为空的话，符合条件，继续拿user信息

            String userJsonStr = redisTemplateUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令
                redisTemplateUtil.expire(loginToken, RedisConstant.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }


    @Override
    public void destroy() {

    }
}
