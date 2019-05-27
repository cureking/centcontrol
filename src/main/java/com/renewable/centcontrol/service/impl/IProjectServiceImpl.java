package com.renewable.centcontrol.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.renewable.centcontrol.common.Const;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.dao.ProjectMapper;
import com.renewable.centcontrol.pojo.Project;
import com.renewable.centcontrol.service.IProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Service("iProjectImpl")
public class IProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectMapper projectMapper;


    @Override
    public ServerResponse<PageInfo> listByPage(int pageNum, int pageSize) {
        //TODO PageHelper在SpringBoot中无法生效（代码与SpringFramework中的一致）
        //第一步：startPage--start
        PageHelper.startPage(pageNum, pageSize);

        //第二步：填充自己的sql查询逻辑
        List<Project> inclinationTotalList = projectMapper.selectList();
        if (inclinationTotalList == null){
            return ServerResponse.createByErrorMessage("not found inclinationInit");
        }

        //这里由于业务简单，而且对数据隐蔽性要求不高，这里先不急着写VO了
        //此处日后可扩展VO
        List<Project> projectByTimeVoList = Lists.newArrayList();
        for (Project inclinationTotalItem : inclinationTotalList) {
            Project inclinationTotalVo = (Project)inclinationTotalItem;   // 可以建立assembleVo()，而不是强转
            projectByTimeVoList.add(inclinationTotalVo);
        }

        //第三步：pageHelper--收尾
        PageInfo pageResult = new PageInfo(inclinationTotalList);
        pageResult.setList(projectByTimeVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse getProject(int projectId) {
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (project == null){
            return ServerResponse.createByErrorMessage("not found the project !");
        }
        return ServerResponse.createBySuccess(project);
    }

    @Override
    public ServerResponse createProject(Project project) {
        if (project == null){
            return ServerResponse.createByErrorMessage("the project is null");
        }

        // 2.新建insertProject
        Project insertProject = project;    // 其实这里应该通过相关BO的Assemble()进行正向处理，这里暂时简化，日后完善
        project.setId(null);    // 避免DuplicateKeyException
        // 其实这里还需要进行重复性检测。不过现在就一个项目，所以暂时这样吧。

        int countRow = projectMapper.insertSelective(project);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("insert project fail !");
        }
        return ServerResponse.createBySuccessMessage("insert project success !");
    }

    @Override
    public ServerResponse updateProject(Project project) {
        if (project == null){
            return ServerResponse.createByErrorMessage("the project is null");
        }
        if (project.getId() == null){
            return ServerResponse.createByErrorMessage("the id of project is nil !");
        }

        int countRow = projectMapper.updateByPrimaryKeySelective(project);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("update project fail !");
        }
        return ServerResponse.createBySuccessMessage("update project success !");
    }

    @Override
    public ServerResponse deleteProject(int projectId) {
        int countRow = projectMapper.deleteByPrimaryKey(projectId);
        if (countRow == 0){
            return ServerResponse.createByErrorMessage("delete project fail !");
        }
        return ServerResponse.createBySuccessMessage("delete project success !");
    }
}
