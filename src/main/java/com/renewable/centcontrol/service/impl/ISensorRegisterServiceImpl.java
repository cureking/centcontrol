package com.renewable.centcontrol.service.impl;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.common.constant.SensorRegisterConstant;
import com.renewable.centcontrol.dao.SensorRegisterMapper;
import com.renewable.centcontrol.pojo.InitializationInclination;
import com.renewable.centcontrol.pojo.Sensor;
import com.renewable.centcontrol.pojo.SensorRegister;
import com.renewable.centcontrol.pojo.SerialSensor;
import com.renewable.centcontrol.rabbitmq.producer.SensorRegisterProducer;
import com.renewable.centcontrol.service.IInitializationInclinationService;
import com.renewable.centcontrol.service.ISensorRegisterService;
import com.renewable.centcontrol.service.ISerialSensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Service("iSensorRegisterServiceImpl")
public class ISensorRegisterServiceImpl implements ISensorRegisterService {

    @Autowired
    private SensorRegisterMapper sensorRegisterMapper;

    @Autowired
    private SensorRegisterProducer sensorRegisterProducer;

    @Autowired
    private ISerialSensorService iSerialSensorService;

    @Autowired
    private IInitializationInclinationService iInitializationInclinationService;

    public ServerResponse updateSensorRegisteredStatusBySensorRegisterId(int sensorRegisterId, byte targetStatusCode) {
        // 1.校验数据

        SensorRegister sensorRegisterUpdateStatus = new SensorRegister();
        sensorRegisterUpdateStatus.setId(sensorRegisterId);
        sensorRegisterUpdateStatus.setStatus(targetStatusCode);

        return this.updateSensorRegister(sensorRegisterUpdateStatus);
    }

