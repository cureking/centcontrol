package com.renewable.centcontrol.controller.portal;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.InitializationInclination;
import com.renewable.centcontrol.service.IInitializationInclinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/initialization_inclination/")
public class InitializationInclinationController {

    @Autowired
    private IInitializationInclinationService iInitializationInclinationService;

    @RequestMapping(value = "get_initialization_inclination_by_id.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getInitializationInclinationById(@RequestParam Integer id){
        return iInitializationInclinationService.getInitializationInclinationById(id);
    }

    @RequestMapping(value = "get_initialization_inclination_by_sensor_register_id.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getInitializationInclinationBySensorRegisterId(@RequestParam Integer sensorRegisterId){
        return iInitializationInclinationService.getInitializationInclinationBySensorRegisterId(sensorRegisterId);
    }

    @RequestMapping(value = "list_initialization_inclination_by_terminal_id.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listInitializationInclinationByTerminalId(@RequestParam Integer terminalId){
        return iInitializationInclinationService.listInitializationInclinationByTerminalId(terminalId);
    }

}
