package io.datalake.controller.sys.request;

import lombok.Data;

@Data
public class MenuDeleteRequest {

    private Long menuId;

    private Long pid;
}
