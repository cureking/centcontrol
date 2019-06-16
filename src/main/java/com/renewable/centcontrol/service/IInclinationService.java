package com.renewable.centcontrol.service;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.InclinationInit;
import com.renewable.centcontrol.pojo.InclinationTotal;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface IInclinationService {

    ServerResponse<PageInfo> listInitDataByPage(int pageNum, int pageSize, int terminalId, int sensorId);

    ServerResponse<List<Object>> listInitDataByTime(String startTime, String endTime, int terminalId, int sensorId);

    ServerResponse getInitDataById(long inclinationInitId);

    ServerResponse<PageInfo> listTotalDataByPage(int pageNum, int pageSize, int terminalId, int sensorId);

    ServerResponse<List<Object>> listTotalDataByTime(String startTime, String endTime, Integer terminalId, Integer sensorId);

    ServerResponse getTotalDataById(long inclinationTotalId);

    ServerResponse insertTotalDataByList(List<InclinationTotal> inclinationTotalList);

    ServerResponse insertInitDataByList(List<InclinationInit> inclinationInitList);

}
