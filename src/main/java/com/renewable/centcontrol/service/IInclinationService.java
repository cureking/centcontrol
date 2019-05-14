package com.renewable.centcontrol.service;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;

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

    ServerResponse<List<Object>> listTotalDataByTime(String startTime, String endTime, int terminalId, int sensorId);

    ServerResponse getTotalDataById(long inclinationTotalId);

}
