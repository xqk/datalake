package io.datalake.dto.panel;

import io.datalake.plugins.common.base.domain.ChartViewWithBLOBs;
import io.datalake.plugins.common.base.domain.DatasetTableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2021-03-05
 * Description:
 */
@Data
public class PanelViewTableDTO extends ChartViewWithBLOBs {
    @ApiModelProperty("仪表板ID")
    private String panelId;

    private List<DatasetTableField> tableFields;


}
