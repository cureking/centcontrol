package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.InitializationInclination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InitializationInclinationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InitializationInclination record);

    int insertSelective(InitializationInclination record);

    InitializationInclination selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InitializationInclination record);

    int updateByPrimaryKey(InitializationInclination record);

    // custom
    InitializationInclination selectBySensorRegisterId(int sensorRegisterId);

    List<InitializationInclination> listByTerminalId(int terminalId);

    int updateBatch(@Param("initializationInclinationList") List<InitializationInclination> initializationInclinationList);

}