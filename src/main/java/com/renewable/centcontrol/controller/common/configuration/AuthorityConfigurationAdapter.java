package com.renewable.centcontrol.controller.common.configuration;

import com.renewable.centcontrol.controller.common.interceptor.AuthorityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description：
 * @Author: jarry
 */
@Configuration
//public class AuthorityConfigurationAdapter extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns("/manage/**");
//    }
//}

// WebMvcConfigurerAdapter已经在Spring5中过时，这里采用新的WebMvcConfigurer。
// 使用方法：1.直接实现该接口（内部代码与原先几乎一致）；2.继承WebMvcConfigurationSupport，从而重写其中方法
// @Link:https://www.cnblogs.com/unknows/p/10620685.html
public class AuthorityConfigurationAdapter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns("/manage/**");
    }
}
