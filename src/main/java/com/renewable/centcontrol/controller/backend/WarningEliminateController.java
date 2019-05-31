package com.renewable.centcontrol.controller.backend;

import com.renewable.centcontrol.common.RedisTemplateUtil;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.User;
import com.renewable.centcontrol.service.IWarningEliminateService;
import com.renewable.centcontrol.util.CookieUtil;
import com.renewable.centcontrol.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/manage/warning_eliminate/")
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

        return iWarningEliminateService.eliminateWarning(warningId, user.getId());
    }
}
