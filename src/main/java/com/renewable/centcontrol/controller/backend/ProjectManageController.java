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
        //1.校验用户权限
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //2.填充业务逻辑
            return iProjectService.createProject(project);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("update_project_id.do")
    @ResponseBody
    public ServerResponse updateProject(HttpSession session, Project project){
        //1.校验用户权限
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //2.填充业务逻辑
            return iProjectService.updateProject(project);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("delete_project_id.do")
    @ResponseBody
    public ServerResponse deleteProjectById(HttpSession session, int projectId){
        //1.校验用户权限
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //2.填充业务逻辑
            return iProjectService.deleteProject(projectId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
