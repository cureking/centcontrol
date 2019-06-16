package com.renewable.centcontrol.controller.backend;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Sensor;
import com.renewable.centcontrol.service.ISensorService;
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
@RequestMapping("/manage/sensor/")
public class SensorManageController {

	@Autowired
	private ISensorService iSensorService;

	@RequestMapping(value = "update.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse updateSensor(@RequestBody Sensor sensor){
		return iSensorService.updateSensor(sensor);
	}

	@RequestMapping(value = "insert.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse insertSensor(@RequestBody Sensor sensor){
		return iSensorService.insertSensor(sensor);
	}


}
