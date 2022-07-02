package io.datalake.dto.panel.linkJump;

import io.datalake.plugins.common.base.domain.PanelLinkJump;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: xqk
 * Date: 2021/10/25
 * Description:
 */
@Data
public class PanelLinkJumpDTO  extends PanelLinkJump {

    //仪表板可以跳转视图信息 sourceViewId#
    private String sourceInfo;

    private List<String> targetInfoList;

    private List<PanelLinkJumpInfoDTO> linkJumpInfoArray = new ArrayList<>();

    private Map<String,PanelLinkJumpInfoDTO> mapJumpInfoArray = new HashMap<>();


}
