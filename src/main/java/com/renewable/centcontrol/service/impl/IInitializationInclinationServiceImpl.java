package com.renewable.centcontrol.service.impl;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.dao.InitializationInclinationMapper;
import com.renewable.centcontrol.pojo.InitializationInclination;
import com.renewable.centcontrol.rabbitmq.producer.InitializationInclinationProducer;
import com.renewable.centcontrol.service.IInitializationInclinationService;
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
@Service("iInitializationInclinationServiceImpl")
public class IInitializationInclinationServiceImpl implements IInitializationInclinationService {

    @Autowired
    private InitializationInclinationMapper initializationInclinationMapper;

    @Autowired
    private InitializationInclinationProducer initializationInclinationProducer;

    public ServerResponse insertInitializationInclination(InitializationInclination initializationInclination){
        // 1.校验数据
        if (initializationInclination == null){
            return ServerResponse.createByErrorMessage("the initializationInclination is null !");
        }

        // 2.装载数据
        InitializationInclination initializationInclinationAdded =  initializationInclination;       // 这里简化处理

        // 3.将数据保存至数据库
        int countRow = initializationInclinationMapper.insertSelective(initializationInclinationAdded);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("the initializationInclinationAdded insert fail !");
        }

        // 5.返回成功响应
        return ServerResponse.createBySuccessMessage("initializationInclinationAdded has inserted .");
    }

    public ServerResponse addInitializationInclination(InitializationInclination initializationInclination){
        // 1.数据校验
        if (initializationInclination == null){
            return ServerResponse.createByErrorMessage("initializationInclination is null !");
        }

        // 2.数据组装
        InitializationInclination insertInitializationInclination = initializationInclination;

        // 3.保存sensorRegister至数据库
        ServerResponse addInitializationInclinationResponse = this.insertInitializationInclination(insertInitializationInclination);
        if (addInitializationInclinationResponse.isFail()){
            return addInitializationInclinationResponse;
        }

        // 4.将上述sensorRegister发送MQ，更新终端配置
        ServerResponse sendInitializationInclination2MQResponse = this.sendInitializationInclination2MQ(insertInitializationInclination);
        if (sendInitializationInclination2MQResponse.isFail()){
            return sendInitializationInclination2MQResponse;
        }

        // 5.返回成功响应
        return ServerResponse.createBySuccessMessage("initializationInclination added .");
    }

    private ServerResponse sendInitializationInclination2MQ(InitializationInclination initializationInclination){
        // 4.全局更新相关配置（主要针对目标终端机与本机） // 调用目标Producer方法
        try {
            initializationInclinationProducer.sendInitializationInclination(initializationInclination);
        } catch (Exception e) {
            log.error("Exception:{}",e);
            return ServerResponse.createByErrorMessage("Exception: "+e.toString());
        }

        return ServerResponse.createBySuccessMessage("the initializationInclination has sended to mq .");
    }
}
