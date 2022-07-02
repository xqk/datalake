package io.datalake.controller.sys.response;

import io.datalake.plugins.common.base.domain.SysDept;
import lombok.Data;


@Data
public class DeptNodeResponse extends SysDept {
    private boolean hasChildren;

    private boolean leaf;

    private boolean top;

}
