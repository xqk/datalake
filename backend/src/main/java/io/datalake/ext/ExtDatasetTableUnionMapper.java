package io.datalake.ext;

import io.datalake.dto.dataset.DataSetTableUnionDTO;
import io.datalake.plugins.common.base.domain.DatasetTableUnion;

import java.util.List;

public interface ExtDatasetTableUnionMapper {
    List<DataSetTableUnionDTO> selectBySourceTableId(String tableId);

    List<DataSetTableUnionDTO> selectByTargetTableId(String tableId);

    List<DataSetTableUnionDTO> selectUsedFieldBySource(DatasetTableUnion datasetTableUnion);

    List<DataSetTableUnionDTO> selectUsedFieldByTarget(DatasetTableUnion datasetTableUnion);
}