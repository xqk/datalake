package io.datalake.mobile.server;

import io.datalake.commons.utils.Pager;
import io.datalake.mobile.api.HomeApi;
import io.datalake.mobile.dto.HomeItemDTO;
import io.datalake.mobile.dto.HomeItemShareDTO;
import io.datalake.mobile.dto.HomeRequest;
import io.datalake.mobile.service.HomeService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HomeServer implements HomeApi {

    @Resource
    private HomeService homeService;

    @Override
    public Pager<List<HomeItemDTO>> query(HomeRequest request) {
        return homeService.query(request);
    }

    @Override
    public Object detail(String id) {
        return null;
    }

    @Override
    public Pager<List<HomeItemShareDTO>> queryShares(HomeRequest request) {

        return homeService.queryShares(request);
    }

}
