package io.datalake.controller.panel;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.auth.annotation.DePermission;
import io.datalake.auth.annotation.DePermissionProxy;
import io.datalake.auth.annotation.DePermissions;
import io.datalake.commons.constants.DePermissionType;
import io.datalake.commons.constants.ResourceAuthLevel;
import io.datalake.controller.handler.annotation.I18n;
import io.datalake.controller.request.panel.PanelGroupBaseInfoRequest;
import io.datalake.controller.request.panel.PanelGroupRequest;
import io.datalake.controller.request.panel.PanelViewDetailsRequest;
import io.datalake.dto.PermissionProxy;
import io.datalake.dto.authModel.VAuthModelDTO;
import io.datalake.dto.panel.PanelGroupDTO;
import io.datalake.plugins.common.base.domain.PanelGroup;
import io.datalake.service.panel.PanelGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Author: xqk
 * Date: 2021-03-05
 * Description:
 */
@Api(tags = "仪表板：仪表板组")
@ApiSupport(order = 150)
@RestController
@RequestMapping("panel/group")
public class PanelGroupController {

    @Resource
    private PanelGroupService panelGroupService;

    @ApiOperation("查询树")
    @PostMapping("/tree")
    public List<PanelGroupDTO> tree(@RequestBody PanelGroupRequest request) {
        return panelGroupService.tree(request);
    }

    @ApiOperation("默认树")
    @PostMapping("/defaultTree")
    public List<PanelGroupDTO> defaultTree(@RequestBody PanelGroupRequest request) {
        return panelGroupService.defaultTree(request);
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    @DePermissions(value = {
            @DePermission(type = DePermissionType.PANEL, value = "id"),
            @DePermission(type = DePermissionType.PANEL, value = "pid", level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE)
    }, logical = Logical.AND)
    @I18n
    public PanelGroup save(@RequestBody PanelGroupRequest request) throws Exception{
        String panelId = panelGroupService.save(request);
        return findOne(panelId);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @DePermissions(value = {
            @DePermission(type = DePermissionType.PANEL, value = "id"),
            @DePermission(type = DePermissionType.PANEL, value = "pid", level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE)
    }, logical = Logical.AND)
    @I18n
    public String update(@RequestBody PanelGroupRequest request) {
        return panelGroupService.update(request);
    }

    @ApiOperation("删除")
    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE)
    @PostMapping("/deleteCircle/{id}")
    public void deleteCircle(@PathVariable String id) {
        panelGroupService.deleteCircle(id);
    }

    @ApiOperation("详细信息")
    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW)
    @GetMapping("/findOne/{id}")
    public PanelGroupDTO findOne(@PathVariable String id) throws Exception {
        return panelGroupService.findOne(id);
    }

    @ApiIgnore
    @ApiOperation("详细信息(分享人代理)")
    @DePermissionProxy(paramIndex = 1)
    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW)
    @PostMapping("/proxy/findOne/{id}")
    public PanelGroupDTO proxyFindOne(@PathVariable String id, @RequestBody PermissionProxy proxy)
            throws Exception {
        return panelGroupService.findOne(id);
    }

    @ApiOperation("仪表板视图信息")
    @PostMapping("/queryPanelViewTree")
    @I18n
    public List<VAuthModelDTO> queryPanelViewTree() {
        return panelGroupService.queryPanelViewTree();
    }

    @ApiOperation("仪表板视图复用信息")
    @PostMapping("/queryPanelMultiplexingViewTree")
    @I18n
    public List<VAuthModelDTO> queryPanelMultiplexingViewTree() {
        return panelGroupService.queryPanelMultiplexingViewTree();
    }

    @ApiOperation("仪表板组件信息")
    @GetMapping("/queryPanelComponents/{id}")
    @I18n
    public Map queryPanelComponents(@PathVariable String id) {
        return panelGroupService.queryPanelComponents(id);
    }

    @ApiOperation("导出仪表板视图明细")
    @PostMapping("/exportDetails")
    @I18n
    public void exportDetails(@RequestBody PanelViewDetailsRequest request, HttpServletResponse response) throws IOException {
        panelGroupService.exportPanelViewDetails(request, response);
    }

    @ApiOperation("更新仪表板状态")
    @PostMapping("/updatePanelStatus/{panelId}")
    @I18n
    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE)
    public void updatePanelStatus(@PathVariable String panelId, @RequestBody PanelGroupBaseInfoRequest request) {
        panelGroupService.updatePanelStatus(panelId, request);
    }

}
