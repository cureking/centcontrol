package com.renewable.centcontrol.controller.portal;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.ISensorRegisterService;
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
@RequestMapping("/sensor/register/")
public class SensorRegisterController {

	@Autowired
	private ISensorRegisterService iSensorRegisterService;

	@RequestMapping(value = "list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse listSensorRegister(){
		return iSensorRegisterService.listSensorRegister();
	}

	@RequestMapping(value = "list_by_termianl_id.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse listSensorRegisterByTerminalId(@RequestParam(value = "terminalId",defaultValue = "") Integer terminalId){
		return iSensorRegisterService.listSensorRegisterByTerminalId(terminalId);
	}

	@RequestMapping(value = "get_by_id.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getSensorRegisterById(@RequestParam(value = "sensorRegisterId",defaultValue = "") Integer sensorRegisterId){
		return iSensorRegisterService.selectSensorRegisterById(sensorRegisterId);
	}
}