    public ServerResponse updateSensorRegister(SensorRegister sensorRegister) {
        if (sensorRegister == null) {
            log.warn("the sensorRegister is null ! the sensorRegister is {} .", sensorRegister);
            return ServerResponse.createByErrorMessage("the sensorRegister is null !");
        }

        SensorRegister sensorRegisterUpdate = new SensorRegister();
        sensorRegisterUpdate = sensorRegister;      // 这里日后可扩展为assembleVo，这里先不做扩展

        int countRow = sensorRegisterMapper.updateByPrimaryKeySelective(sensorRegisterUpdate);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("the sensorRegister update fail !");
        }
        return ServerResponse.createBySuccessMessage("the sensorRegister update success !");
    }

    public ServerResponse addSensorRegisterList(List<SensorRegister> sensorRegisterList){
        // 由于时间关系，这里先采用循环调用单数据add的方法。之后再写专门的batch方法（batch更有挑战，不是嘛）。循环调用，会加长流程，从而影响使用。不过就目前而言，0.1s与0.01s没什么区别
        if (sensorRegisterList == null || sensorRegisterList.size() == 0){
            return  ServerResponse.createByErrorMessage("the sensorRegister is null or the size of the sensorRegister is zero !");
        }
        for (SensorRegister sensorRegister : sensorRegisterList) {
            ServerResponse addServerRegisterResponse = this.insertSensorRegister(sensorRegister);
            if (addServerRegisterResponse.isFail()){
                // 这里可以做个简单的异常处理
//                continue;
                return addServerRegisterResponse;
            }
            ServerResponse sendSesnorRegitster2MQResponse = this.sendSensorRegitster2MQ(sensorRegister);
            if (addServerRegisterResponse.isFail()){
                return addServerRegisterResponse;
            }
        }
        return ServerResponse.createBySuccessMessage("the sensorRegisterList has dealed .");
    }

    public ServerResponse insertSensorRegister(SensorRegister sensorRegister){
        // 1.校验数据
        if (sensorRegister == null){
            return ServerResponse.createByErrorMessage("the sensorRegister is null !");
        }

        // 2.装载数据
        SensorRegister sensorRegisterAdded = new SensorRegister();
        sensorRegisterAdded = sensorRegister;       // 这里简化处理
        sensorRegisterAdded.setStatus(SensorRegisterConstant.SensorRegisterStatusEnum.Running.getCode());   // 新增是否应该标记为初始状态，从而提醒相关工作人员进行初始化配置

        // 3.将数据保存至数据库
        int countRow = sensorRegisterMapper.insertSelective(sensorRegisterAdded);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("the sensorRegister insert fail !");
        }

        // 5.返回成功响应
        return ServerResponse.createBySuccessMessage("sensorRegister has inserted .");
    }

    public ServerResponse<Integer> insertSensorRegisterAndRetureId(SensorRegister sensorRegister){
        // 1.校验数据
        if (sensorRegister == null){
            return ServerResponse.createByErrorMessage("the sensorRegister is null !");
        }

        // 2.装载数据
        SensorRegister sensorRegisterAdded = new SensorRegister();
        sensorRegisterAdded = sensorRegister;       // 这里简化处理
        sensorRegisterAdded.setStatus(SensorRegisterConstant.SensorRegisterStatusEnum.Running.getCode());   // 新增是否应该标记为初始状态，从而提醒相关工作人员进行初始化配置

        // 3.将数据保存至数据库
        Integer insertedId = sensorRegisterMapper.insertSelectiveAndReturnId(sensorRegisterAdded);
        if (insertedId == null){
            return ServerResponse.createByErrorMessage("the sensorRegister insert fail !");
        }

        // 5.返回成功响应
        return ServerResponse.createBySuccess("sensorRegister has inserted .",insertedId);
    }

    public ServerResponse addSensorRegisterListBySerialSensorList(List<SerialSensor> serialSensorList){
        // 1.校验数据
        if (serialSensorList == null || serialSensorList.size() == 0){
            return  ServerResponse.createByErrorMessage("the sensorRegister is null or the size of the sensorRegister is zero !");
        }

        // 2.对每个数据进行对应处理（包括调用对应serialSensor，initializationInclination等服务）
        for (SerialSensor serialSensor : serialSensorList) {
            ServerResponse addSensorRegisterResponse = this.addSensorRegisterBySerialSensor(serialSensor);
            if (addSensorRegisterResponse.isFail()){
                return addSensorRegisterResponse;
            }
        }

        // 3.返回成功响应
        return ServerResponse.createBySuccessMessage("the sensorRegisterList has dealed .");
    }

    private ServerResponse addSensorRegisterBySerialSensor(SerialSensor serialSensor){

        // 1.调用sensorRegister的add服务（包含保存与配置同步）
        SensorRegister addSensorRegister = this.generateSensorRegisterBySerialSensor(serialSensor);
        ServerResponse addSensorRegisterResponse = this.addSensorRegister(addSensorRegister);
        if (addSensorRegisterResponse.isFail()){
            return addSensorRegisterResponse;
        }

        // 1+.设置serialSensor的sensorRegister_id。     // 这步非常重要，这是唯一可以粘合sensorRegister,serialSensor,initializationInclination的地方
        int sensorRegisterId = (int)addSensorRegisterResponse.getData();
        serialSensor.setSensorRegisterId(sensorRegisterId);

        // 2.调用相关serialSensor的add服务（包含保存与配置同步）
        ServerResponse addSerialSensorResponse = iSerialSensorService.addSerialSensor(serialSensor);
        if (addSerialSensorResponse.isFail()){
            return addSerialSensorResponse;
        }

        // 3.调用相关initializationInclinationService的add服务（包含保存与配置同步）
        // TODO 其实这里应该根据sensor的function_id来进行判断是进行哪种数据的初始化
        InitializationInclination initializationInclination = this.generateInitializationInclinationBySerialSensor(serialSensor);
        ServerResponse addInitializationInclinationResponse = iInitializationInclinationService.addInitializationInclination(initializationInclination);
        if (addInitializationInclinationResponse.isFail()){
            return addInitializationInclinationResponse;
        }

        // 4.返回成功响应
        return ServerResponse.createBySuccessMessage("sensorRegister has add from mq (include sensorRegister,serialSensor,initialization).");
    }

    private ServerResponse<Integer> addSensorRegister(SensorRegister sensorRegister){
        // 1.数据校验
        if (sensorRegister == null){
            return ServerResponse.createByErrorMessage("sensorRegister is null !");
        }

        // 2.数据组装
        SensorRegister insertSensorRegister = sensorRegister;

        // 3.保存sensorRegister至数据库
        ServerResponse addSensorRegisterResponse = this.insertSensorRegisterAndRetureId(insertSensorRegister);
        if (addSensorRegisterResponse.isFail()){
            return addSensorRegisterResponse;
        }

        // 3+.设置SensorRegister的ID
        int insertId = (int)addSensorRegisterResponse.getData();
        insertSensorRegister.setId(insertId);

        // 4.将上述sensorRegister发送MQ，更新终端配置
        ServerResponse sendSesnorRegitster2MQResponse = this.sendSensorRegitster2MQ(insertSensorRegister);
        if (sendSesnorRegitster2MQResponse.isFail()){
            return sendSesnorRegitster2MQResponse;
        }

        // 5.返回成功响应
        return ServerResponse.createBySuccess("sensorRegister added .",insertId);
    }

    private SensorRegister generateSensorRegisterBySerialSensor(SerialSensor serialSensor){
        SensorRegister sensorRegister = new SensorRegister();
        sensorRegister.setNickname("No named");
        sensorRegister.setTerminalId(serialSensor.getTerminalId());
        sensorRegister.setStatus(SensorRegisterConstant.SensorRegisterStatusEnum.Running.getCode());

        sensorRegister.setCreateTime(new Date());
        sensorRegister.setUpdateTime(new Date());

        return sensorRegister;
    }

    private InitializationInclination generateInitializationInclinationBySerialSensor(SerialSensor serialSensor){
        InitializationInclination initializationInclination = new InitializationInclination();

        // 这里可以进行初始化配置
        initializationInclination.setTerminalId(serialSensor.getTerminalId());
        initializationInclination.setSensorRegisterId(serialSensor.getSensorRegisterId());
        initializationInclination.setCreateTime(new Date());
        initializationInclination.setUpdateTime(new Date());

        return initializationInclination;
    }

    private ServerResponse sendSensorRegitster2MQ(SensorRegister sensorRegisterAdded){
        // 4.全局更新相关配置（主要针对目标终端机与本机） // 调用目标Producer方法
        try {
            sensorRegisterProducer.sendSensorRegister(sensorRegisterAdded);
        } catch (Exception e) {
            log.error("Exception:{}",e);
            return ServerResponse.createByErrorMessage("Exception: "+e.toString());
        }

        return ServerResponse.createBySuccessMessage("the sensorRegister has sended to mq .");
    }
}
