package io.datalake.dto.panel.outerParams;

import io.datalake.plugins.common.base.domain.PanelOuterParamsInfo;
import io.datalake.plugins.common.base.domain.PanelOuterParamsTargetViewInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: xqk
 * Date: 2022/3/17
 * Description:
 */
@Data
public class PanelOuterParamsInfoDTO extends PanelOuterParamsInfo {

    private String panelId;

    private List<PanelOuterParamsTargetViewInfo> targetViewInfoList=new ArrayList<>();

    //仪表板外部参数信息 panelId#paramName
    private String sourceInfo;

    //目标联动参数 targetViewId#targetFieldId
    private List<String> targetInfoList;
}
