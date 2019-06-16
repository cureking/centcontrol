package com.renewable.centcontrol.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
import java.util.List;

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

    @Override
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


    @Override
    public ServerResponse eliminateWarning(int warningId, int userId) {

        // 1.在缓存中，消除警报列表中对应警报记录（如果不存在对应警报，请返回对应错误响应)
        boolean warningExist = redisTemplateUtil.isMember(WARNING_SET_KEY, WARNING_PREFIX + warningId);
        if (!warningExist){
            return ServerResponse.createByErrorMessage("the warning is not exist in cache ! warning id: "+warningId);
        }
        redisTemplateUtil.remove(WARNING_SET_KEY, WARNING_PREFIX + warningId);

        // 2.保存消警记录
        ServerResponse getWarningByIdResponse = iWarningService.getWarningById(warningId);
        if (getWarningByIdResponse.isFail()) {
            return getWarningByIdResponse;
        }
        Warning targetWarning = (Warning) getWarningByIdResponse.getData();
        WarningEliminate warningEliminate = this.warningEliminateAssembleVo(targetWarning, userId);

        ServerResponse insertWarningEliminateResponse = this.insertWarningEliminate(warningEliminate);
        if (insertWarningEliminateResponse.isFail()){
            return insertWarningEliminateResponse;
        }

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

    @Override
    public ServerResponse getWarningEliminateFromPersistenceById(Integer id) {
        if (id == null){
            return ServerResponse.createByErrorMessage("the id is null !");
        }
        WarningEliminate warningEliminate = null;
        warningEliminate = warningEliminateMapper.selectByPrimaryKey(id);
        if (warningEliminate == null){
            return ServerResponse.createByErrorMessage("the warningEliminate with the id is not exist ! id: "+id);
        }
        return ServerResponse.createBySuccess(warningEliminate);
    }

    @Override
    public ServerResponse listWarningEliminateFromPersistenceWithPageHelper(int pageNum, int pageSize, Integer warningId, Integer userId, Integer terminalId) {
        //第一步：startPage--start
        PageHelper.startPage(pageNum, pageSize);

        //第二步：填充自己的sql查询逻辑
        List<WarningEliminate> warningEliminateList = warningEliminateMapper.selectList(warningId, userId, terminalId);
        if (warningEliminateList == null || warningEliminateList.size() == 0) {
            return ServerResponse.createByErrorMessage("not found warningEliminate");
        }

        //这里由于业务简单，而且对数据隐蔽性要求不高，这里先不急着写VO了
        //此处日后可扩展VO
        List<WarningEliminate> warningEliminateVoList = Lists.newArrayList();
        for (WarningEliminate warningEliminateItem : warningEliminateList) {
            WarningEliminate warningEliminateVo = (WarningEliminate) warningEliminateItem;   // 可以建立assembleVo()，而不是强转
            warningEliminateVoList.add(warningEliminateVo);
        }

        //第三步：pageHelper--收尾
        PageInfo pageResult = new PageInfo(warningEliminateList);
        pageResult.setList(warningEliminateVoList);
        return ServerResponse.createBySuccess(pageResult);
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
