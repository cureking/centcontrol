package com.renewable.centcontrol.controller;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.ITerminalService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/terminal/")
public class TerminalController {

    @Autowired
    private ITerminalService iTerminalService;


    //现在规模还很小，所以列表可以直接返回完整信息，不需要再单独获取。日后数据量增大，保密性提高后，列表返回的数据，可以建立对应VO，来进行展示
    @RequestMapping(value = "get_list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "productId", defaultValue = "1") int productId) {

        return iTerminalService.getList(pageNum,pageSize,productId);
    }

    @RequestMapping(value = "get_detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(int productId){
        return ServerResponse.createByErrorMessage("response success");
    }
}
