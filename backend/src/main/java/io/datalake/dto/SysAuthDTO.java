package io.datalake.dto;

import io.datalake.plugins.common.base.domain.SysAuth;
import io.datalake.plugins.common.base.domain.SysAuthDetail;
import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2021-05-12
 * Description:
 */
@Data
public class SysAuthDTO extends SysAuth {

     private List<SysAuthDetail> sysAuthDetails;
}
