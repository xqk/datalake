package io.datalake.ext;

import io.datalake.controller.request.DatasourceUnionRequest;
import io.datalake.dto.DatasourceDTO;
import io.datalake.ext.query.GridExample;

import java.util.List;

public interface ExtDataSourceMapper {

    List<DatasourceDTO> query(GridExample example);

    List<DatasourceDTO> queryUnion(DatasourceUnionRequest request);



}
