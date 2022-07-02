package io.datalake.ext;

import io.datalake.controller.request.chart.ChartGroupRequest;
import io.datalake.dto.chart.ChartGroupDTO;

import java.util.List;

public interface ExtChartGroupMapper {
    List<ChartGroupDTO> search(ChartGroupRequest ChartGroup);
}
