package com.renewable.centcontrol.controller.portal;

import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.service.IProjectService;
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
@RequestMapping("/project/")
public class ProjectController {

    @Autowired
    private IProjectService iProjectService;


    @RequestMapping(value = "list_project_page.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listProjectByPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProjectService.listByPage(pageNum, pageSize);
    }

    @RequestMapping(value = "get_project_id.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProjectById(int projectId) {
        return iProjectService.getProject(projectId);
    }

    //增减改，暂时不需要
    //甚至这个接口暂时都不用暴露，因为目前面对的企业，大多只使用一个产品，不过留一个空间
}
