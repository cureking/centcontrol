package com.renewable.centcontrol.service;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Project;
import com.renewable.centcontrol.pojo.Terminal;

/**
 * @Description：
 * @Author: jarry
 */
public interface IProjectService {

    ServerResponse<PageInfo> listByPage(int pageNum, int pageSize);

    ServerResponse getProject(int projectId);

    ServerResponse createProject(Project project);

    ServerResponse updateProject(Project project);

    ServerResponse deleteProject(int projectId);

}
