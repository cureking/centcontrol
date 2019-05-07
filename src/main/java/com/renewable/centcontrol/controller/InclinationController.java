package com.renewable.centcontrol.controller;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.IInclinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/inclination/")
public class InclinationController {

    @Autowired
    private IInclinationService iInclinationService;


    //根据传感器id，pageHelper参数获取对应监控数据  //采用pageHelper技术
    @RequestMapping(value = "get_init_list",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getDataList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                int sensor_identifier) {    //这里的snesor_id指的是类似0x68这种标示符，并不是后来所作的传感器注册表中的id
//        return iInclinationService.getDataList(pageNum, pageSize, sensor_identifier);
        return null;
    }
    //根据传感器id，时间区域获取对应监控数据
    @RequestMapping(value = "get_data_list_time.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Object>> getDataListByTime(@RequestParam(value = "startTime", defaultValue = "1970-1-1 0:0:0") String startTime,
                                                          @RequestParam(value = "endTime", defaultValue = "2919-33-14 11:33:54") String endTime,
                                                          int sensor_identifier) {
//        return iInclinationService.getDataListByTime(startTime, endTime, sensor_identifier);
        return null;
    }
}
