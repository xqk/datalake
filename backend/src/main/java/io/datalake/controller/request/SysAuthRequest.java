package io.datalake.controller.request;

import io.datalake.plugins.common.base.domain.SysAuth;
import io.datalake.plugins.common.base.domain.SysAuthDetail;
import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2021-05-11
 * Description:
 */
@Data
public class SysAuthRequest extends SysAuth {

    private List<String> authSources;

    private List<String> authTargets;

    private SysAuthDetail authDetail;


}
