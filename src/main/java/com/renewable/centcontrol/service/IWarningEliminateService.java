package com.renewable.centcontrol.service;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.WarningEliminate;

/**
 * @Description：
 * @Author: jarry
 */
public interface IWarningEliminateService {

    ServerResponse insertWarningEliminate(WarningEliminate warningEliminate);

    ServerResponse eliminateWarning(int warningId, int userId);
}
