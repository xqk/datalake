package io.datalake.ext;

import io.datalake.controller.sys.response.SysUserGridResponse;
import io.datalake.ext.query.GridExample;

import java.util.List;

public interface ExtSysUserMapper {
    List<SysUserGridResponse> query(GridExample example);

    List<String> ldapUserNames(Integer from);
}
