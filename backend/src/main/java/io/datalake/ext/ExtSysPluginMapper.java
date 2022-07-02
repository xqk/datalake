package io.datalake.ext;

import io.datalake.ext.query.GridExample;
import io.datalake.plugins.common.base.domain.MyPlugin;

import java.util.List;

public interface ExtSysPluginMapper {

    List<MyPlugin> query(GridExample example);
}
