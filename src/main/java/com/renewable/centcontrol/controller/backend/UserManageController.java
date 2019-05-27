package com.renewable.centcontrol.controller.backend;

import com.renewable.centcontrol.common.Const;
import com.renewable.centcontrol.common.RedisTemplateUtil;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.common.constant.RedisConstant;
import com.renewable.centcontrol.pojo.User;
import com.renewable.centcontrol.service.IUserService;
import com.renewable.centcontrol.util.CookieUtil;
import com.renewable.centcontrol.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description：
 * @Author: jarry
 * @Date: 12/21/2018 14:43
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){

        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
//                session.setAttribute(Const.CURRENT_USER,user);

                //新增redis共享cookie，session的方式
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                redisTemplateUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), RedisConstant.RedisCacheExtime.REDIS_SESSION_EXTIME);

                User userTest = JsonUtil.string2Obj(redisTemplateUtil.get(session.getId()),User.class);
                return response;
            }else{
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }
}
