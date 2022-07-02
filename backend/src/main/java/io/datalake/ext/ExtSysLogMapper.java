package io.datalake.ext;

import io.datalake.dto.log.FolderItem;
import io.datalake.ext.query.GridExample;
import io.datalake.plugins.common.base.domain.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtSysLogMapper {

    List<SysLogWithBLOBs> query(GridExample example);

    List<FolderItem> idAndName(@Param("ids") List<String> ids, @Param("type") Integer type);
}
