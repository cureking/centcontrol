package com.renewable.centcontrol.service.impl;

import com.google.common.collect.Lists;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.dao.SerialSensorMapper;
import com.renewable.centcontrol.pojo.SerialSensor;
import com.renewable.centcontrol.rabbitmq.producer.SerialSensorProducer;
import com.renewable.centcontrol.service.ISensorRegisterService;
import com.renewable.centcontrol.service.ISerialSensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Service("iSerialSensorServiceImpl")
public class ISerialSensorServiceImpl implements ISerialSensorService {

    @Autowired
    private SerialSensorMapper serialSensorMapper;

    @Autowired
    private ISensorRegisterService iSensorRegisterService;

    @Autowired
    private SerialSensorProducer serialSensorProducer;

    public ServerResponse insert(SerialSensor serialSensor){
        // 1.数据校验（部分情况下还有权限校验）
        if (serialSensor == null || serialSensor.getPort() == null){
            return ServerResponse.createByErrorMessage("the serialSensor is null or the port of the serialSensor is null !");
        }

        // 2.serialSensorAssemble
        SerialSensor insertSerialSensor = serialSensor; // 这里简化一下

        // 3.插入数据
        int countRow = serialSensorMapper.insertSelective(insertSerialSensor);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("the serialSensor insert fail !");
        }

