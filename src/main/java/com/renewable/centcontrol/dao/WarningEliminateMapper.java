package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.WarningEliminate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarningEliminateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarningEliminate record);

    int insertSelective(WarningEliminate record);

    WarningEliminate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarningEliminate record);

    int updateByPrimaryKey(WarningEliminate record);

    // custom
    List<WarningEliminate> selectList(@Param(value = "warningId") Integer warningId, @Param(value = "userId") Integer userId, @Param(value = "terminalId") Integer terminalId);
}