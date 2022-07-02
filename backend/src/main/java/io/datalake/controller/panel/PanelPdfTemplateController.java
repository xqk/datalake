package io.datalake.controller.panel;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.plugins.common.base.domain.PanelPdfTemplate;
import io.datalake.service.panel.PanelPdfTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * Author: xqk
 * Date: 2021-03-05
 * Description:
 */
@Api(tags = "仪表板：PDF导出模板")
@ApiSupport(order = 170)
@RestController
@RequestMapping("pdf-template")
public class PanelPdfTemplateController {

    @Resource
    private PanelPdfTemplateService panelPdfTemplateService;

    @GetMapping("queryAll")
    @ApiOperation("查询所有仪表板模板")
    public List<PanelPdfTemplate> queryAll() {
        return panelPdfTemplateService.queryAll();
    }


}
