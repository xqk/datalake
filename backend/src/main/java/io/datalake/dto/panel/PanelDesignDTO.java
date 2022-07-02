package io.datalake.dto.panel;

import io.datalake.dto.chart.ChartViewDTO;
import io.datalake.plugins.common.base.domain.PanelDesign;
import lombok.Data;

/**
 * Author: xqk
 * Date: 2021-03-17
 * Description:
 */
@Data
public class PanelDesignDTO extends PanelDesign {

    //当前视图是否在仪表板中显示
    private boolean keepFlag = false;

    //当前视图是否已经进行样式初始化
    private boolean styleInit = false;

    private ChartViewDTO chartView;

    private Object systemComponent;

    public PanelDesignDTO() {

    }
    public PanelDesignDTO(ChartViewDTO chartView) {
        this.chartView=chartView;
    }

}
