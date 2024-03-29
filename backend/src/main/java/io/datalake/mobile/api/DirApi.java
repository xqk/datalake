package io.datalake.mobile.api;


import io.datalake.mobile.dto.DirItemDTO;
import io.datalake.mobile.dto.DirRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/mobile/dir")
public interface DirApi {


    @PostMapping("/query")
    List<DirItemDTO> query(@RequestBody DirRequest request);
}
