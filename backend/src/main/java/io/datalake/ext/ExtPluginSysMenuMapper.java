package io.datalake.ext;

import io.datalake.plugins.common.dto.PluginSysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtPluginSysMenuMapper {

    void savePluginMenu(@Param("menuList") List<PluginSysMenu> menuList);

    int deletePluginMenu();
}
