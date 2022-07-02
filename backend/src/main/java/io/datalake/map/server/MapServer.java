package io.datalake.map.server;

import io.datalake.commons.utils.LogUtil;
import io.datalake.map.api.MapApi;
import io.datalake.map.dto.entity.AreaEntity;
import io.datalake.map.service.MapService;
import io.datalake.map.utils.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MapServer implements MapApi {



    @Resource
    private MapService mapService;

    @Override
    public String resourceFull(@PathVariable String areaCode) {
        return mapService.geometry(areaCode);
    }

    @Override
    public String asyncGeometry() {
        try {
            List<AreaEntity> areaEntities = mapService.areaEntities();
            MapUtils.recursionWriteFull(areaEntities);
        }catch (Exception e) {
            LogUtil.error(e);
            return e.getMessage();
        }
        return "async success";
    }

    @Override
    public List<AreaEntity> areaEntitys(@PathVariable String pcode) {
        List<AreaEntity> areaEntities = mapService.areaEntities();
        if (StringUtils.equals(pcode, "0")) {
            return areaEntities;
        }
        return mapService.entitysByPid(areaEntities, pcode);
    }

    @Override
    public void retry(@PathVariable String areaCode) {
        List<AreaEntity> areaEntities = mapService.areaEntities();
        AreaEntity areaEntity = MapUtils.nodeByCode(areaEntities, areaCode);
        List<AreaEntity> targets = new ArrayList<>();
        targets.add(areaEntity);
        MapUtils.recursionWriteFull(targets);
    }
}
