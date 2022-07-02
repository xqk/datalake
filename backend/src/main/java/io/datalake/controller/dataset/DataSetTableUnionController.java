package io.datalake.controller.dataset;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.auth.annotation.DePermission;
import io.datalake.commons.constants.DePermissionType;
import io.datalake.commons.constants.ResourceAuthLevel;
import io.datalake.dto.dataset.DataSetTableUnionDTO;
import io.datalake.plugins.common.base.domain.DatasetTableUnion;
import io.datalake.service.dataset.DataSetTableUnionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author gin
 * @Date 2021/5/7 10:30 上午
 */
@Api(tags = "数据集：数据集关联")
@ApiSupport(order = 70)
@RestController
@RequestMapping("dataset/union")
public class DataSetTableUnionController {
    @Resource
    private DataSetTableUnionService dataSetTableUnionService;

    @DePermission(type = DePermissionType.DATASET, value = "sourceTableId", level = ResourceAuthLevel.DATASET_LEVEL_MANAGE)
    @ApiOperation("保存")
    @PostMapping("save")
    public DatasetTableUnion save(@RequestBody DatasetTableUnion datasetTableUnion) {
        return dataSetTableUnionService.save(datasetTableUnion);
    }

    @DePermission(type = DePermissionType.DATASET, value = "sourceTableId", level = ResourceAuthLevel.DATASET_LEVEL_MANAGE)
    @ApiOperation("删除")
    @PostMapping("delete")
    public void delete(@RequestBody DatasetTableUnion datasetTableUnion) {
        dataSetTableUnionService.delete(datasetTableUnion.getId());
    }

    @DePermission(type = DePermissionType.DATASET)
    @ApiOperation("查询")
    @PostMapping("listByTableId/{tableId}")
    public List<DataSetTableUnionDTO> listByTableId(@PathVariable String tableId) {
        return dataSetTableUnionService.listByTableId(tableId);
    }
}
