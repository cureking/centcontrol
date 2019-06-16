package com.renewable.centcontrol.service;


import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Sensor;

/**
 * @Description：
 * @Author: jarry
 */
public interface ISensorService {

	ServerResponse insertSensor(Sensor sensor);

	ServerResponse updateSensor(Sensor sensor);

	ServerResponse getSensorById(Integer sensorId);

	ServerResponse listSensor(int pageNum, int pageSize);

}
