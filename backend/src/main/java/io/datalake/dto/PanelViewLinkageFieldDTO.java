package io.datalake.dto;

import io.datalake.plugins.common.base.domain.PanelViewLinkageField;

/**
 * Author: xqk
 * Date: 8/4/21
 * Description:
 */
public class PanelViewLinkageFieldDTO extends PanelViewLinkageField {

    private String sourceViewId;

    private String targetViewId;

    public String getSourceViewId() {
        return sourceViewId;
    }

    public void setSourceViewId(String sourceViewId) {
        this.sourceViewId = sourceViewId;
    }

    public String getTargetViewId() {
        return targetViewId;
    }

    public void setTargetViewId(String targetViewId) {
        this.targetViewId = targetViewId;
    }
}
