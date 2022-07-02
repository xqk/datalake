package io.datalake.plugins.server.view;

import io.datalake.plugins.config.SpringContextUtil;
import io.datalake.plugins.view.entity.PluginViewType;
import io.datalake.plugins.view.service.ViewPluginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@ApiIgnore
@RequestMapping("/plugin/view")
@RestController
public class PluginViewServer {

    @PostMapping("/types")
    public List<PluginViewType> types() {
        List<PluginViewType> result = new ArrayList<>();
        Map<String, ViewPluginService> beanMap = SpringContextUtil.getApplicationContext()
                .getBeansOfType(ViewPluginService.class);
        if (beanMap.keySet().size() == 0) {
            return result;
        }
        for (Entry<String, ViewPluginService> entry : beanMap.entrySet()) {
            result.add(entry.getValue().viewType());
        }
        return result;
    }
}
