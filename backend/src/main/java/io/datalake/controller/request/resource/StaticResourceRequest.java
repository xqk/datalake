package io.datalake.controller.request.resource;

import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2022/4/28
 * Description:
 */
@Data
public class StaticResourceRequest {

    private List<String> resourcePathList;

}
