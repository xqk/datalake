package io.datalake.controller.request.chart;

import io.datalake.plugins.common.base.domain.ChartViewCacheWithBLOBs;
import lombok.Data;

/**
 * Author: xqk
 * Date: 2022/3/10
 * Description:
 */
@Data
public class ChartViewCacheRequest  extends ChartViewCacheWithBLOBs {

    private String savePosition = "cache";
}
