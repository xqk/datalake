package io.datalake.config.properties;

import lombok.Data;

/**
 * Author: xqk
 * Date: 2022/4/24
 * Description:
 */
@Data
public class StaticResourceProperties {

    /**
     * Upload prefix.
     */
    private String uploadUrlPrefix = "static-resource";

}
