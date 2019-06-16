package com.renewable.centcontrol.controller.common.configuration;

import com.renewable.centcontrol.controller.common.interceptor.ManageAuthorityInterceptor;
import com.renewable.centcontrol.controller.common.interceptor.OrdinaryAuthorityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description：
 * @Author: jarry
 */
@Configuration


/**
 *  WebMvcConfigurerAdapter已经在Spring5中过时，这里采用新的WebMvcConfigurer。
 *  使用方法：1.直接实现该接口（内部代码与原先几乎一致）；2.继承WebMvcConfigurationSupport，从而重写其中方法
 *  @Link:https://www.cnblogs.com/unknows/p/10620685.html
 * @Author:jarry
 */
public class AuthorityConfigurationAdapter implements WebMvcConfigurer {

	//    @Override
	//    public void addInterceptors(InterceptorRegistry registry){
	//        registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns("/manage/**");
	//    }

	// 上述方法无法在AuthorityInterceptor()中自动注入Bean，如RedisTemplateUtil。（又是因为存在new，Spring的注入，一遇到new就GG。必须通过@Bean显式）
	// @Link:https://stackoverflow.com/questions/23349180/java-config-for-spring-interceptor-where-interceptor-is-using-autowired-spring-b
	// @Link:https://blog.csdn.net/mjlfto/article/details/65635135

	@Bean
	ManageAuthorityInterceptor localManageInterceptor() {
		return new ManageAuthorityInterceptor();
	}

	@Bean
	OrdinaryAuthorityInterceptor localOrdinaryAuthorityInterceptor() {
		return new OrdinaryAuthorityInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		// 管理员权限拦截
		registry.addInterceptor(localManageInterceptor()).addPathPatterns("/manage/**").excludePathPatterns("/manage/user/login.do");

		// 普通用户权限拦截（目前只针对消警）
		registry.addInterceptor(localOrdinaryAuthorityInterceptor()).addPathPatterns("/warning_eliminate/*");
	}
}
