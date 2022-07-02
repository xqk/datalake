package io.datalake.ext;

import io.datalake.controller.sys.response.RoleUserItem;
import io.datalake.ext.query.GridExample;
import io.datalake.plugins.common.base.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ExtSysRoleMapper {


    List<SysRole> query(GridExample example);

    int deleteRoleMenu(@Param("roleId") Long roleId);

    int batchInsertRoleMenu(@Param("maps") List<Map<String, Long>> maps);

    List<RoleUserItem> queryAll();

    List<Long> menuIds(@Param("roleId") Long roleId);
}
