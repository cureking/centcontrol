package com.renewable.centcontrol.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.dao.InclinationInitMapper;
import com.renewable.centcontrol.dao.InclinationTotalMapper;
import com.renewable.centcontrol.pojo.InclinationInit;
import com.renewable.centcontrol.pojo.InclinationTotal;
import com.renewable.centcontrol.service.IInclinationService;
import com.renewable.centcontrol.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Service("iInclinationImpl")
public class IInclinationServiceImpl implements IInclinationService {

    @Autowired
    private InclinationInitMapper inclinationInitMapper;

    @Autowired
    private InclinationTotalMapper inclinationTotalMapper;


    @Override
    public ServerResponse<PageInfo> listInitDataByPage(int pageNum, int pageSize, int terminalId, int sensorId) {
        //TODO PageHelper在SpringBoot中无法生效（代码与SpringFramework中的一致）
        //第一步：startPage--start
        PageHelper.startPage(pageNum, pageSize);

        //第二步：填充自己的sql查询逻辑
        List<InclinationInit> inclinationInitList = inclinationInitMapper.selectList(terminalId, sensorId);
        if (inclinationInitList == null) {
            return ServerResponse.createByErrorMessage("not found inclinationInit");
        }

        //这里由于业务简单，而且对数据隐蔽性要求不高，这里先不急着写VO了
        //此处日后可扩展VO
        List<InclinationInit> inclinationInitByTimeVoList = Lists.newArrayList();
        for (InclinationInit inclinationInitItem : inclinationInitList) {
            InclinationInit inclinationInitVo = (InclinationInit) inclinationInitItem;   // 可以建立assembleVo()，而不是强转
            inclinationInitByTimeVoList.add(inclinationInitVo);
        }

        //第三步：pageHelper--收尾
        PageInfo pageResult = new PageInfo(inclinationInitList);
        pageResult.setList(inclinationInitByTimeVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<List<Object>> listInitDataByTime(String startTime, String endTime, int terminalId, int sensorId) {
        List<InclinationInit> inclinationInitList = inclinationInitMapper.selectListByTime(DateTimeUtil.strToDate(startTime), DateTimeUtil.strToDate(endTime), terminalId, sensorId);

        if (inclinationInitList == null) {
            return ServerResponse.createByErrorMessage("not found inclinationInit");
        }

        //此处日后可扩展VO
        List<Object> inclinationInitByTimeVoList = Lists.newArrayList();
        for (InclinationInit inclinationInitItem : inclinationInitList) {
            InclinationInit inclinationInitVo = (InclinationInit) inclinationInitItem;   // 可以建立assembleVo()，而不是强转
            Object InclinationVoObject = (Object) inclinationInitVo;
            inclinationInitByTimeVoList.add(InclinationVoObject);
        }

        return ServerResponse.createBySuccess(inclinationInitByTimeVoList);
    }

    @Override
    public ServerResponse getInitDataById(long inclinationInitId) {
        InclinationInit inclinationInit = inclinationInitMapper.selectByPrimaryKey(inclinationInitId);
        if (inclinationInit == null) {
            return ServerResponse.createByErrorMessage("not found inclinationInit");
        } else {
            return ServerResponse.createBySuccess(inclinationInit);
        }
    }

    @Override
    public ServerResponse<PageInfo> listTotalDataByPage(int pageNum, int pageSize, int terminalId, int sensorId) {
        //TODO PageHelper在SpringBoot中无法生效（代码与SpringFramework中的一致）
        //第一步：startPage--start
        PageHelper.startPage(pageNum, pageSize);

        //第二步：填充自己的sql查询逻辑
        List<InclinationTotal> inclinationTotalList = inclinationTotalMapper.selectList(terminalId, sensorId);
        if (inclinationTotalList == null) {
            return ServerResponse.createByErrorMessage("not found inclinationInit");
        }

        //这里由于业务简单，而且对数据隐蔽性要求不高，这里先不急着写VO了
        //此处日后可扩展VO
        List<InclinationTotal> inclinationTotalByTimeVoList = Lists.newArrayList();
        for (InclinationTotal inclinationTotalItem : inclinationTotalList) {
            InclinationTotal inclinationTotalVo = (InclinationTotal) inclinationTotalItem;   // 可以建立assembleVo()，而不是强转
            inclinationTotalByTimeVoList.add(inclinationTotalVo);
        }

        //第三步：pageHelper--收尾
        PageInfo pageResult = new PageInfo(inclinationTotalList);
        pageResult.setList(inclinationTotalByTimeVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<List<Object>> listTotalDataByTime(String startTime, String endTime, int terminalId, int sensorId) {
        List<InclinationTotal> inclinationTotalList = inclinationTotalMapper.selectListByTime(DateTimeUtil.strToDate(startTime), DateTimeUtil.strToDate(endTime), terminalId, sensorId);

        if (inclinationTotalList == null) {
            return ServerResponse.createByErrorMessage("not found inclinationInit");
        }

        //此处日后可扩展VO
        List<Object> inclinationTotalByTimeVoList = Lists.newArrayList();
        for (InclinationTotal inclinationTotalItem : inclinationTotalList) {
            InclinationTotal inclinationTotalVo = (InclinationTotal) inclinationTotalItem;   // 可以建立assembleVo()，而不是强转
            Object InclinationVoObject = (Object) inclinationTotalVo;
            inclinationTotalByTimeVoList.add(InclinationVoObject);
        }

        return ServerResponse.createBySuccess(inclinationTotalByTimeVoList);
    }

    @Override
    public ServerResponse getTotalDataById(long inclinationTotalId) {
        InclinationTotal inclinationTotal = inclinationTotalMapper.selectByPrimaryKey(inclinationTotalId);
        if (inclinationTotal == null) {
            return ServerResponse.createByErrorMessage("not found inclinationInit");
        } else {
            return ServerResponse.createBySuccess(inclinationTotal);
        }
    }

    @Override
    public ServerResponse insertTotalDataByList(List<InclinationTotal> inclinationTotalList) {
        if (inclinationTotalList == null) {
            return ServerResponse.createByErrorMessage("the inclinationTotalList is null !");
        }

        int countRow = inclinationTotalMapper.insertList(inclinationTotalList);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("inclinationTotalList insert fail !");
        }

        return ServerResponse.createBySuccessMessage("inclinaitonTotalList insert success !");
    }

    @Override
    public ServerResponse insertInitDataByList(List<InclinationInit> inclinationInitList) {
        if (inclinationInitList == null) {
            return ServerResponse.createByErrorMessage("the inclinationTotalList is null !");
        }

        int countRow = inclinationInitMapper.insertList(inclinationInitList);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("inclinationTotalList insert fail !");
        }

        return ServerResponse.createBySuccessMessage("inclinaitonTotalList insert success !");
    }


}
