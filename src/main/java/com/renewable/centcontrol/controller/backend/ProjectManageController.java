package com.renewable.centcontrol.controller.backend;

import com.renewable.centcontrol.common.Const;
import com.renewable.centcontrol.common.ResponseCode;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Project;
import com.renewable.centcontrol.pojo.User;
import com.renewable.centcontrol.service.IProjectService;
import com.renewable.centcontrol.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/manage/project/")
public class ProjectManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProjectService iProjectService;


    @RequestMapping("create_project.do")
    @ResponseBody
    public ServerResponse createProject(HttpSession session, Project project){
        //1.校验用户权限  // 已交由拦截器处理

        //2.填充业务逻辑
        return iProjectService.createProject(project);
    }

    @RequestMapping("update_project_id.do")
    @ResponseBody
    public ServerResponse updateProject(HttpSession session, Project project){
        //1.校验用户权限  // 已交由拦截器处理

        //2.填充业务逻辑
        return iProjectService.updateProject(project);
    }

    @RequestMapping("delete_project_id.do")
    @ResponseBody
    public ServerResponse deleteProjectById(HttpSession session, int projectId){
        //1.校验用户权限  // 已交由拦截器处理

        //2.填充业务逻辑
        return iProjectService.deleteProject(projectId);
    }
}
