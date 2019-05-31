package com.renewable.centcontrol.controller.portal;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.IWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description：
 * @Author: jarry
 */
@Controller
@RequestMapping("/warning/")
public class WarningController {

    @Autowired
    private IWarningService iWarningService;

    @RequestMapping(value = "list_warningset_from_cache.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listWarningSetFromCache() {
        return iWarningService.listWarningSetFromCache();
    }
}
