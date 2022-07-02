package io.datalake.ext;

import io.datalake.dto.PanelGroupExtendDataDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtPanelGroupExtendDataMapper {
    void savePanelExtendData(@Param("records") List<PanelGroupExtendDataDTO> records);

    void copyExtendData(@Param("sourceViewId")String sourceViewId,@Param("newViewId")String newViewId,@Param("newPanelId")String newPanelId);

    void copyWithCopyId(@Param("copyId")String copyId);
}
