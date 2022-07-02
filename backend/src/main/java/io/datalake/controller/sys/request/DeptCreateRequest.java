package io.datalake.controller.sys.request;

import io.datalake.plugins.common.base.domain.SysDept;
import lombok.Data;

@Data
public class DeptCreateRequest extends SysDept {

    private boolean top;

}
