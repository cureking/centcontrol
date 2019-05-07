package com.renewable.centcontrol.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.dao.TerminalMapper;
import com.renewable.centcontrol.pojo.Terminal;
import com.renewable.centcontrol.service.IInclinationService;
import com.renewable.centcontrol.service.ITerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Service("iTerminalImpl")
public class ITerminalServiceImpl implements ITerminalService {

    @Autowired
    private TerminalMapper terminalMapper;


    @Override
    public ServerResponse<PageInfo> getList(int pageNum, int pageSize, int productId){

        //pagehelper 使用逻辑:  第一步，startPage--start；第二步，填充自己的sql查询逻辑；第三步，pageHelper--收尾
        //第一步：startPage--start
        PageHelper.startPage(pageNum,pageSize);

        //test
        Terminal x =terminalMapper.selectByPrimaryKey(1);

        //填充SQL查询逻辑
        List<Terminal> terminalList = terminalMapper.selectList(productId);  //这里由于是内网，而且只是查询信息，所以并没有进行验证等安全操作。
        if (terminalList == null) {
            return ServerResponse.createByErrorMessage("no data conform your requirement!");
        }
        //日后需要，这里可以将数据清洗为对应VO，再返回

        //第三步：pageHelper--收尾
        PageInfo pageResult = new PageInfo(terminalList);
        pageResult.setList(terminalList);
        return ServerResponse.createBySuccess(pageResult);
    }

}
