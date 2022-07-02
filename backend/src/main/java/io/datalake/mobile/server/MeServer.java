package io.datalake.mobile.server;


import io.datalake.mobile.api.MeApi;
import io.datalake.mobile.dto.MeItemDTO;
import io.datalake.mobile.service.MeService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MeServer implements MeApi {

    @Resource
    private MeService meService;
    @Override
    public MeItemDTO query() {
        return meService.personInfo();
    }
}
