package io.datalake.controller.request;

import io.datalake.plugins.common.base.domain.Datasource;
import lombok.Data;

/**
 * Author: xqk
 * Date: 2021-05-18
 * Description:
 */
@Data
public class DatasourceUnionRequest  extends Datasource {

    private String userId;

    private String sort;

}
