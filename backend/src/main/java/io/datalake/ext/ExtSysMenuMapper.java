package io.datalake.ext;

import io.datalake.controller.sys.request.SimpleTreeNode;
import io.datalake.ext.query.GridExample;
import io.datalake.plugins.common.base.domain.SysMenu;

import java.util.List;

public interface ExtSysMenuMapper {

    List<SimpleTreeNode> allNodes();

    List<SimpleTreeNode> nodesByExample(GridExample example);

    List<SysMenu> querySysMenu();
}
