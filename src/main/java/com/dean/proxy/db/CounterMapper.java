package com.dean.proxy.db;

import com.dean.proxy.bean.Counter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterMapper {

    @Insert({"INSERT INTO counter(`internal_id`, `count`, `timestamp`) " +
        "VALUES(#{internalId}, #{count}, #{timestamp})"})
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert( Counter counter);

    @Select({"select internal_id, SUM(`use`) as `use` from counter where internal_id=#{internalId}"})
    Counter getByInternal(@Param("internalId") String internalId);
}
