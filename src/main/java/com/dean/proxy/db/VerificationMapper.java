package com.dean.proxy.db;

import com.dean.proxy.bean.Verification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author dean
 */
@Repository
public interface VerificationMapper {

    @Insert({"INSERT INTO verification(`internal_id`, `use`, `verify_available_time`, `unavailable_time`) " +
        "VALUES(#{internalId}, #{use}, #{verifyAvailableTime}, #{unavailableTime})"})
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert(Verification verification);


    @Select({"select * from verification where `use` = true and internal_id=#{internalId}"})
    Verification getSuccessByInternalId(@Param("internalId") String internalId);


    @Update({"<script> update verification set id=#{id}"
        + "<if test='verifyAvailableTime!=null'> ,verify_available_time=#{verifyAvailableTime} </if>"
        + "<if test='unavailableTime!=null'> ,unavailable_time=#{unavailableTime} </if>"
        + "<if test='duration!=null'> ,duration=#{duration} </if>"
        + "<if test='use!=null'> ,`use`=#{use} </if>"
        + "where id=#{id} </script>"})
    boolean update(Verification p);
}
