package com.renewable.centcontrol.controller;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @RequestMapping(value = "get_list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return null;
    }

    @RequestMapping(value = "get_detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(int productId){
        return null;
    }

    //增减改，暂时不需要
    //甚至这个接口暂时都不用暴露，因为目前面对的企业，大多只使用一个产品，不过留一个空间
}
