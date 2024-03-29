package io.datalake.controller.sys;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.commons.utils.PageUtils;
import io.datalake.commons.utils.Pager;
import io.datalake.controller.handler.annotation.I18n;
import io.datalake.controller.sys.base.BaseGridRequest;
import io.datalake.dto.SysLogGridDTO;
import io.datalake.dto.log.FolderItem;
import io.datalake.service.sys.log.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "系统：日志管理")
@ApiSupport(order = 220)
@RequestMapping("/api/log")
public class SysLogController {

    @Resource
    private LogService logService;

    @I18n
    @ApiOperation("查询日志")
    @PostMapping("/logGrid/{goPage}/{pageSize}")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", name = "goPage", value = "页码", required = true, dataType = "Integer"),
        @ApiImplicitParam(paramType = "path", name = "pageSize", value = "页容量", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "request", value = "查询条件", required = true)
    })
    public Pager<List<SysLogGridDTO>> logGrid(@PathVariable int goPage, @PathVariable int pageSize,
                                              @RequestBody BaseGridRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, logService.query(request));
    }

    @ApiOperation("操作类型")
    @PostMapping("/opTypes")
    public List<FolderItem> types() {
        return logService.types();
    }

    @ApiOperation("导出操作日志")
    @PostMapping("/export")
    @ApiImplicitParam(name = "request", value = "查询条件", required = true)
    public void export(@RequestBody BaseGridRequest request) throws Exception{
        logService.exportExcel(request);
    }
}
