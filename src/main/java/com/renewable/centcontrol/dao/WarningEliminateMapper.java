package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.WarningEliminate;

public interface WarningEliminateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarningEliminate record);

    int insertSelective(WarningEliminate record);

    WarningEliminate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarningEliminate record);

    int updateByPrimaryKey(WarningEliminate record);
}