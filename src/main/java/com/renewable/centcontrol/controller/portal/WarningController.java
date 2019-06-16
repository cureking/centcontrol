package com.renewable.centcontrol.controller.portal;

import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.IWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "get_warning_by_id.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getWarningById(@RequestParam int warningId) {
        return iWarningService.getWarningById(warningId);
    }

    /**
     * 从数据库获取warning列表，这里四个参数全部是可选的（前两个参数通过SPringMVC实现缺省值，后两个参数通过Mybatis实现if，是否添加该参数。
     * @param pageNum
     * @param pageSize
     * @param terminalId
     * @param sensorRegisterId
     * @return
     */
    @RequestMapping(value = "list_warning_with_page")
    @ResponseBody
    public ServerResponse listWarningWithPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "terminalId", defaultValue = "") Integer terminalId,    // 这就是为什么阿里手册要求封装数据类型的原因
                                              @RequestParam(value = "sensorRegisterId", defaultValue = "") Integer sensorRegisterId
    ){
        return iWarningService.listWarningWithPage(pageNum, pageSize, terminalId, sensorRegisterId);
    }

    // warning不给删除权限

    @RequestMapping(value = "list_warningset_from_cache.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listWarningSetFromCache() {
        return iWarningService.listWarningSetFromCache();
    }

    @RequestMapping(value = "test_add_warning.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse testAddWarning(){
        return iWarningService.testAddWarning();
    }
}
