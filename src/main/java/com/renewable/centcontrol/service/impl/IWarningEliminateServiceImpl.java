package com.renewable.centcontrol.service.impl;

import com.renewable.centcontrol.common.RedisTemplateUtil;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.common.constant.SensorRegisterConstant;
import com.renewable.centcontrol.common.constant.WarningEliminateConstant;
import com.renewable.centcontrol.dao.WarningEliminateMapper;
import com.renewable.centcontrol.pojo.Warning;
import com.renewable.centcontrol.pojo.WarningEliminate;
import com.renewable.centcontrol.service.ISensorRegisterService;
import com.renewable.centcontrol.service.ITerminalService;
import com.renewable.centcontrol.service.IWarningEliminateService;
import com.renewable.centcontrol.service.IWarningService;
import com.renewable.centcontrol.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.renewable.centcontrol.common.constant.CacheConstant.*;
import static com.renewable.centcontrol.common.constant.TerminalConstant.TerminalStatusEnum.AlertEliminate;
import static com.renewable.centcontrol.common.constant.WarningEliminateConstant.WARNING_ELIMINATE_VALIDITY;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Service("iWarningEliminateServiceImpl")
public class IWarningEliminateServiceImpl implements IWarningEliminateService {

    @Autowired
    private IWarningService iWarningService;

    @Autowired
    private ITerminalService iTerminalService;

    @Autowired
    private ISensorRegisterService iSensorRegisterService;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private WarningEliminateMapper warningEliminateMapper;

    public ServerResponse insertWarningEliminate(WarningEliminate warningEliminate) {
        if (warningEliminate == null) {
            return ServerResponse.createByErrorMessage("the warningEliminate is null !");
        }
        WarningEliminate insertWarningEliminate = new WarningEliminate();
        insertWarningEliminate = warningEliminate;

        int countRow = warningEliminateMapper.insertSelective(warningEliminate);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("the warningEliminate insert fail !");
        }
        return ServerResponse.createBySuccessMessage("the warningEliminate insert success ");
    }


    public ServerResponse eliminateWarning(int warningId, int userId) {
        // 1.保存消警记录
        ServerResponse getWarningByIdResponse = iWarningService.getWarningById(warningId);
        if (getWarningByIdResponse.isFail()) {
            return getWarningByIdResponse;
        }
        Warning targetWarning = (Warning) getWarningByIdResponse.getData();
        WarningEliminate warningEliminate = this.warningEliminateAssembleVo(targetWarning, userId);

        ServerResponse insertWarningEliminateResponse = this.insertWarningEliminate(warningEliminate);
        // 2.在缓存中，消除警报列表中对应警报记录
        redisTemplateUtil.remove(WARNING_SET_KEY, WARNING_PREFIX + warningId);

        // 3.更新对应终端状态至“消警”状态    // 这里我纠结了一下到底是设置正常运行呢，还是警报消除。后来决定是后者，至于前者可以通过DB的定时任务，找寻警报消除状态保持一定时限以上的设备，不过不急吧。也许以后会增加工作流，来进行确认
        ServerResponse updateTerminalStatusReponse = iTerminalService.updateTerminalStatusById(targetWarning.getTerminalId(), AlertEliminate.getCode());
        if (updateTerminalStatusReponse.isFail()) {
            return ServerResponse.createByErrorMessage("the status of the terminal update fail !");
        }

        // 4.更新对应SensorRegister状态至“消警”状态
        int targetSensorRegisterId = targetWarning.getSensorRegisterId();
        ServerResponse updateSensorRegisterStatusResponse = iSensorRegisterService.updateSensorRegisteredStatusBySensorRegisterId(targetSensorRegisterId, SensorRegisterConstant.SensorRegisterStatusEnum.AlertEliminate.getCode());
        if (updateTerminalStatusReponse.isFail()) {
            return ServerResponse.createByErrorMessage("the status of the sensorRegister update fail !");
        }
        // 5.在缓存中，生成对应消警记录，确保在一定实现内新警报不会报警
        StringBuilder warningEliminateCacheKey = new StringBuilder(WARNING_ELIMINATE_PREFIX).append(targetSensorRegisterId);
        String warningEliminateCacheValue = JsonUtil.obj2String(warningEliminate);
        redisTemplateUtil.setForTimeMS(warningEliminateCacheKey.toString(), warningEliminateCacheValue, WARNING_ELIMINATE_VALIDITY);

        // 6.返回完成响应
        log.warn("the warning has eliminated :{}");
        return ServerResponse.createBySuccessMessage("the warning has eliminated .");
    }

    private WarningEliminate warningEliminateAssembleVo(Warning warning, int userId) {
        WarningEliminate warningEliminate = new WarningEliminate();
        warningEliminate.setWarningId(warning.getId());
        warningEliminate.setUserId(userId);
        warningEliminate.setEliminateWay(WarningEliminateConstant.EliminateWayEnum.Web_Page.getCode());
        warningEliminate.setMark("TEST_2");     // prod阶段可以取消
        warningEliminate.setTerminalId(warning.getTerminalId());
        warningEliminate.setSensorType(warning.getSensorType());
        warningEliminate.setCreateTime(new Date());
        warningEliminate.setUpdateTime(new Date());
        return warningEliminate;
    }
}