        // 4.返回成功响应
        return ServerResponse.createBySuccessMessage("the serialSensor insert success .");
    }

    public ServerResponse insertBatch(List<SerialSensor> serialSensorList){
        // 1.数据校验（部分情况下还有权限校验）
        if (serialSensorList == null || serialSensorList.size() == 0){
            return ServerResponse.createByErrorMessage("the serialSensorList is null or the size of the serialSensorList is zero !");
        }

        // 2.serialSensorAssemble
        List<SerialSensor> insertSerialSensorList = serialSensorList;// 这里简化一下
        if (insertSerialSensorList == null || insertSerialSensorList.size() == 0){
            return ServerResponse.createByErrorMessage("serialList assemble fail ! the serialList is :"+ insertSerialSensorList.toString());
        }

        // 3.插入数据
        int countRow = serialSensorMapper.insertBatch(insertSerialSensorList);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("the serialSensor insert fail !");
        }

        // 4.返回成功响应
        return ServerResponse.createBySuccessMessage("the serialSensor insert success .");
    }

    public ServerResponse update(SerialSensor serialSensor){
        // 1.数据校验（部分情况下还有权限校验）
        if (serialSensor == null || serialSensor.getPort() == null){
            return ServerResponse.createByErrorMessage("the serialSensor is null or the port of the serialSensor is null !");
        }

        // 2.serialSensorAssemble
        SerialSensor insertSerialSensor = serialSensor; // 这里简化一下

        // 3.插入数据
        int countRow = serialSensorMapper.updateByPrimaryKeySelective(insertSerialSensor);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("the serialSensor update fail !");
        }

        // 4.返回成功响应
        return ServerResponse.createBySuccessMessage("the serialSensor update success .");
    }

    public ServerResponse insertSerialSensorAndReturnId(SerialSensor serialSensor){
        // 1.数据校验
        if (serialSensor == null){
            return ServerResponse.createByErrorMessage("the serialSensor is null !");
        }

        // 2.插入数据
        SerialSensor insertSerialSensor = serialSensor;
        Integer id = serialSensorMapper.insertSelectiveAndReturnId(insertSerialSensor);
        if (id == null){
            return ServerResponse.createByErrorMessage("serialSensor insert fail !");
        }
        return ServerResponse.createBySuccess(id);
    }

    public ServerResponse dealSerialSensorListFromMQ(List<SerialSensor> serialSensorList){
        // 1.校验数据
        if (serialSensorList == null || serialSensorList.size() == 0){
            return ServerResponse.createByErrorMessage("the serialSensorList is null or the size of the serialSensorList is zero !");
        }

        // 2.剔除已经存在数据库中的数据（虽然这已经在终端服务器中做过了，但这里必须再做一次校验。毕竟数据是以中控室为中心的）
        List<SerialSensor> filterSerialSensorList = this.serialSensorFilterByExistInDB(serialSensorList);
        if (filterSerialSensorList == null || filterSerialSensorList.size() == 0){
            return ServerResponse.createBySuccessMessage("there is no new serialSensor !");
        }

        // 3.根据新增serial数据，调用对应sensorRegisterService中addSensorRegisterListBySerialSensorList()。
        // 并且上述方法会自动处理serialSensorList与对应InitializationInclination等处理（数据库与全局配置）。之所以不在这里处理，一方面是由于这是该方法应该做的，另一方面sensorRegister_id的处理会比较麻烦
        ServerResponse addSensorRegisterResponse = iSensorRegisterService.addSensorRegisterListBySerialSensorList(filterSerialSensorList);
        if (addSensorRegisterResponse.isFail()){
            return addSensorRegisterResponse;
        }

        // 5.返回成功deal的响应
        return ServerResponse.createBySuccessMessage("the serialSensorList uploaded has dealded .");
    }

    private List<SerialSensor> serialSensorFilterByExistInDB(List<SerialSensor> serialSensorList){
        List<SerialSensor> resultSerialSensorList = Lists.newArrayList();
        for (SerialSensor serialSensor : serialSensorList) {
            ServerResponse checkValidResponse = this.checkValidByPort(serialSensor);
            if (checkValidResponse.isFail()){
                // 不存在符合条件的数据，将该数据放入新增列表中
                resultSerialSensorList.add(serialSensor);
            }
        }
        return resultSerialSensorList;
    }

    private ServerResponse checkValidByPort(SerialSensor serialSensor){

        int terminalId = serialSensor.getTerminalId();
        List<SerialSensor> resultSerialSensorList = serialSensorMapper.selectByTerminalIdAndPort(terminalId,serialSensor.getPort());

        if (resultSerialSensorList.size() == 1){
            return ServerResponse.createBySuccessMessage("the serialSensor meeting the condition exist");
        }
        if (resultSerialSensorList.size() > 1){
            // 这里日后可能出现异常，就是查询多个结果出来。也就是一个port有多个address，但是这个异常出现了，也就是提醒该写新的语句了。    // 话说硬件难道就不可以有个标准的注册规范嘛
            return ServerResponse.createBySuccessMessage("the serialSensor meeting the condition exist (more than one)!");
        }
        // 无法获得符合条件的数据
        return ServerResponse.createByErrorMessage("no serial meet the condition !");
    }

    public ServerResponse addSerialSensor(SerialSensor serialSensor){
        // 1.校验数据
        if (serialSensor == null){
            return ServerResponse.createByErrorMessage("the serialSensor is null !");
        }

        // 2.数据组装
        SerialSensor insertSerialSensor = serialSensor;

        // 3.保存serialSensor至数据库
        ServerResponse addSerialSensorResponse = this.insert(insertSerialSensor);
        if (addSerialSensorResponse.isFail()){
            return addSerialSensorResponse;
        }

        // 4.将上述serialSensor发送MQ，更新终端配置
        ServerResponse sendSesnorRegitster2MQResponse = this.sendSensorRegitster2MQ(insertSerialSensor);
        if (sendSesnorRegitster2MQResponse.isFail()){
            return sendSesnorRegitster2MQResponse;
        }

        // 5.返回成功响应
        return ServerResponse.createBySuccessMessage("sensorRegister added .");
    }

    public ServerResponse sendSensorRegitster2MQ(SerialSensor serialSensor){
        // 全局更新相关配置（主要针对目标终端机与本机） // 调用目标Producer方法
        try {
            serialSensorProducer.sendSerialSensor(serialSensor);
        } catch (Exception e) {
            log.error("Exception:{}",e);
            return ServerResponse.createByErrorMessage("Exception: "+e.toString());
        }

        return ServerResponse.createBySuccessMessage("the serialSensor has sended to mq .");
    }
}
