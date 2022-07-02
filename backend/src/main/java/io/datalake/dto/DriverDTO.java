package io.datalake.dto;

import io.datalake.plugins.common.base.domain.DeDriver;
import lombok.Data;

@Data
public class DriverDTO extends DeDriver {
    private String typeDesc;
}
