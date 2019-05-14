package com.renewable.centcontrol.controller.backend;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.Const;
import com.renewable.centcontrol.common.ResponseCode;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Project;
import com.renewable.centcontrol.pojo.Terminal;
import com.renewable.centcontrol.pojo.User;
import com.renewable.centcontrol.service.ITerminalService;
import com.renewable.centcontrol.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/manage/terminal/")
public class TerminalManageController {


    @Autowired
    private IUserService iUserService;
    @Autowired
    private ITerminalService iTerminalService;


    /**
     * 对终端进行新增
     * @param session
     * @param terminal
     * @return
     */
    @RequestMapping(value = "create_terminal.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse createTerminal(HttpSession session, Terminal terminal) {

        //1.校验用户权限
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //2.填充业务逻辑
            return iTerminalService.createTerminal(terminal);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    /**
     * 对项目进行更新
     * @param session
     * @param terminal
     * @return
     */
    @RequestMapping(value = "update_terminal.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateTerminal(HttpSession session, Terminal terminal) {

        //1.校验用户权限
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务逻辑
            return iTerminalService.updateTerminal(terminal);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 对项目进行更新      //无论是何种情况下，删除操作都是十分危险的。【强制】要求前端进行确认操作
     * @param session
     * @return
     */
    @RequestMapping(value = "delete_terminal.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse deleteTerminal(HttpSession session,int terminalId) {

        //1.校验用户权限
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务逻辑
            return iTerminalService.deleteTerminal(terminalId);             //现在是单用户，故这里不需要传输useId，来避免横向越权问题（数据库中A用户利用B用户的terminalId，来删除了B用户的terminal）。
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
