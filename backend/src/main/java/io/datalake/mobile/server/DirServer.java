package io.datalake.mobile.server;

import io.datalake.mobile.api.DirApi;
import io.datalake.mobile.dto.DirItemDTO;
import io.datalake.mobile.dto.DirRequest;
import io.datalake.mobile.service.DirService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DirServer implements DirApi {

    @Resource
    private DirService dirService;

    @Override
    public List<DirItemDTO> query(DirRequest request) {
        return dirService.query(request);
    }
}
