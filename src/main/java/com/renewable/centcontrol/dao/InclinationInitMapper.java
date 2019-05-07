package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.InclinationInit;

public interface InclinationInitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(InclinationInit record);

    int insertSelective(InclinationInit record);

    InclinationInit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InclinationInit record);

    int updateByPrimaryKey(InclinationInit record);
}