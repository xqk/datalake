package io.datalake.mobile.api;

import io.datalake.commons.utils.Pager;
import io.datalake.mobile.dto.HomeItemDTO;
import io.datalake.mobile.dto.HomeItemShareDTO;
import io.datalake.mobile.dto.HomeRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/mobile/home")
public interface HomeApi {

    @PostMapping("/query")
    Pager<List<HomeItemDTO>> query(@RequestBody HomeRequest request);

    @PostMapping("/queryShares")
    Pager<List<HomeItemShareDTO>> queryShares(@RequestBody HomeRequest request);

    @PostMapping("/detail/{id}")
    Object detail(@PathVariable String id);

}
