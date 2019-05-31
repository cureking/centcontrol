package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.SensorRegister;

public interface SensorRegisterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SensorRegister record);

    int insertSelective(SensorRegister record);

    SensorRegister selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SensorRegister record);

    int updateByPrimaryKey(SensorRegister record);

    // custom
    Integer insertSelectiveAndReturnId(SensorRegister sensorRegister);
}