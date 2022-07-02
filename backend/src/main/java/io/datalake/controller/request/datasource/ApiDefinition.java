package io.datalake.controller.request.datasource;

import io.datalake.plugins.common.base.domain.DatasetTableField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ApiDefinition {
    private String name;
    private String desc;
    private String url;
    private String method = "GET";
    private List<DatasetTableField> fields;
    private ApiDefinitionRequest request;
    private String dataPath;
    private String status;
    private List<Map<String,String>> datas = new ArrayList<>();
}
