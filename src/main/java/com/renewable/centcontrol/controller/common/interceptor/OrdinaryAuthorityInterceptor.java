package com.renewable.centcontrol.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.renewable.centcontrol.common.Const;
import com.renewable.centcontrol.common.RedisTemplateUtil;
import com.renewable.centcontrol.common.ResponseCode;
import com.renewable.centcontrol.pojo.User;
import com.renewable.centcontrol.util.CookieUtil;
import com.renewable.centcontrol.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Component
public class OrdinaryAuthorityInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		log.info("preHandle");

		//
		HandlerMethod handlerMethod = (HandlerMethod)handler;

		// 解析HandlerMethod
		String methodName = handlerMethod.getMethod().getName();
		/** 注意这里是通过getBean()来获取className的 **/
		String className = handlerMethod.getBean().getClass().getName();

		// [可选] 解析参数,具体的参数key以及value是什么，我们打印日志
		StringBuffer requestParamBuffer = new StringBuffer();
		Map paramMap = request.getParameterMap();
		Iterator it = paramMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String mapKey = (String) entry.getKey();

			String mapValue = StringUtils.EMPTY;

			//request这个参数的map，里面的value返回的是一个String[]
			Object obj = entry.getValue();
			if (obj instanceof String[]) {
				String[] strs = (String[]) obj;
				mapValue = Arrays.toString(strs);
			}
			requestParamBuffer.append(mapKey).append("=").append(mapValue);
		}

		/** 打印日志 */
		log.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}", className, methodName, requestParamBuffer.toString());

		/** 试图获取User信息 */
		User user = null;
		String loginToken = CookieUtil.readLoginToken(request);
		if (StringUtils.isNotEmpty(loginToken)) {
			String userJsonStr = redisTemplateUtil.get(loginToken);
			user = JsonUtil.string2Obj(userJsonStr, User.class);
		}

		/** 对获取的用户信息进行判断，从而决定是否拦截 */
		if (user == null) {
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");

			PrintWriter out = response.getWriter();

			Map resultMap = Maps.newHashMap();
			resultMap.put("success",false);
			resultMap.put("status", ResponseCode.NEED_LOGIN.getCode());
			resultMap.put("msg", "拦截器拦截,用户未登录。");
			out.print(JsonUtil.obj2String(resultMap));

			out.flush();
			out.close();

			return false;
		}
		return true;
	}
}
