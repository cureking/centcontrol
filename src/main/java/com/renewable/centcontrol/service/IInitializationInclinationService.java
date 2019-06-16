package com.renewable.centcontrol.service;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.InitializationInclination;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface IInitializationInclinationService {

    ServerResponse getInitializationInclinationById(Integer id);

    ServerResponse getInitializationInclinationBySensorRegisterId(Integer sensorRegisterId);

    ServerResponse listInitializationInclinationByTerminalId(Integer terminalId);

    ServerResponse addInitializationInclination(InitializationInclination initializationInclination);

    ServerResponse updateInitializationInclinationById(InitializationInclination initializationInclination);

    ServerResponse receiveInitializationInclinationListFromMQ(List<InitializationInclination> initializationInclinationList);
}
