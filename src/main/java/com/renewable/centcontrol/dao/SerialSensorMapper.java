package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.SerialSensor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SerialSensorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SerialSensor record);

    int insertSelective(SerialSensor record);

    SerialSensor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SerialSensor record);

    int updateByPrimaryKey(SerialSensor record);

    // custom
    int insertBatch(@Param("serialSensorList") List<SerialSensor> serialSensorList);

    Integer insertSelectiveAndReturnId(SerialSensor serialSensor);

    List<SerialSensor> selectByTerminalIdAndPort(@Param("terminalId") int terminalId, @Param("port") String port);

    List<SerialSensor> selectList(Integer terminalId);

    int updateBatch(@Param("serialSensorList") List<SerialSensor> serialSensorList);
}