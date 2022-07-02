package io.datalake.dto;

import io.datalake.controller.request.datasource.ApiDefinition;
import io.datalake.plugins.common.base.domain.Datasource;
import io.datalake.plugins.common.constants.DatasourceCalculationMode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2021-05-18
 * Description:
 */
@Data
public class DatasourceDTO extends Datasource {

    @ApiModelProperty("权限")
    private String privileges;
    private List<ApiDefinition> apiConfiguration;
    private String typeDesc;
    private DatasourceCalculationMode calculationMode;
}
