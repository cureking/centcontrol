package com.renewable.centcontrol.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.renewable.centcontrol.common.Const;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.dao.TerminalMapper;
import com.renewable.centcontrol.pojo.Terminal;
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
    public ServerResponse createTerminal(Terminal terminal) {
        //这里的新建服务器一方面面向控制层，另一方面面向自动注册功能（还没写）。前期可以只通过控制层录入。切记，这里是服务层，服务层就是提供无状态的基础服务或业务服务，应用冲突请放在服务层之上（或高层服务，而不是这种进行新增的基础服务）。
        //不过由于规模较小，所以实际这里的服务是业务服务。

        if (terminal == null) {
            return ServerResponse.createByErrorMessage("null parameter");
        }

        Integer insertTerminalId = terminal.getId();
        String insertTerminalIp = terminal.getIp();

        //判断即将插入的数据在数据库中是否已经存在（包括ID冲突，IP冲突，MAC冲突等）  // 其实在有冲突ID时，访问update操作，无冲突ID时，进行insert操作。
        ServerResponse responseAboutClash = this.countClashed(insertTerminalId,insertTerminalIp);
        if (!responseAboutClash.isSuccess()) {
            return responseAboutClash;
        }

        Terminal createdTerminal = new Terminal();
        createdTerminal = terminal; // 日后需要，这里可以进行装载，这里就简单写了

        int countRow = terminalMapper.insertSelective(createdTerminal);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("terminal insert fail !");
        } else {
            return ServerResponse.createBySuccessMessage("terminal insert success !");
        }
    }

    public ServerResponse getTerminal(int terminalId) {
        Terminal terminal = terminalMapper.selectByPrimaryKey(terminalId);
        if (terminal == null) {
            return ServerResponse.createByErrorMessage("no data conform your requirement!");
        } else {
            return ServerResponse.createBySuccess(terminal);
        }
    }

    @Override
    public ServerResponse<PageInfo> listByPage(int pageNum, int pageSize, int projectId) {

        //pagehelper 使用逻辑:  第一步，startPage--start；第二步，填充自己的sql查询逻辑；第三步，pageHelper--收尾
        //第一步：startPage--start
        PageHelper.startPage(pageNum, pageSize);

        //填充SQL查询逻辑
        List<Terminal> terminalList = terminalMapper.selectList(projectId);  //这里由于是内网，而且只是查询信息，所以并没有进行验证等安全操作。
        if (terminalList == null) {
            return ServerResponse.createByErrorMessage("no data conform your requirement!");
        }
        //日后需要，这里可以将数据清洗为对应VO，再返回

        //第三步：pageHelper--收尾
        PageInfo pageResult = new PageInfo(terminalList);
        pageResult.setList(terminalList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse updateTerminal(Terminal terminal) {
        //切记，由于终端服务器的特点（可以通过ID或者IP或者MAC来唯一标示），故注意更新时的数据处理（一方面可以通过三者唯一标定，另一方面避免同一IP在多个ID的终端服务器记录中出现。）(终端服务器自己维持一个全局唯一ID）
        //其实分布式全局ID是一个比较复杂，重要的分布式问题（什么问题涉及真正的分布式，高并发后都会比较复杂）。常见解决方案有UUID，Snowflake，Flicker，Redis，Leaf等。
        //但是这里只通过ID，进行更新。其他的之后再扩展

        if (terminal == null) {
            return ServerResponse.createByErrorMessage("submitted terminal is null !");
        }

        // 验证要更新的terminal的id是否存在
//        ServerResponse responseClash = this.countClashed();       //TODO
        if (terminal.getId() == null){
            return ServerResponse.createByErrorMessage("submitted terminal is no id !");
        }

        Terminal updateTerminal = new Terminal();
        updateTerminal = terminal;
        int countRow = terminalMapper.updateByPrimaryKeySelective(updateTerminal);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("update terminal fail !");
        } else {
            return ServerResponse.createBySuccessMessage("update terminal success !");
        }
    }

    @Override
    public ServerResponse deleteTerminal(int terminalId) {
        // 其实，删除就是更新状态

        Terminal terminal = terminalMapper.selectByPrimaryKey(terminalId);
        if (terminal == null) {
            return ServerResponse.createByErrorMessage("该终端服务器不存在");
        }
        //进行终端处理器删除         //TODO_finished 其实我认为更理想的一种实现状态，是不进行删除，而是对终端服务器进行一个状态的管理：“运行中”，“删除”，“故障中”等等。这样便于管理（会有一个历史状态，这样也许有利于之后的警告处理（毕竟警告在中控室的概览中是面向工控机（终端服务器））。我再考虑考虑吧。
//        int rowCount = terminalMapper.deleteByPrimaryKey(terminalId);
//        if (rowCount > 0){
//            return ServerResponse.createBySuccess();
//        } else{
//        return ServerResponse.createByError();
//        }
        ServerResponse response = this.changeState(terminalId, Const.TerminalStateEnum.Deleted.getCode());
        return response;
    }

    private ServerResponse changeState(int id, int targetStateCode) {
        // 其实，更换状态就是更新状态

        Terminal targetTerminal = new Terminal();
        targetTerminal.setId(id);
        targetTerminal.setState(targetStateCode);
        //更新update_time
        int countRow = terminalMapper.updateByPrimaryKeySelective(targetTerminal);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("state change fail !");
        } else {
            return ServerResponse.createBySuccessMessage("state change success !");
        }
    }

    private ServerResponse countClashed(Integer id, String ip) {     //想了想MAC冲突，出现频次太低。而且站在业务角度，这个冲突拿到了也没有太大意义，因为我们不用MAC
        if (id == null && ip == null){
            return ServerResponse.createByErrorMessage("illeagal parameter");
        }

        if (ip != null) {
            int countRowByIp = terminalMapper.countByIp(ip);

            if (countRowByIp != 0) {
                return ServerResponse.createByErrorMessage("the IP has existed !");
            }
        }

        if (id != null){
            int countRowById = terminalMapper.countByPrimaryKey(id);
            if (countRowById != 0){
                return ServerResponse.createByErrorMessage("the ID has existed !");
            }
        }

        return ServerResponse.createBySuccessMessage("No Clash !");
    }

}
