package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.Terminal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Terminal record);

    int insertSelective(Terminal record);

    Terminal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Terminal record);

    int updateByPrimaryKey(Terminal record);

    //custom
    List<Terminal> selectList(int projectId);

//    int countClashed(@Param(value = "stateCode")Integer stateCode, @Param(value = "id")int id, @Param(value = "ip")String ip, @Param(value = "mac")String mac);

    Terminal selectByIp(String ip);

    int countByIp(String ip);

    int countByPrimaryKey(int id);
}