package io.datalake.dto.panel.po;


import io.datalake.plugins.common.base.domain.PanelView;

/**
 * Author: xqk
 * Date: 2021-07-06
 * Description:
 */
public class PanelViewInsertDTO extends PanelView {

    public PanelViewInsertDTO() {
    }

    public PanelViewInsertDTO(String chartViewId,String panelGroupId) {
        super();
        super.setChartViewId(chartViewId);
        super.setPanelId(panelGroupId);
        super.setPosition("panel");
    }
    public PanelViewInsertDTO(String chartViewId,String panelGroupId,String position) {
        super();
        super.setChartViewId(chartViewId);
        super.setPanelId(panelGroupId);
        super.setPosition(position);
    }

}
