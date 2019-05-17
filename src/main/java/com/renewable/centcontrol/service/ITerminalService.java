package com.renewable.centcontrol.service;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Terminal;

/**
 * @Description：
 * @Author: jarry
 */
public interface ITerminalService {

    ServerResponse<PageInfo> listByPage(int pageNum,int pageSize,int projectId);

    ServerResponse getTerminal(int terminalId);

    ServerResponse createTerminal(Terminal terminal);

    ServerResponse updateTerminal(Terminal terminal);

    ServerResponse deleteTerminal(int terminalId);

    ServerResponse getTerminalFromRabbitmq(Terminal terminal);

}
