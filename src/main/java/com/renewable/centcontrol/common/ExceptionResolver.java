package com.renewable.centcontrol.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {    // HandlerExceptionResolver接口的实现，表示要进行全局异常处理

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("{} Exception", httpServletRequest.getRequestURI(), e);
        // 这里不希望返回一个ModelAndView，而是希望返回JsonView
        // Jackson1.9只支持使用MappingJacksonJsonView()，该项目中Jackson采用了高版本，所以直接使用MappingJackson2JsonView()
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        // 模仿ServerResponse这一通用返回格式（但是由于该函数只能返回ModelAndView，所以不能直接返回ServerResponse()
        modelAndView.addObject("status", ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg", "接口异常,详情请查看服务端日志的异常信息");
        modelAndView.addObject("data", e.toString());
        return modelAndView;
    }
}