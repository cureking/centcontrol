package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.Warning;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WarningMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Warning record);

    int insertSelective(Warning record);

    Warning selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Warning record);

    int updateByPrimaryKey(Warning record);

    // custom
    int getLastWarningBySensorRegisterIdAndLastCreateTime(@Param("sensorRegisterId") int sensorRegisterId, @Param("lastCreateTime") Date lastCreateTime);

    List<Warning> selectListWithPageHelper(@Param("terminalId") Integer terminalId, @Param("sensorRegisterId") Integer sensorRegisterId);
}