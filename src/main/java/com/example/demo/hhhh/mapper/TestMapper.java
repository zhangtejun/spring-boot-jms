package com.example.demo.hhhh.mapper;


import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {
    @Select("select * from test where id= #{id}")
    Map<String,Object> findById(@Param("id") String id);
    @Select("select * from test")
    List<Map<String,Object>> findAll();

    @Insert("insert into weixin_access_token(access_token, expires_in) values( #{accessToken}, #{expiresIn})")
    void saveAccessToken(@Param("accessToken") String accessToken,@Param("expiresIn") int expiresIn);
    @Update("update  weixin_access_token set access_token=#{accessToken}, expires_in=#{expiresIn}")
    void updateAccessToken(@Param("accessToken") String accessToken,@Param("expiresIn") int expiresIn);

    @Select("select * from weixin_access_token")
    Map<String,Object> find();
}
