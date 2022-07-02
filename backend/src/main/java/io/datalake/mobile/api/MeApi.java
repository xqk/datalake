package io.datalake.mobile.api;

import io.datalake.mobile.dto.MeItemDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RequestMapping("/mobile/me")
public interface MeApi {

    @PostMapping("/query")
    MeItemDTO query();
}
