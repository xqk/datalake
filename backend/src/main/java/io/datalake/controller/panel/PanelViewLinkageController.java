package io.datalake.controller.panel;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.auth.annotation.DePermissionProxy;
import io.datalake.commons.model.BaseRspModel;
import io.datalake.controller.request.panel.PanelLinkageRequest;
import io.datalake.dto.PermissionProxy;
import io.datalake.service.panel.PanelViewLinkageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Author: xqk
 * Date: 8/4/21
 * Description:
 */
@Api(tags = "仪表板：视图联动")
@ApiSupport(order = 171)
@RestController
@RequestMapping("linkage")
public class PanelViewLinkageController {

    @Resource
    private PanelViewLinkageService panelViewLinkageService;

    @ApiOperation("获取仪表板所有视图联动信息")
    @PostMapping("/getViewLinkageGather")
    public Map getViewLinkageGather(@RequestBody PanelLinkageRequest request) {
        return panelViewLinkageService.getViewLinkageGather(request);
    }

    @ApiOperation("保存仪表板视图联动信息")
    @PostMapping("/saveLinkage")
    public BaseRspModel saveLinkage(@RequestBody PanelLinkageRequest request) {
        panelViewLinkageService.saveLinkage(request);
        return new BaseRspModel();
    }

    @ApiOperation("获取当前仪表板所有联动信息")
    @GetMapping("/getPanelAllLinkageInfo/{panelId}")
    public Map<String, List<String>> getPanelAllLinkageInfo(@PathVariable String panelId) {
        return panelViewLinkageService.getPanelAllLinkageInfo(panelId);
    }

    @ApiIgnore
    @ApiOperation("获取当前仪表板所有联动信息(分享人代理)")
    @DePermissionProxy(paramIndex = 1)
    @PostMapping("/proxy/getPanelAllLinkageInfo/{panelId}")
    public Map<String, List<String>> getPanelAllLinkageInfo(@PathVariable String panelId,
            @RequestBody PermissionProxy proxy) {
        return panelViewLinkageService.getPanelAllLinkageInfo(panelId);
    }

}
