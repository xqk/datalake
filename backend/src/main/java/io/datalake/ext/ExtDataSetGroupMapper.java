package io.datalake.ext;

import io.datalake.controller.request.dataset.DataSetGroupRequest;
import io.datalake.dto.dataset.DataSetGroupDTO;

import java.util.List;
import java.util.Map;

public interface ExtDataSetGroupMapper {
    List<DataSetGroupDTO> search(DataSetGroupRequest ChartGroup);

    Map<String, String> searchIds(String id, String type);
}
