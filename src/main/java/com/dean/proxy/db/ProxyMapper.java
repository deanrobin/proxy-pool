package com.dean.proxy.db;

import java.util.List;

import com.dean.proxy.bean.Proxy;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author dean
 */
@Repository
public interface ProxyMapper {

    @Insert({"INSERT INTO proxy(`internal_id`, `ip`, `port`, anonymity, type, source, "
        + "country, location, response_time, `timestamp`) " +
        "VALUES(#{internalId}, #{ip}, #{port}, #{anonymity}, #{type}, #{source}, "
        + "#{country}, #{location}, #{responseTime}, #{timestamp})"})
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert( Proxy p);

    //@Select("select * from proxy where status = 1")
    //List<Proxy> getAllProxy();

    @Select("select * from proxy where internal_id = #{internalId}")
    Proxy getByInternal(@Param("internalId") String internalId);

    @Select({"select * from proxy where status=0 order by id asc limit 0, 10"})
    List<Proxy> getNeedVerify();

    @Select({"select * from proxy where status=1 order by id asc"})
    List<Proxy> getSuccessVerify();

    @Select({"select * from proxy where status=10 ORDER BY RAND() LIMIT 20"})
    List<Proxy> getNeedVerifyAgain();

    @Select({"select * from proxy where status=12 ORDER BY id asc"})
    List<Proxy> getUnavailableVerify();

    @Update({"<script> update proxy set status=#{status}"
        + "<if test='validateTime!=null'> ,validate_time=#{validateTime} </if>"
        + "<if test='validateCount!=null'> ,validate_count=#{validateCount} </if>"
        + "<if test='availableCount!=null'> ,available_count=#{availableCount} </if>"
        + "<if test='continuousUnavailableNumber!=null'> ,continuous_unavailable_number=#{continuousUnavailableNumber} </if>"
        + "where id=#{id} </script>"})
    boolean updateStatus(Proxy p);


    @Select({"<script> select * from proxy where status=1 "
        + "<if test='country!=null'> and country=#{country} </if>"
        + "<if test='source!=null'> and source=#{source} </if>"
        + "<if test='type!=null'> and type=#{type} </if>"
        + "order by proxy.${sortKey} desc limit 0, #{count};</script>"})
    List<Proxy> query(@Param("country") String country, @Param("source") String source,
                      @Param("type") String type, @Param("sortKey") String sortKey,
                      @Param("count") Integer count);

    @Select({"select count(*) from proxy where status=1"})
    Integer successCount();

    @Select({"select count(*) from proxy"})
    Integer count();

    @Select({"select count(*) from proxy where status=0"})
    Integer unverifiedCount();

}
