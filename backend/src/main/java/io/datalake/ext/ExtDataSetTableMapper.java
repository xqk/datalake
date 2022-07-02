package io.datalake.ext;

import io.datalake.controller.request.dataset.DataSetTableRequest;
import io.datalake.dto.dataset.DataSetTableDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtDataSetTableMapper {
    List<DataSetTableDTO> search(DataSetTableRequest request);

    DataSetTableDTO searchOne(DataSetTableRequest request);

    List<DataSetTableDTO> searchDataSetTableWithPanelId(@Param("panelId") String panelId, @Param("userId") String userId);

}
