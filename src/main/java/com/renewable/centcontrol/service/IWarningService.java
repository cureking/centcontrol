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

    ServerResponse dealWarningFromMQByList(List<Warning> warningList);

    // 也许只用于内部使用
    ServerResponse singleWarningDealProcess(Warning warning);

    ServerResponse listWarningSetFromCache();

}
