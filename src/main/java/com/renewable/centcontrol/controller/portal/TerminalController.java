package com.renewable.centcontrol.controller.portal;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Project;
import com.renewable.centcontrol.service.ITerminalService;
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
@RequestMapping("/terminal/")
public class TerminalController {

    @Autowired
    private ITerminalService iTerminalService;


    //现在规模还很小，所以列表可以直接返回完整信息，不需要再单独获取。日后数据量增大，保密性提高后，列表返回的数据，可以建立对应VO，来进行展示

    /**
     * 获取项目列表
     * @param pageNum
     * @param pageSize
     * @param projectId
     * @return
     */
    @RequestMapping(value = "list_by_page.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listByPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "projectId", defaultValue = "1") int projectId) {

        return iTerminalService.listByPage(pageNum,pageSize,projectId);
    }

    /**
     * 获取具体项目详情（其实列表目前返回的就是详情）
     * @param terminalId
     * @return
     */
    @RequestMapping(value = "get_detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(int terminalId){
        return iTerminalService.getTerminal(terminalId);
    }
}
