package io.datalake.controller.request.authModel;

import io.datalake.dto.authModel.VAuthModelDTO;
import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2021/11/24
 * Description:
 */
@Data
public class VAuthModelRequest extends VAuthModelDTO {

    private String userId;

    private String privileges;

    private Integer datasetMode;

    private boolean clearEmptyDir;

    private List<String> modelInnerTypeArray;

    private List<String> pids;

}
