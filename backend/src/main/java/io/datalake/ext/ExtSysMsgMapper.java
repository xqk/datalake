package io.datalake.ext;

import io.datalake.controller.sys.response.MsgGridDto;
import io.datalake.plugins.common.base.domain.SysMsgExample;
import io.datalake.plugins.common.base.domain.SysMsgSetting;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExtSysMsgMapper {

    @Update({
            "<script>",
            "update sys_msg set status = 1, read_time = #{time} where msg_id in ",
            "<foreach collection='msgIds' item='msgId' open='(' separator=',' close=')' >",
            " #{msgId}",
            "</foreach>",
            "</script>"
    })
    int batchStatus(@Param("msgIds") List<Long> msgIds, @Param("time") Long time);

    @Delete({
            "<script>",
            "delete from sys_msg where msg_id in ",
            "<foreach collection='msgIds' item='msgId' open='(' separator=',' close=')' >",
            " #{msgId}",
            "</foreach>",
            "</script>"
    })
    int batchDelete(@Param("msgIds") List<Long> msgIds);

    int batchInsert(@Param("settings") List<SysMsgSetting> settings);

    List<MsgGridDto> queryGrid(SysMsgExample example);

}
