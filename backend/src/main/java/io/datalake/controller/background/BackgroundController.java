package io.datalake.controller.background;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.controller.handler.annotation.I18n;
import io.datalake.plugins.common.base.domain.SysBackgroundImage;
import io.datalake.service.background.BackgroundService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Author: xqk
 * Date: 2022/2/22
 * Description:
 */
@Api(tags = "背景：背景边框")
@ApiSupport(order = 170)
@RestController
@RequestMapping("background")
public class BackgroundController {
    @Resource
    private BackgroundService backgroundService;

    @GetMapping("/findAll")
    @I18n
    public Map<String, List<SysBackgroundImage>> findAll(){
        return backgroundService.findAll();
    }


}
