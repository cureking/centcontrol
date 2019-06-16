package com.renewable.centcontrol.service;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Warning;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface IWarningService {

    ServerResponse getWarningById(int warningId);

    ServerResponse listWarningWithPage(int pageNum, int pageSize, Integer terminalId, Integer sensorRegisterId);

    ServerResponse dealWarningFromMQByList(List<Warning> warningList);

    ServerResponse singleWarningDealProcess(Warning warning);

    ServerResponse listWarningSetFromCache();

    // test
    ServerResponse testAddWarning();

}
