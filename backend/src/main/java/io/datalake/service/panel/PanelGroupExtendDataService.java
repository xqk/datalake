package io.datalake.service.panel;

import com.google.gson.Gson;
import io.datalake.dto.chart.ChartViewDTO;
import io.datalake.exception.DataLakeException;
import io.datalake.plugins.common.base.domain.PanelGroupExtendData;
import io.datalake.plugins.common.base.domain.PanelGroupExtendDataExample;
import io.datalake.plugins.common.base.mapper.PanelGroupExtendDataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: xqk
 * Date: 2022/3/15
 * Description:
 */
@Service
public class PanelGroupExtendDataService {

    @Resource
    private PanelGroupExtendDataMapper panelGroupExtendDataMapper;

    public ChartViewDTO getChartDataInfo(String viewId,ChartViewDTO view){
        Gson gson = new Gson();
        PanelGroupExtendDataExample extendDataExample = new PanelGroupExtendDataExample();
        extendDataExample.createCriteria().andViewIdEqualTo(viewId);
        List<PanelGroupExtendData>  extendDataList = panelGroupExtendDataMapper.selectByExampleWithBLOBs(extendDataExample);
        if(CollectionUtils.isNotEmpty(extendDataList)){
            ChartViewDTO chartViewTemplate = gson.fromJson(extendDataList.get(0).getViewDetails(),ChartViewDTO.class);
            view.setData(chartViewTemplate.getData());
        }else{
            DataLakeException.throwException("模板缓存数据中未获取指定视图数据："+viewId);
        }
        return view;
    }


}
