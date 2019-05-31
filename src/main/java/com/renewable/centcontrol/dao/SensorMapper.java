package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.Sensor;

public interface SensorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sensor record);

    int insertSelective(Sensor record);

    Sensor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sensor record);

    int updateByPrimaryKey(Sensor record);
}