package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.InclinationRegister;

public interface InclinationRegisterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InclinationRegister record);

    int insertSelective(InclinationRegister record);

    InclinationRegister selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InclinationRegister record);

    int updateByPrimaryKey(InclinationRegister record);
}