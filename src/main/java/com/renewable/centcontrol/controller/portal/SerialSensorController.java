package com.renewable.centcontrol.controller.portal;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.IInitializationInclinationService;
import com.renewable.centcontrol.service.ISerialSensorService;
import org.apache.ibatis.annotations.Param;
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
@RequestMapping("/sensor/serial/")
public class SerialSensorController {

    @Autowired
    private ISerialSensorService iSerialSensorService;

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listSerialSensor(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "terminalId", defaultValue = "") Integer terminalId){
        return iSerialSensorService.listSerialSensor(pageNum, pageSize, terminalId);
    }

    @RequestMapping(value = "get_serial_sensor.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getSerialSensor(@RequestParam(value = "serialSensorId") Integer serialSensorId){
        return iSerialSensorService.getSerialSensor(serialSensorId);
    }
}
