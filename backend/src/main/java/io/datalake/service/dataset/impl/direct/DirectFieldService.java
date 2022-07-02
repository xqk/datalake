package io.datalake.service.dataset.impl.direct;

import com.google.gson.Gson;
import io.datalake.commons.constants.ColumnPermissionConstants;
import io.datalake.commons.exception.DEException;
import io.datalake.commons.model.BaseTreeNode;
import io.datalake.commons.utils.TreeUtils;
import io.datalake.dto.dataset.DataSetTableUnionDTO;
import io.datalake.dto.dataset.DataTableInfoDTO;
import io.datalake.i18n.Translator;
import io.datalake.plugins.common.base.domain.DatasetTable;
import io.datalake.plugins.common.base.domain.DatasetTableField;
import io.datalake.plugins.common.base.domain.Datasource;
import io.datalake.plugins.common.dto.chart.ChartFieldCustomFilterDTO;
import io.datalake.plugins.common.request.datasource.DatasourceRequest;
import io.datalake.plugins.datasource.provider.Provider;
import io.datalake.plugins.datasource.query.QueryProvider;
import io.datalake.provider.ProviderFactory;
import io.datalake.service.dataset.*;
import io.datalake.service.datasource.DatasourceService;
import io.datalake.service.engine.EngineService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service("directDataSetFieldService")
public class DirectFieldService implements DataSetFieldService {
    @Resource
    private DataSetTableFieldsService dataSetTableFieldsService;
    @Resource
    private DataSetTableService dataSetTableService;
    @Resource
    private DatasourceService datasourceService;
    @Resource
    private DataSetTableUnionService dataSetTableUnionService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private EngineService engineService;

    @Override
    public List<Object> fieldValues(String fieldId, Long userId, Boolean userPermissions, Boolean rowAndColumnMgm) throws Exception {
        List<String> filedIds = new ArrayList<>();
        filedIds.add(fieldId);
        return fieldValues(filedIds, userId, userPermissions, false, rowAndColumnMgm);
    }

