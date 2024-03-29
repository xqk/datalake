package io.datalake.controller.authModel;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.controller.request.authModel.VAuthModelRequest;
import io.datalake.dto.authModel.VAuthModelDTO;
import io.datalake.service.authModel.VAuthModelService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: xqk
 * Date: 2021/11/5
 * Description:
 */
@Api(tags = "授权树：授权树模型")
@ApiSupport(order = 80)
@RestController
@RequestMapping("authModel")
public class VAuthModelController {

    @Resource
    private VAuthModelService vAuthModelService;

    @PostMapping("/queryAuthModel")
    public List<VAuthModelDTO> queryAuthModel(@RequestBody VAuthModelRequest request){
        return vAuthModelService.queryAuthModel(request);
    }

}
