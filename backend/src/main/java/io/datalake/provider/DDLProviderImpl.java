package io.datalake.provider;

import io.datalake.plugins.common.base.domain.DatasetTableField;
import io.datalake.plugins.common.base.domain.Datasource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DDLProviderImpl extends DDLProvider {
    @Override
    public String createView(String name, String viewSQL) {
        return null;
    }

    @Override
    public String dropTable(String name) {
        return null;
    }

    @Override
    public String dropView(String name) {
        return null;
    }

    @Override
    public String replaceTable(String name) {
        return null;
    }

    @Override
    public String createTableSql(String name, List<DatasetTableField> datasetTableFields, Datasource engine) {
        return null;
    }

    @Override
    public String insertSql(String name, List<String[]> dataList, int page, int pageNumber) {
        String insertSql = "INSERT INTO TABLE_NAME VALUES ".replace("TABLE_NAME", name);
        StringBuffer values = new StringBuffer();

        Integer realSize = page * pageNumber < dataList.size() ? page * pageNumber : dataList.size();
        for (String[] strings : dataList.subList((page - 1) * pageNumber, realSize)) {
            String[] strings1 = new String[strings.length];
            for(int i=0; i< strings.length;i++){
                strings1[i] = strings[i].replace("'","\\'");
            }
            values.append("('").append(UUID.randomUUID())
                    .append("','" ).append(String.join("','", Arrays.asList(strings1)))
                    .append("'),");
        }
        return insertSql + values.substring(0, values.length() - 1);
    }


}
