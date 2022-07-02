package io.datalake.controller.sys.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenusRequest {

    private Long roleId;

    private List<Long> menuIds;
}
