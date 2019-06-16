package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.SensorRegister;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SensorRegisterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SensorRegister record);

    int insertSelective(SensorRegister record);

    SensorRegister selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SensorRegister record);

    int updateByPrimaryKey(SensorRegister record);

    // custom
    Integer insertSelectiveAndReturnId(SensorRegister sensorRegister);

    List<SensorRegister> listSensorRegisterByTerminalId(Integer terminalId);

    List<SensorRegister> listSensorRegister();

    int updateBatch(@Param("sensorRegisterList") List<SensorRegister> sensorRegisterList);
}