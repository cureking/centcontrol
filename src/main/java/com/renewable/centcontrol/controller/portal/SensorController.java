package com.renewable.centcontrol.controller.portal;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.ISensorService;
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
@RequestMapping("/sensor/")
public class SensorController {

	@Autowired
	private ISensorService iSensorService;

	@RequestMapping(value = "list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse listSensor(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
									 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
		return iSensorService.listSensor(pageNum, pageSize);
	}

	@RequestMapping(value = "get_by_id.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getSensorById(@RequestParam(value = "sensorId", defaultValue = "") Integer sensorId){
		return iSensorService.getSensorById(sensorId);
	}

}
