package io.datalake.controller.sys.request;

import io.datalake.plugins.common.base.domain.SysMenu;
import lombok.Data;

@Data
public class MenuCreateRequest extends SysMenu {

    private boolean top;
}
