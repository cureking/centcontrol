package com.renewable.centcontrol.dao;

import com.renewable.centcontrol.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    //custom
    int checkUsername(String username);

    int checkEmail(String email);

    //多个输入参数时，需要采用mybatis的Param注解。在对应的xml文件中，参数名取得是Param注解中的名字
    User selectLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    int checkPassword(@Param("passwordOld") String passwordOld, @Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}