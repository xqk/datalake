package io.datalake.dto;

import io.datalake.plugins.common.base.domain.SysAuthDetail;
import lombok.Data;

/**
 * Author: xqk
 * Date: 2021-06-03
 * Description:
 */
@Data
public class SysAuthDetailDTO extends SysAuthDetail {
    private String authSource;

    private String authSourceType;

    private String authTarget;

    private String authTargetType;
}