    @Override
    public List<Object> fieldValues(List<String> fieldIds, Long userId, Boolean userPermissions, Boolean needMapping, Boolean rowAndColumnMgm) throws Exception {
        String fieldId = fieldIds.get(0);
        DatasetTableField field = dataSetTableFieldsService.selectByPrimaryKey(fieldId);
        if (field == null || StringUtils.isEmpty(field.getTableId())) return null;

        DatasetTable datasetTable = dataSetTableService.get(field.getTableId());
        if (ObjectUtils.isEmpty(datasetTable) || StringUtils.isEmpty(datasetTable.getName())) return null;

        DatasetTableField datasetTableField = DatasetTableField.builder().tableId(field.getTableId()).checked(Boolean.TRUE).build();
        List<DatasetTableField> fields = dataSetTableFieldsService.list(datasetTableField);
        final List<String> allTableFieldIds = fields.stream().map(DatasetTableField::getId).collect(Collectors.toList());
        boolean multi = fieldIds.stream().anyMatch(item -> !allTableFieldIds.contains(item));
        if (multi && needMapping) {
            DEException.throwException("Cross multiple dataset is not supported");
        }

        List<DatasetTableField> permissionFields = fields;
        List<ChartFieldCustomFilterDTO> customFilter = new ArrayList<>();
        if (userPermissions) {
            //列权限
            List<String> desensitizationList = new ArrayList<>();
            fields = permissionService.filterColumnPermissons(fields, desensitizationList, datasetTable.getId(), userId);
            Map<String, DatasetTableField> fieldMap = fields.stream().collect(Collectors.toMap(DatasetTableField::getId, node -> node));
            permissionFields = fieldIds.stream().map(fieldMap::get).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(permissionFields) || permissionFields.get(0) == null) {
                return new ArrayList<>();
            }
            if (CollectionUtils.isNotEmpty(desensitizationList) && desensitizationList.contains(field.getDatalakeName())) {
                List<Object> results = new ArrayList<>();
                results.add(ColumnPermissionConstants.Desensitization_desc);
                return results;
            }
            //行权限
            customFilter = permissionService.getCustomFilters(fields, datasetTable, userId);
        }
        if (rowAndColumnMgm) {
            Map<String, DatasetTableField> fieldMap = fields.stream().collect(Collectors.toMap(DatasetTableField::getId, node -> node));
            permissionFields = fieldIds.stream().map(fieldMap::get).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(permissionFields)) {
                return new ArrayList<>();
            }
        }

        DatasourceRequest datasourceRequest = new DatasourceRequest();
        Provider datasourceProvider = null;
        if (datasetTable.getMode() == 0) {// 直连
            if (StringUtils.isEmpty(datasetTable.getDataSourceId())) return null;
            Datasource ds = datasourceService.get(datasetTable.getDataSourceId());
            if (StringUtils.isNotEmpty(ds.getStatus()) && ds.getStatus().equalsIgnoreCase("Error")) {
                throw new Exception(Translator.get("i18n_invalid_ds"));
            }
            datasourceProvider = ProviderFactory.getProvider(ds.getType());
            datasourceRequest = new DatasourceRequest();
            datasourceRequest.setDatasource(ds);
            DataTableInfoDTO dataTableInfoDTO = new Gson().fromJson(datasetTable.getInfo(), DataTableInfoDTO.class);
            QueryProvider qp = ProviderFactory.getQueryProvider(ds.getType());
            if (StringUtils.equalsIgnoreCase(datasetTable.getType(), "db")) {
                datasourceRequest.setTable(dataTableInfoDTO.getTable());
                datasourceRequest.setQuery(qp.createQuerySQL(dataTableInfoDTO.getTable(), permissionFields, true, ds, customFilter));
            } else if (StringUtils.equalsIgnoreCase(datasetTable.getType(), "sql")) {
                datasourceRequest.setQuery(qp.createQuerySQLAsTmp(dataTableInfoDTO.getSql(), permissionFields, true, customFilter));
            } else if (StringUtils.equalsIgnoreCase(datasetTable.getType(), "custom")) {
                DataTableInfoDTO dt = new Gson().fromJson(datasetTable.getInfo(), DataTableInfoDTO.class);
                List<DataSetTableUnionDTO> listUnion = dataSetTableUnionService.listByTableId(dt.getList().get(0).getTableId());
                String sql = dataSetTableService.getCustomSQLDatasource(dt, listUnion, ds);
                datasourceRequest.setQuery(qp.createQuerySQLAsTmp(sql, permissionFields, true, customFilter));
            } else if (StringUtils.equalsIgnoreCase(datasetTable.getType(), "union")) {
                DataTableInfoDTO dt = new Gson().fromJson(datasetTable.getInfo(), DataTableInfoDTO.class);
                String sql = (String) dataSetTableService.getUnionSQLDatasource(dt, ds).get("sql");
                datasourceRequest.setQuery(qp.createQuerySQLAsTmp(sql, permissionFields, true, customFilter));
            }
        } else if (datasetTable.getMode() == 1) {// 抽取
            // 连接doris，构建doris数据源查询
            Datasource ds = engineService.getDeEngine();
            datasourceProvider = ProviderFactory.getProvider(ds.getType());
            datasourceRequest = new DatasourceRequest();
            datasourceRequest.setDatasource(ds);
            String tableName = "ds_" + datasetTable.getId().replaceAll("-", "_");
            datasourceRequest.setTable(tableName);
            QueryProvider qp = ProviderFactory.getQueryProvider(ds.getType());
            datasourceRequest.setQuery(qp.createQuerySQL(tableName, permissionFields, true, null, customFilter));
        }

        List<String[]> rows = datasourceProvider.getData(datasourceRequest);
        if (!needMapping) {
            List<Object> results = rows.stream().map(row -> row[0]).distinct().collect(Collectors.toList());
            return results;
        }
        Set<String> pkSet = new HashSet<>();

        List<BaseTreeNode> treeNodes = rows.stream().map(row -> buildTreeNode(row, pkSet)).flatMap(Collection::stream).collect(Collectors.toList());
        List tree = TreeUtils.mergeDuplicateTree(treeNodes, TreeUtils.DEFAULT_ROOT);
        return tree;

    }

    private List<BaseTreeNode> buildTreeNode(String[] row, Set<String> pkSet) {
        List<BaseTreeNode> nodes = new ArrayList<>();
        List<String> parentPkList = new ArrayList<>();
        for (int i = 0; i < row.length; i++) {
            String text = row[i];

            parentPkList.add(text);
            String val = parentPkList.stream().collect(Collectors.joining(TreeUtils.SEPARATOR));
            String parentVal = i == 0 ? TreeUtils.DEFAULT_ROOT : row[i - 1];
            String pk = parentPkList.stream().collect(Collectors.joining(TreeUtils.SEPARATOR));
            if (pkSet.contains(pk)) continue;
            pkSet.add(pk);
            BaseTreeNode node = new BaseTreeNode(val, parentVal, text, pk + TreeUtils.SEPARATOR + i);
            nodes.add(node);
        }
        return nodes;

    }

}
