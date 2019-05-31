package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.InitializationInclination;

public interface InitializationInclinationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InitializationInclination record);

    int insertSelective(InitializationInclination record);

    InitializationInclination selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InitializationInclination record);

    int updateByPrimaryKey(InitializationInclination record);
}