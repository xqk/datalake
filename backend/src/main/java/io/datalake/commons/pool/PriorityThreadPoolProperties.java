package io.datalake.commons.pool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "detask", ignoreInvalidFields = true)
@Data
@Component
public class PriorityThreadPoolProperties {

    private int corePoolSize = 2;
    private int maximumPoolSize = 100;
    private int keepAliveTime = 60;

}
