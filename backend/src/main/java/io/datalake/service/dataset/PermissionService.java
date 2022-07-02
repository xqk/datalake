package io.datalake.service.dataset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.datalake.auth.api.dto.CurrentRoleDto;
import io.datalake.auth.entity.SysUserEntity;
import io.datalake.auth.service.AuthUserService;
import io.datalake.commons.constants.ColumnPermissionConstants;
import io.datalake.commons.utils.AuthUtils;
import io.datalake.plugins.common.base.domain.DatasetTable;
import io.datalake.plugins.common.base.domain.DatasetTableField;
import io.datalake.plugins.common.dto.chart.ChartCustomFilterItemDTO;
import io.datalake.plugins.common.dto.chart.ChartFieldCustomFilterDTO;
import io.datalake.plugins.config.SpringContextUtil;
import io.datalake.plugins.xpack.auth.dto.request.*;
import io.datalake.plugins.xpack.auth.service.ColumnPermissionService;
import io.datalake.plugins.xpack.auth.service.RowPermissionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Resource
    private AuthUserService authUserService;

    public List<ChartFieldCustomFilterDTO> getCustomFilters(List<DatasetTableField> fields, DatasetTable datasetTable, Long user) {
        List<ChartFieldCustomFilterDTO> customFilter = new ArrayList<>();
        Map<String, String> values = new HashMap<>();
        for (DatasetRowPermissions datasetRowPermissions : rowPermissions(datasetTable.getId(), user, values)) {
            ChartFieldCustomFilterDTO dto = new ChartFieldCustomFilterDTO();
            if (StringUtils.isEmpty(datasetRowPermissions.getDatasetFieldId())) {
                continue;
            }
            DatasetTableField field = getFieldById(fields, datasetRowPermissions.getDatasetFieldId());
            if (field == null) {
                continue;
            }
            dto.setField(field);
            dto.setId(field.getId());
            dto.setFilterType(datasetRowPermissions.getFilterType());
            if (datasetRowPermissions.getFilterType().equalsIgnoreCase("logic")) {
                if (StringUtils.isEmpty(datasetRowPermissions.getFilter())) {
                    continue;
                }
                List<ChartCustomFilterItemDTO> lists = new Gson().fromJson(datasetRowPermissions.getFilter(), new TypeToken<ArrayList<ChartCustomFilterItemDTO>>(){}.getType());
                lists.forEach(chartCustomFilterDTO -> {
                    chartCustomFilterDTO.setFieldId(field.getId());
                    if(datasetRowPermissions.getAuthTargetType().equalsIgnoreCase("sysParams")){
                        System.out.println(values.get(chartCustomFilterDTO.getValue()).toString());
                        chartCustomFilterDTO.setValue(values.get(chartCustomFilterDTO.getValue()).toString());
                    }
                });
                dto.setFilter(lists);
                dto.setLogic(datasetRowPermissions.getLogic());

                customFilter.add(dto);
            } else {
                if (StringUtils.isEmpty(datasetRowPermissions.getEnumCheckField())) {
                    continue;
                }
                if(datasetRowPermissions.getAuthTargetType().equalsIgnoreCase("sysParams")){
                    dto.setEnumCheckField(Arrays.asList(values.get(datasetRowPermissions.getEnumCheckField()).toString().split(",").clone()));
                }else {
                    dto.setEnumCheckField(Arrays.asList(datasetRowPermissions.getEnumCheckField().split(",").clone()));
                }
                customFilter.add(dto);
            }
        }
        return customFilter;
    }

    public List<DatasetTableField> filterColumnPermissons(List<DatasetTableField> fields, List<String> desensitizationList, String datasetTableId, Long user){
        List<DatasetTableField> result = new ArrayList<>();
        List<ColumnPermissionItem> allColumnPermissionItems = new ArrayList<>();
        for (DataSetColumnPermissionsDTO dataSetColumnPermissionsDTO : columnPermissions(datasetTableId, user)) {
            ColumnPermissions columnPermissions = new Gson().fromJson(dataSetColumnPermissionsDTO.getPermissions(), ColumnPermissions.class);
            if(!columnPermissions.getEnable()){continue;}
            allColumnPermissionItems.addAll(columnPermissions.getColumns().stream().filter(columnPermissionItem -> columnPermissionItem.getSelected()).collect(Collectors.toList()));
        }
        fields.forEach(field ->{
            List<String> permissions = allColumnPermissionItems.stream().filter(columnPermissionItem -> columnPermissionItem.getId().equalsIgnoreCase(field.getId())).map(ColumnPermissionItem::getOpt).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(permissions)){
                result.add(field);
            }else {
                if(!permissions.contains(ColumnPermissionConstants.Prohibit)){
                    desensitizationList.add(field.getDatalakeName());
                    result.add(field);
                }
            }
        });
        return result;
    }


    private List<DatasetRowPermissions> rowPermissions(String datasetId, Long userId, Map<String, String> values) {
        List<DatasetRowPermissions> datasetRowPermissions = new ArrayList<>();
        Map<String, RowPermissionService> beansOfType = SpringContextUtil.getApplicationContext().getBeansOfType((RowPermissionService.class));
        if (beansOfType.keySet().size() == 0) {
            return new ArrayList<>();
        }
        RowPermissionService rowPermissionService = SpringContextUtil.getBean(RowPermissionService.class);
        SysUserEntity userEntity = userId != null ? authUserService.getUserById(userId) : AuthUtils.getUser();
        List<Long> roleIds = new ArrayList<>();
        Long deptId = null;

        if (userEntity == null ) {
            return datasetRowPermissions;
        }
        if (userEntity.getIsAdmin()) {
            return datasetRowPermissions;
        }
        userId = userEntity.getUserId();
        deptId = userEntity.getDeptId();
        List<CurrentRoleDto> currentRoleDtos = authUserService.roleInfos(userId);
        roleIds = currentRoleDtos.stream().map(CurrentRoleDto::getId).collect(Collectors.toList());
        DataSetRowPermissionsDTO dataSetRowPermissionsDTO = new DataSetRowPermissionsDTO();
        dataSetRowPermissionsDTO.setDatasetId(datasetId);
        dataSetRowPermissionsDTO.setAuthTargetIds(Collections.singletonList(userId));
        dataSetRowPermissionsDTO.setAuthTargetType("user");
        datasetRowPermissions.addAll(rowPermissionService.searchRowPermissions(dataSetRowPermissionsDTO));
        dataSetRowPermissionsDTO.setAuthTargetIds(roleIds);
        dataSetRowPermissionsDTO.setAuthTargetType("role");
        datasetRowPermissions.addAll(rowPermissionService.searchRowPermissions(dataSetRowPermissionsDTO));
        dataSetRowPermissionsDTO.setAuthTargetIds(Collections.singletonList(deptId));
        dataSetRowPermissionsDTO.setAuthTargetType("dept");
        datasetRowPermissions.addAll(rowPermissionService.searchRowPermissions(dataSetRowPermissionsDTO));

        dataSetRowPermissionsDTO.setAuthTargetType("sysParams");
        dataSetRowPermissionsDTO.setAuthTargetIds(null);
        datasetRowPermissions.addAll(rowPermissionService.searchRowPermissions(dataSetRowPermissionsDTO));


        values.put("${sysParams.userId}", userEntity.getUsername());
        values.put("${sysParams.userName}", userEntity.getNickName());
        values.put("${sysParams.userEmail}", userEntity.getEmail());
        values.put("${sysParams.userSource}", userEntity.getFrom() == 0 ? "LOCAL" : "OIDC");
        values.put("${sysParams.dept}", userEntity.getDeptName());
        values.put("${sysParams.roles}", String.join(",", currentRoleDtos.stream().map(CurrentRoleDto::getName).collect(Collectors.toList())));
        return datasetRowPermissions;
    }

    private List<DataSetColumnPermissionsDTO> columnPermissions(String datasetId, Long userId) {
        List<DataSetColumnPermissionsDTO> datasetColumnPermissions = new ArrayList<>();
        Map<String, ColumnPermissionService> beansOfType = SpringContextUtil.getApplicationContext().getBeansOfType((ColumnPermissionService.class));
        if (beansOfType.keySet().size() == 0) {
            return new ArrayList<>();
        }
        ColumnPermissionService columnPermissionService = SpringContextUtil.getBean(ColumnPermissionService.class);
        SysUserEntity userEntity = userId != null ? authUserService.getUserById(userId) : AuthUtils.getUser();
        List<Long> roleIds = new ArrayList<>();
        Long deptId = null;

        if (userEntity == null ) {
            return datasetColumnPermissions;
        }
        if (userEntity.getIsAdmin()) {
            return datasetColumnPermissions;
        }
        userId = userEntity.getUserId();
        deptId = userEntity.getDeptId();
        roleIds = authUserService.roles(userId).stream().map(r -> Long.valueOf(r)).collect(Collectors.toList());
        DataSetColumnPermissionsDTO dataSetColumnPermissionsDTO = new DataSetColumnPermissionsDTO();
        dataSetColumnPermissionsDTO.setDatasetId(datasetId);
        dataSetColumnPermissionsDTO.setAuthTargetIds(Collections.singletonList(userId));
        dataSetColumnPermissionsDTO.setAuthTargetType("user");
        datasetColumnPermissions.addAll(columnPermissionService.searchPermissions(dataSetColumnPermissionsDTO));
        dataSetColumnPermissionsDTO.setAuthTargetIds(roleIds);
        dataSetColumnPermissionsDTO.setAuthTargetType("role");
        datasetColumnPermissions.addAll(columnPermissionService.searchPermissions(dataSetColumnPermissionsDTO));
        dataSetColumnPermissionsDTO.setAuthTargetIds(Collections.singletonList(deptId));
        dataSetColumnPermissionsDTO.setAuthTargetType("dept");
        datasetColumnPermissions.addAll(columnPermissionService.searchPermissions(dataSetColumnPermissionsDTO));
        return datasetColumnPermissions;
    }

    private DatasetTableField getFieldById(List<DatasetTableField> fields, String fieldId) {
        DatasetTableField field = null;
        for (DatasetTableField datasetTableField : fields) {
            if (fieldId.equalsIgnoreCase(datasetTableField.getId())) {
                field = datasetTableField;
            }
        }
        return field;
    }
}
