package com.renewable.centcontrol.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.dao.SensorMapper;
import com.renewable.centcontrol.pojo.Sensor;
import com.renewable.centcontrol.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Service("iSensorServiceImpl")
public class ISensorServiceImpl implements ISensorService {

	@Autowired
	private SensorMapper sensorMapper;

	@Override
	public ServerResponse insertSensor(Sensor sensor) {
		if (sensor == null){
			return ServerResponse.createByErrorMessage("the sensor is null !");
		}

		Sensor sensorInsert = sensor;
		sensorInsert.setId(null);

		int countRow = sensorMapper.insertSelective(sensorInsert);
		if (countRow == 0){
			return ServerResponse.createByErrorMessage("sensor insert fail !");
		}

		return ServerResponse.createBySuccessMessage("sensor insert success .");
	}

	@Override
	public ServerResponse updateSensor(Sensor sensor) {
		if (sensor == null){
			return ServerResponse.createByErrorMessage("the sensor is null !");
		}
		if (sensor.getId() == null){
			return ServerResponse.createByErrorMessage("the id of the sensor is null !");
		}

		Sensor sensorUpdate = sensor;

		int countRow = sensorMapper.updateByPrimaryKeySelective(sensorUpdate);
		if (countRow == 0){
			return ServerResponse.createByErrorMessage("sensor update fail !");
		}

		return ServerResponse.createBySuccessMessage("sensor update success .");
	}

	@Override
	public ServerResponse getSensorById(Integer sensorId) {
		if (sensorId == null){
			return ServerResponse.createByErrorMessage("the sensorId is null !");
		}
		Sensor sensor = null;
		sensor = sensorMapper.selectByPrimaryKey(sensorId);
		if (sensor == null){
			return ServerResponse.createByErrorMessage("there is no sensor with the id: "+sensorId);
		}
		return ServerResponse.createBySuccess(sensor);
	}

	@Override
	public ServerResponse listSensor(int pageNum, int pageSize) {

		//第一步：startPage--start
		PageHelper.startPage(pageNum, pageSize);

		//第二步：填充自己的sql查询逻辑
		List<Sensor> sensorList = null;
		sensorList = sensorMapper.listSensor();
		if (sensorList == null || sensorList.size() == 0){
			return ServerResponse.createByErrorMessage("there is no sensor !");
		}

		//这里由于业务简单，而且对数据隐蔽性要求不高，这里先不急着写VO了
		//此处日后可扩展VO
		List<Sensor> sensorVoList = Lists.newArrayList();
		for (Sensor sensorItem : sensorList) {
			Sensor sensorVo = (Sensor) sensorItem;   // 可以建立assembleVo()，而不是强转
			sensorVoList.add(sensorVo);
		}

		//第三步：pageHelper--收尾
		PageInfo pageResult = new PageInfo(sensorList);
		pageResult.setList(sensorVoList);
		return ServerResponse.createBySuccess(pageResult);
	}

}
