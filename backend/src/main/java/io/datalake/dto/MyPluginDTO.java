package io.datalake.dto;

import io.datalake.plugins.common.base.domain.MyPlugin;
import lombok.Data;

@Data
public class MyPluginDTO extends MyPlugin {
    private String require = "1.9.0";
}
