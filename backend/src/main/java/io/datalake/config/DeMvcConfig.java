package io.datalake.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static io.datalake.commons.constants.StaticResourceConstants.*;
import static io.datalake.commons.utils.StaticResourceUtils.ensureBoth;
import static io.datalake.commons.utils.StaticResourceUtils.ensureSuffix;

/**
 * Author: xqk
 * Date: 2022/4/24
 * Description:
 */
@Configuration
public class DeMvcConfig implements WebMvcConfigurer {
    /**
     * Configuring static resource path
     *
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String workDir = FILE_PROTOCOL + ensureSuffix(WORK_DIR, FILE_SEPARATOR);
        String uploadUrlPattern = ensureBoth(UPLOAD_URL_PREFIX, URL_SEPARATOR) + "**";
        registry.addResourceHandler(uploadUrlPattern)
                .addResourceLocations(workDir);

    }
}
