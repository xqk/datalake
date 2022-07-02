package io.datalake.controller.sys.response;

import io.datalake.plugins.common.base.domain.SysMenu;
import lombok.Data;

@Data
public class MenuNodeResponse extends SysMenu {

    private boolean hasChildren;

    private boolean top;
}
