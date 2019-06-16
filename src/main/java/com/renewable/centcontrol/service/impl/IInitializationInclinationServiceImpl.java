package com.renewable.centcontrol.service.impl;

import com.google.common.collect.Lists;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.common.constant.InitializationConstant;
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

    @Override
    public ServerResponse getInitializationInclinationById(Integer id) {
        if (id == null){
            return ServerResponse.createByErrorMessage("the id is null !");
        }
        InitializationInclination initializationInclination = null;
        initializationInclination = initializationInclinationMapper.selectByPrimaryKey(id);
        if (initializationInclination == null){
            return ServerResponse.createByErrorMessage("can't find the initializationInclination with id:" + id);
        }
        return ServerResponse.createBySuccess(initializationInclination);
    }

    @Override
    public ServerResponse getInitializationInclinationBySensorRegisterId(Integer sensorRegisterId) {
        if (sensorRegisterId == null){
            return ServerResponse.createByErrorMessage("the sensorRegisterId is null !");
        }
        InitializationInclination initializationInclination = null;
        initializationInclination = initializationInclinationMapper.selectBySensorRegisterId(sensorRegisterId);
        // 日后这里可以通过list接收，判断list.size()，来判断是否存在数据库异常    // 不过这个概率比较低，可以到发生了再处理（除非是直接修改数据库的）

        if (initializationInclination == null){
            return ServerResponse.createByErrorMessage("can't find the initializationInclination with sensorRegister id:" + sensorRegisterId);
        }
        return ServerResponse.createBySuccess(initializationInclination);
    }

    @Override
    public ServerResponse listInitializationInclinationByTerminalId(Integer terminalId) {
        if (terminalId == null){
            return ServerResponse.createByErrorMessage("the terminalId is null !");
        }
        List<InitializationInclination> initializationInclinationList = null;
        initializationInclinationList = initializationInclinationMapper.listByTerminalId(terminalId);
        if (initializationInclinationList == null || initializationInclinationList.size() == 0){
            return ServerResponse.createByErrorMessage("can't find the initializationInclination with terminal id:" + terminalId);
        }
        return ServerResponse.createBySuccess(initializationInclinationList);
    }

    @Override
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

    @Override
    public ServerResponse updateInitializationInclinationById(InitializationInclination initializationInclination) {
        if (initializationInclination == null){
            return ServerResponse.createByErrorMessage("the initializationInclination is null !");
        }
        Integer updateId = initializationInclination.getId();
        if (updateId == null){
            return ServerResponse.createByErrorMessage("the id of the initializationInclination is null !");
        }
        InitializationInclination existedInitializationInclination = null;
        existedInitializationInclination = initializationInclinationMapper.selectByPrimaryKey(updateId);
        if (existedInitializationInclination == null) {
            return ServerResponse.createByErrorMessage("the initializationInclination you want to update is not existed !");
        }

        InitializationInclination initializationInclinationUpdate = this.InitializationInclinationAssemble(initializationInclination);
        int countRow = initializationInclinationMapper.updateByPrimaryKeySelective(initializationInclinationUpdate);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("the initializationInclination update fail ! the initializationInclination is "+initializationInclination.toString());
        }
        return ServerResponse.createBySuccessMessage("the initializationInclination update success .");
    }

    @Override
    public ServerResponse receiveInitializationInclinationListFromMQ(List<InitializationInclination> initializationInclinationList) {
        // 1.校验
        if (initializationInclinationList == null || initializationInclinationList.size() == 0){
            return ServerResponse.createByErrorMessage("the initializationInclination list is null or its size is zero !");
        }

        // 2.判断列表中的数据是哪些是新增插入，哪些已有更新。返回两个列表，详见serialSensor相关操作
        // 目前都是更新（初始数据记录的新增是自动注册那条线的）
        List<InitializationInclination> filterUpdateInitializationInclinationList = initializationInclinationList;

        // 3.对拆分出来的数据列表进行相关操作
        // 3.1对更新列表中的数据进行更新  // 只有在更新列表不为空才进行下列操作
        if (filterUpdateInitializationInclinationList.size() != 0){
            int countRow = initializationInclinationMapper.updateBatch(filterUpdateInitializationInclinationList);
            if (countRow == 0){
                return ServerResponse.createByErrorMessage("filterUpdateInitializationInclinationList update fail !");
            }
        }

        return ServerResponse.createBySuccessMessage("filterUpdateInitializationInclinationList update success .");
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

    /**
     * 根据InitializationInclination其他字段，判断status应该如何设置
     */
    private InitializationInclination InitializationInclinationAssemble(InitializationInclination initializationInclination){
        InitializationInclination initializationInclinationAssemble = new InitializationInclination();
        initializationInclinationAssemble.setId(initializationInclination.getId());
        initializationInclinationAssemble.setSensorRegisterId(initializationInclination.getSensorRegisterId());
        initializationInclinationAssemble.setTerminalId(initializationInclination.getTerminalId());

        initializationInclinationAssemble.setRadius(initializationInclination.getRadius());
        initializationInclinationAssemble.setInitH1(initializationInclination.getInitH1());
        initializationInclinationAssemble.setInitAngle1(initializationInclination.getInitAngle1());
        initializationInclinationAssemble.setInitH2(initializationInclination.getInitH2());
        initializationInclinationAssemble.setInitAngle2(initializationInclination.getInitAngle2());
        initializationInclinationAssemble.setInitH3(initializationInclination.getInitH3());
        initializationInclinationAssemble.setInitAngle3(initializationInclination.getInitAngle3());
        initializationInclinationAssemble.setInitH4(initializationInclination.getInitH4());
        initializationInclinationAssemble.setInitAngle4(initializationInclination.getInitAngle4());

        initializationInclinationAssemble.setInitTotalAngle(initializationInclination.getInitTotalAngle());
        initializationInclinationAssemble.setInitDirectAngle(initializationInclination.getInitDirectAngle());
        initializationInclinationAssemble.setTotalAngleLimit(initializationInclination.getTotalAngleLimit());
        initializationInclinationAssemble.setTotalInitAngleLimit(initializationInclination.getTotalInitAngleLimit());
        initializationInclinationAssemble.setInitX(initializationInclination.getInitX());
        initializationInclinationAssemble.setInitY(initializationInclination.getInitY());

        initializationInclinationAssemble.setStatus(InitializationConstant.InitializationStatusEnum.USE.getCode());
        if (initializationInclination.getRadius() == null ||
                initializationInclination.getInitH1() == null || initializationInclination.getInitAngle1() == null || initializationInclination.getInitH2() == null || initializationInclination.getInitAngle2() == null ||
                initializationInclination.getInitH3() == null || initializationInclination.getInitAngle3() == null || initializationInclination.getInitH4() == null || initializationInclination.getInitAngle4() == null ||
                initializationInclination.getInitTotalAngle() == null || initializationInclination.getInitDirectAngle() == null || initializationInclination.getTotalAngleLimit() == null || initializationInclination.getTotalInitAngleLimit() == null ||
                initializationInclination.getInitX() == null || initializationInclination.getInitY() == null){
            initializationInclinationAssemble.setStatus(InitializationConstant.InitializationStatusEnum.NEED_COMPLETE.getCode());
        }

        initializationInclinationAssemble.setCreateTime(new Date());
        initializationInclinationAssemble.setUpdateTime(new Date());

        return initializationInclinationAssemble;
    }

}
