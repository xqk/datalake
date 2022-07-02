package io.datalake.plugins.server;

import io.datalake.commons.exception.DEException;
import io.datalake.commons.utils.LogUtil;
import io.datalake.i18n.Translator;
import io.datalake.plugins.config.SpringContextUtil;
import io.datalake.plugins.xpack.theme.dto.ThemeDto;
import io.datalake.plugins.xpack.theme.dto.ThemeItem;
import io.datalake.plugins.xpack.theme.dto.ThemeRequest;
import io.datalake.plugins.xpack.theme.service.ThemeXpackService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@RequestMapping("/plugin/theme")
@RestController
public class ThemeServer {

    @PostMapping("/themes")
    public List<ThemeDto> themes() {

        ThemeXpackService themeXpackService = SpringContextUtil.getBean(ThemeXpackService.class);
        return themeXpackService.themes();
    }

    @PostMapping("/items/{themeId}")
    public List<ThemeItem> themeItems(@PathVariable("themeId") int themeId) {
        ThemeXpackService themeXpackService = SpringContextUtil.getBean(ThemeXpackService.class);
        return themeXpackService.queryItems(themeId);
    }

    @RequiresPermissions("sysparam:read")
    @PostMapping("/save")
    public void save(@RequestPart("request") ThemeRequest request,
            @RequestPart(value = "file", required = false) MultipartFile bodyFile) {
        ThemeXpackService themeXpackService = SpringContextUtil.getBean(ThemeXpackService.class);
        try {
            themeXpackService.save(request, bodyFile);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            if (ObjectUtils.isNotEmpty(e.getMessage()) && e.getMessage().indexOf("theme_name_repeat") != -1) {
                DEException.throwException(Translator.get("theme_name_repeat"));
            } else if (ObjectUtils.isNotEmpty(e.getMessage()) && e.getMessage().indexOf("theme_name_empty") != -1) {
                DEException.throwException(Translator.get("theme_name_empty"));
            } else {
                DEException.throwException(e);
            }
        }

    }

    @RequiresPermissions("sysparam:read")
    @PostMapping("/delete/{themeId}")
    public void delete(@PathVariable("themeId") int themeId) {
        ThemeXpackService themeXpackService = SpringContextUtil.getBean(ThemeXpackService.class);
        themeXpackService.deleteTheme(themeId);
    }

}
