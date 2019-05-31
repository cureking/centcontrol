package com.renewable.centcontrol.service.impl;

import com.renewable.centcontrol.common.RedisTemplateUtil;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.common.constant.SensorRegisterConstant;
import com.renewable.centcontrol.common.constant.TerminalConstant;
import com.renewable.centcontrol.dao.WarningMapper;
import com.renewable.centcontrol.pojo.Warning;
import com.renewable.centcontrol.service.ISensorRegisterService;
import com.renewable.centcontrol.service.ITerminalService;
import com.renewable.centcontrol.service.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.renewable.centcontrol.common.constant.CacheConstant.*;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Service("iWarningServiceImpl")
public class IWarningServiceImpl implements IWarningService {

    @Autowired
    private WarningMapper warningMapper;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private ITerminalService iTerminalService;

    @Autowired
    private ISensorRegisterService iSensorRegisteredService;


    @Override
    public ServerResponse getWarningById(int warningId) {

        Warning warning = new Warning();
        warning = warningMapper.selectByPrimaryKey(warningId);
        if (warning == null) {
            return ServerResponse.createByErrorMessage("can't find the warning from DB by the Id: " + warningId);
        }
        return ServerResponse.createBySuccess(warning);
    }

    public ServerResponse dealWarningFromMQByList(List<Warning> warningList) {
        // 1.数据校验
        if (warningList == null || warningList.size() == 0) {
            return ServerResponse.createByErrorMessage("the warning list is null or have no warning !");
        }

        // 2.调用相关方法，来处理其中的每条数据
        for (Warning warning : warningList) {
            ServerResponse singleWarningDealResponse = this.singleWarningDealProcess(warning);
            if (singleWarningDealResponse.isFail()) {
//                return ServerResponse.createByErrorMessage("the warning deal fail ! the warning is : "+warning.toString());   // 不能因为一个数据出现问题，导致整个报警列表GG
                log.warn("the warning deal fail ! the warning is : {}", warning.toString());
            }
        }

        return ServerResponse.createBySuccessMessage("every warning in the warning list has dealed ");
    }

    public ServerResponse singleWarningDealProcess(Warning warning) {
        // 1.数据校验
        if (warning == null) {
            return ServerResponse.createByErrorMessage("the warning is null !");
        }
        // 2.数据上传warning数据表 // 插入时别插入ID，否则会产生Multi Primary Key Define Exception.
        ServerResponse insertResponse = this.insertSingleWarning(warning);
        if (insertResponse.isFail()) {
            return insertResponse;  // 该response已经在底层处理过了，直接返回
        }


        // 2+.修改对应终端，传感器状态
        int terminalId = warning.getTerminalId();
        int targetTerminalStatus = TerminalConstant.TerminalStatusEnum.Alert.getCode();
        ServerResponse updateTerminalStatusResponse = iTerminalService.updateTerminalStatusById(terminalId, targetTerminalStatus);
        if (updateTerminalStatusResponse.isFail()) {
            return updateTerminalStatusResponse;
        }

        int sensorRegisterId = warning.getSensorRegisterId();
        byte targetSensorRegisterStatus = SensorRegisterConstant.SensorRegisterStatusEnum.Alert.getCode();
        ServerResponse updateSensorRegisterStatusResponse = iSensorRegisteredService.updateSensorRegisteredStatusBySensorRegisterId(sensorRegisterId, targetSensorRegisterStatus);
        if (updateSensorRegisterStatusResponse.isFail()) {
            return updateSensorRegisterStatusResponse;
        }

        // 3.根据缓存判断，判断该数据是否需要进行报警
        StringBuilder warningEliminateCacheKey = new StringBuilder(WARNING_ELIMINATE_PREFIX).append(sensorRegisterId);
        String warningEliminateStr = redisTemplateUtil.get(warningEliminateCacheKey.toString());
        if (StringUtils.isBlank(warningEliminateStr)) {  // isBlank更为严格，会判断其中内容是否为Nil
            // 3.1.如果没有相关消警信息，则需要报警，调用相关报警方法
            // 突然在想我的报警方法怎么处理，相关的设备已经做了status了。所以，还是在缓存中维持一个报警表吧。

            // 1.获取warning 在Warning表中的具体对应的ID（其实完全可以就只通过SernsorRegisterId来确定，但为了日后扩展与程序健壮性，还是通过ID吧）
            int lastSensorRegisterId = warning.getSensorRegisterId();
            Date lastWarningCreateTime = warning.getCreateTime();

            ServerResponse getLastWarningIdResponse = this.getLastWarningBySensorRegisterIdAndCreateTime(lastSensorRegisterId, lastWarningCreateTime);
            if (getLastWarningIdResponse.isFail()) {
                return getLastWarningIdResponse;
            }

            int lastWarningId = (int) getLastWarningIdResponse.getData();
            redisTemplateUtil.add(WARNING_SET_KEY, WARNING_PREFIX + lastWarningId);     // 在对应警告SET中写入警告消息  // 当前不提供具体信息，当然想要具体信息，请从数据库中重新获取对应数据。
        }

        // 3.2.如果缓存中存在对应消警信息，就不做处理
        log.info("the warning has dealed ! the warning is {}", warning);
        return ServerResponse.createBySuccessMessage("the warning dealed");
    }

    private ServerResponse insertSingleWarning(Warning warning) {
        // 1.数据校验
        if (warning == null) {
            return ServerResponse.createByErrorMessage("the warning is null !");
        }

        // 2.组装对应Warning
        Warning insertWarning = this.warningInsertAssemble(warning).getData();

        // 3.插入对应数据
        int countRow = warningMapper.insertSelective(insertWarning);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("the warning insert fail ! the detail of the warning: " + insertWarning);
        }

        return ServerResponse.createBySuccessMessage("the warning insert success !");
    }

    private ServerResponse<Warning> warningInsertAssemble(Warning warning) {
        // 1.数据校验
        if (warning == null) {
            return ServerResponse.createByErrorMessage("the warning is null !");
        }
        // 这里现在先简单处理吧
        Warning warningAssembleBo = warning;
        warningAssembleBo.setId(null);
        return ServerResponse.createBySuccess(warningAssembleBo);
    }

    public ServerResponse<Integer> getLastWarningBySensorRegisterIdAndCreateTime(int sensorRegisterId, Date createTime) {

        Integer lastWarningId = warningMapper.getLastWarningBySensorRegisterIdAndLastCreateTime(sensorRegisterId, createTime);
        if (lastWarningId == null) {
            return ServerResponse.createByErrorMessage("get last warning by sensorRegisterId and createTime fail !");
        }

        return ServerResponse.createBySuccess(lastWarningId);
    }

    public ServerResponse listWarningSetFromCache() {

        Set<Object> warningObjectSet = redisTemplateUtil.members(WARNING_SET_KEY);
        if (warningObjectSet == null || warningObjectSet.size() == 0) {
            return ServerResponse.createByErrorMessage("there is no warning !");
        }
        return ServerResponse.createBySuccess(warningObjectSet);
    }
}
