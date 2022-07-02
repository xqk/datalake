package io.datalake.dto;

import lombok.Data;

/**
 * Author: xqk
 * Date: 2021-05-12
 * Description:
 */
@Data
public class BaseAuthDetail {

    private Integer privilegeType;//类型

    private Integer privilegeValue;//值 1 不可用 2 可用
}
