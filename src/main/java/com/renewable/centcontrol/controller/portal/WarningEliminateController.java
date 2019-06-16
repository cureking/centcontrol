package com.renewable.centcontrol.controller.portal;

import com.renewable.centcontrol.common.RedisTemplateUtil;
import com.renewable.centcontrol.common.ResponseCode;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.User;
import com.renewable.centcontrol.service.IWarningEliminateService;
import com.renewable.centcontrol.util.CookieUtil;
import com.renewable.centcontrol.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/warning_eliminate/")
public class WarningEliminateController {

    @Autowired
    private IWarningEliminateService iWarningEliminateService;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @RequestMapping("eliminate_warning.do")
    @ResponseBody
    public ServerResponse eliminateWarning(HttpServletRequest httpServletRequest, int warningId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        String userJsonStr = redisTemplateUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        // 前面已经经过拦截器了。应该可以直接获取，或者我应该在拦截器部分，将user放入request中

        return iWarningEliminateService.eliminateWarning(warningId, user.getId());
    }

    @RequestMapping("get_warning_eliminate_from_persistency_by_id.do")
    @ResponseBody
    public ServerResponse getWarningEliminateFromPersistenceById( Integer id) {
        return iWarningEliminateService.getWarningEliminateFromPersistenceById(id);
    }

    @RequestMapping("list_warning_eliminate_from_persistency_with_page_helper.do")
    @ResponseBody
    public ServerResponse listWarningEliminateFromPersistenceWithPageHelper(HttpServletRequest httpServletRequest,
                                                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                            @RequestParam(value = "warningId", defaultValue = "") Integer warningId,
                                                                            @RequestParam(value = "userId", defaultValue = "") Integer userId,
                                                                            @RequestParam(value = "terminalId", defaultValue = "") Integer terminalId) {
//        // 这边权限规则改了，还没有来得及添加拦截器。先简单验证一下吧
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
//        }
//        String userJsonStr = redisTemplateUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr,User.class);
//
//        if(user ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
//        }

        return iWarningEliminateService.listWarningEliminateFromPersistenceWithPageHelper(pageNum, pageSize, warningId, userId, terminalId);
    }
}
