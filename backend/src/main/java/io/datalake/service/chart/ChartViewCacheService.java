package io.datalake.service.chart;

import io.datalake.ext.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Author: xqk
 * Date: 2022/4/2
 * Description:
 */
@Service
public class ChartViewCacheService {

    @Resource
    private ExtChartViewMapper extChartViewMapper;

    @Transactional
    public void refreshCache(String viewId){
        if(extChartViewMapper.updateToCache(viewId)==0){
            extChartViewMapper.copyToCache(viewId);
        }
    }

    public void resetView(String viewId){
        extChartViewMapper.updateToViewFromCache(viewId);
    }

}
