package io.datalake.service.panel;

import io.datalake.plugins.common.base.domain.PanelPdfTemplate;
import io.datalake.plugins.common.base.domain.PanelPdfTemplateExample;
import io.datalake.plugins.common.base.mapper.PanelPdfTemplateMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: xqk
 * Date: 9/1/21
 * Description:
 */
@Service
public class PanelPdfTemplateService {

    @Resource
    private PanelPdfTemplateMapper panelPdfTemplateMapper;

    public List<PanelPdfTemplate> queryAll() {
        PanelPdfTemplateExample example = new PanelPdfTemplateExample();
        example.setOrderByClause("sort asc");
        return panelPdfTemplateMapper.selectByExampleWithBLOBs(example);
    }
}
