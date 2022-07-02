package io.datalake.ext;

import io.datalake.dto.panel.PanelStoreDto;
import io.datalake.ext.query.GridExample;

import java.util.List;

public interface ExtPanelStoreMapper {

    List<PanelStoreDto> query(GridExample example);
}
