package com.neusoft.elmboot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.neusoft.elmboot.po.User;

@Mapper
public interface UserMapper {

    @Select("select * from user where userId=#{userId} and password=#{password}")
    public User getUserByIdByPass(User user);

    @Select("select * from user where userId=#{userId}")
    public User getUserByIdObject(String userId);

    @Select("select count(*) from user where userId=#{userId}")
    public int getUserById(String userId);

    @Insert("insert into user values(#{userId},#{password},#{userName},#{userSex},null,1,0,0,0.00,0)")
    public int saveUser(User user);

    @Update("<script>" +
            "update user " +
            "<set>" +
            "  <if test='userName != null'>userName=#{userName},</if>" +
            "  <if test='userSex != null'>userSex=#{userSex},</if>" +
            "  <if test='userImg != null'>userImg=#{userImg},</if>" +
            "  <if test='isVip != null'>isVip=#{isVip},</if>" +
            "  <if test='redEnvelope != null'>redEnvelope=#{redEnvelope},</if>" +
            "  <if test='goldCoins != null'>goldCoins=#{goldCoins},</if>" +
            "</set>" +
            "where userId=#{userId}" +
            "</script>")
    public int updateUser(User user);
}
