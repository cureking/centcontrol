package com.renewable.centcontrol.controller.backend;


import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.SerialSensor;
import com.renewable.centcontrol.service.ISerialSensorService;
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
@RequestMapping("/manage/sensor/serial/")
public class SerialSensorManageController {

	@Autowired
	private ISerialSensorService iSerialSensorService;

	@RequestMapping(value = "update_by_user.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse updateByUser(@RequestBody SerialSensor serialSensor){
		return iSerialSensorService.updateByUser(serialSensor);
	}

	/**
	 * 中控室刷新配置，可以通过发送全局更新配置消息，由各个终端服务器上传配置信息，进行更新。由于时间关系，这个放后面
	 * @return
	 */
//	@RequestMapping(value = "refresh.do", method = RequestMethod.GET)
//	@ResponseBody
//	public ServerResponse refresh(){
//		return iSerialSensorService.refresh();
//	}
}
