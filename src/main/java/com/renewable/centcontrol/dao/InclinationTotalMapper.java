package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.InclinationTotal;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface InclinationTotalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(InclinationTotal record);

    int insertSelective(InclinationTotal record);

    InclinationTotal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InclinationTotal record);

    int updateByPrimaryKey(InclinationTotal record);

    //custom
    List<InclinationTotal> selectList(@Param(value = "terminalId")int terminalId, @Param(value = "sensorId")int sensorId);

    List<InclinationTotal> selectListByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("terminalId") int terminalId, @Param("sensorId")int sensorId);

    InclinationTotal selectByTerminalAndSensor(@Param("terminalId") int terminalId, @Param("sensorId")int sensorId);

    int insertList(@Param("inclinationTotalList") List<InclinationTotal> inclinationTotalList);
}