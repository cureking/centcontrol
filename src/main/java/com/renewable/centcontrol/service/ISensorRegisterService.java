package com.renewable.centcontrol.service;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.SensorRegister;
import com.renewable.centcontrol.pojo.SerialSensor;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface ISensorRegisterService {

    // out
    ServerResponse selectSensorRegisterById(Integer sensorRegisterId);

    ServerResponse listSensorRegisterByTerminalId(Integer terminalId);

    ServerResponse listSensorRegister();

    ServerResponse updateSensorRegister(SensorRegister sensorRegister);


    ServerResponse updateSensorRegisteredStatusBySensorRegisterId(int sensorRegisterId, byte targetStatusCode);

    ServerResponse addSensorRegisterList(List<SensorRegister> sensorRegisterList);

    ServerResponse insertSensorRegister(SensorRegister sensorRegister);

    ServerResponse addSensorRegisterListBySerialSensorList(List<SerialSensor> serialSensorList);

    ServerResponse receiveSensorRegisterListFromMQ(List<SensorRegister> sensorRegisterList);
}
