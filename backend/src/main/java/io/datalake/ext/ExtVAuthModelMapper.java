package io.datalake.ext;

import io.datalake.controller.request.authModel.VAuthModelRequest;
import io.datalake.dto.authModel.VAuthModelDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtVAuthModelMapper {

    List<VAuthModelDTO> queryAuthModel(@Param("record") VAuthModelRequest record);

    List<VAuthModelDTO> queryAuthModelViews (@Param("record") VAuthModelRequest record);

    List<VAuthModelDTO> queryAuthViewsOriginal (@Param("record") VAuthModelRequest record);
}
