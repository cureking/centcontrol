package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.InclinationInit;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface InclinationInitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(InclinationInit record);

    int insertSelective(InclinationInit record);

    InclinationInit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InclinationInit record);

    int updateByPrimaryKey(InclinationInit record);

    //custom
    List<InclinationInit> selectList(@Param(value = "terminalId")int terminalId, @Param(value = "sensorId")int sensorId);

    List<InclinationInit> selectListByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("terminalId") int terminalId, @Param("sensorId")int sensorId);

    InclinationInit selectByTerminalAndSensor(@Param("terminalId") int terminalId, @Param("sensorId")int sensorId);
}