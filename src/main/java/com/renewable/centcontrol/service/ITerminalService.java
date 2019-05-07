package com.renewable.centcontrol.service;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;

/**
 * @Description：
 * @Author: jarry
 */
public interface ITerminalService {

    ServerResponse<PageInfo> getList(int pageNum,int pageSize,int productId);
}
