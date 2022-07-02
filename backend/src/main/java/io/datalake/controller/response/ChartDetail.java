package io.datalake.controller.response;

import io.datalake.plugins.common.base.domain.ChartViewWithBLOBs;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChartDetail extends DataSetDetail{
    @ApiModelProperty("视图")
    private ChartViewWithBLOBs chart;
}
