package io.datalake.controller.response;

import io.datalake.plugins.common.base.domain.DatasetTable;
import io.datalake.plugins.common.base.domain.Datasource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataSetDetail {
    @ApiModelProperty("数据集")
    private DatasetTable table;
    @ApiModelProperty("数据源")
    private Datasource datasource;
}
