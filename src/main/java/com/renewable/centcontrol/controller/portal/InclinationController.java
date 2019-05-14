package com.renewable.centcontrol.controller.portal;

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
    @RequestMapping(value = "list_init_page.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listInitDataByPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                int terminalId, int sensorId) {    //这里的snesor_id指的是类似0x68这种标示符，并不是后来所作的传感器注册表中的id
        return iInclinationService.listInitDataByPage(pageNum, pageSize, terminalId, sensorId);
    }


    @RequestMapping(value = "list_init_time.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Object>> listInitDataByTime(@RequestParam(value = "startTime", defaultValue = "1970-1-1 0:0:0") String startTime,
                                                          @RequestParam(value = "endTime", defaultValue = "2919-33-14 11:33:54") String endTime,
                                                           int terminalId, int sensorId) {
        return iInclinationService.listInitDataByTime(startTime, endTime, terminalId, sensorId);
    }


    @RequestMapping(value = "get_init_id.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getInitDataById(long inclinationInitId) {
        System.out.println(inclinationInitId);
        return iInclinationService.getInitDataById(inclinationInitId);
    }


    @RequestMapping(value = "list_total_page.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listTotalDataByPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                            int terminalId, int sensorId) {
        return iInclinationService.listTotalDataByPage(pageNum, pageSize, terminalId, sensorId);
    }


    @RequestMapping(value = "list_total_time.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Object>> listTotalDataByTime(@RequestParam(value = "startTime", defaultValue = "1970-1-1 0:0:0") String startTime,
                                                           @RequestParam(value = "endTime", defaultValue = "2919-33-14 11:33:54") String endTime,
                                                            int terminalId, int sensorId) {
        return iInclinationService.listTotalDataByTime(startTime, endTime, terminalId, sensorId);
    }


    @RequestMapping(value = "get_total_id.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getTotalDataById(long inclinationTotalId) {
        return iInclinationService.getTotalDataById(inclinationTotalId);
    }
}
