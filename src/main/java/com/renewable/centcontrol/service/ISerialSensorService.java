package com.renewable.centcontrol.service;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.SerialSensor;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface ISerialSensorService {

    ServerResponse listSerialSensor(int pageNum, int pageSize, Integer terminalId);

    ServerResponse getSerialSensor(Integer serialSensorId);

    ServerResponse updateByUser(SerialSensor serialSensor);

    ServerResponse dealSerialSensorListFromMQ(List<SerialSensor> serialSensorList);

    ServerResponse addSerialSensor(SerialSensor serialSensor);
}
