package io.datalake.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionProxy implements Serializable {

    private Long userId;

}
