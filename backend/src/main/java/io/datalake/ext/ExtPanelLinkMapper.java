package io.datalake.ext;

import io.datalake.controller.request.panel.link.OverTimeRequest;
import org.apache.ibatis.annotations.Param;

public interface ExtPanelLinkMapper {

    void updateOverTime(@Param("request") OverTimeRequest request);
    
}
