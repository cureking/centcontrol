package com.renewable.centcontrol.controller.backend;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.InitializationInclination;
import com.renewable.centcontrol.service.IInitializationInclinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/manage/initialization_inclination/")
public class InitializationInclinationManageController {
    @Autowired
    private IInitializationInclinationService iInitializationInclinationService;


    @RequestMapping(value = "update_initialization_inclination_by_id.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateInitializationInclinationById(@RequestBody InitializationInclination initializationInclination){
        return iInitializationInclinationService.updateInitializationInclinationById(initializationInclination);
    }
}
