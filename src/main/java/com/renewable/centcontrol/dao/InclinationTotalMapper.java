package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.InclinationTotal;

public interface InclinationTotalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(InclinationTotal record);

    int insertSelective(InclinationTotal record);

    InclinationTotal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InclinationTotal record);

    int updateByPrimaryKey(InclinationTotal record);
}