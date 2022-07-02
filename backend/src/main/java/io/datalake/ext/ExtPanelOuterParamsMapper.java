package io.datalake.ext;

import io.datalake.dto.panel.outerParams.PanelOuterParamsDTO;
import io.datalake.dto.panel.outerParams.PanelOuterParamsInfoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtPanelOuterParamsMapper {

    PanelOuterParamsDTO queryWithPanelId(@Param("panelId") String panelId);

    void deleteOuterParamsTargetWithPanelId(@Param("panelId") String panelId);

    void deleteOuterParamsInfoWithPanelId(@Param("panelId") String panelId);

    void deleteOuterParamsWithPanelId(@Param("panelId") String panelId);

    List<PanelOuterParamsInfoDTO> getPanelOuterParamsInfo(@Param("panelId") String panelId);

}
