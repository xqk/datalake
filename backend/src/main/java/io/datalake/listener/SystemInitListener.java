package io.datalake.listener;

import io.datalake.ext.DEVersionMapper;
import io.datalake.plugins.common.base.domain.DatalakeCodeVersion;
import io.datalake.plugins.common.base.mapper.DatalakeCodeVersionMapper;
import io.datalake.plugins.loader.ClassloaderResponsity;
import io.datalake.service.panel.PanelGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Order(value = 1)
public class SystemInitListener implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger logger = LoggerFactory.getLogger(ClassloaderResponsity.class);

    @Resource
    private DEVersionMapper versionMapper;
    @Resource
    private PanelGroupService panelGroupService;
    @Resource
    private DatalakeCodeVersionMapper codeVersionMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("=====initSystem from code [Start]=====");
        logger.info("=====initSystem from code [Start]=====");
        Integer dataeseVersion = versionMapper.lastSuccessDataLakeVersion();
        Integer dataeseCodeVersion = versionMapper.lastDataLakeCodeVersion();

        // v1.8 初始化程序 1 是1.8 初始化程序的执行记录 32 是1.8版本flayway的执行记录
        if(dataeseCodeVersion<1 && dataeseVersion>=32){
            DatalakeCodeVersion codeVersion = new DatalakeCodeVersion();
            codeVersion.setDescription("v1.8 初始化");
            codeVersion.setInstalledOn(new Date());
            codeVersion.setInstalledRank(1);
            try{
                panelGroupService.sysInit1HistoryPanel();
                codeVersion.setSuccess(true);
            }catch (Exception e){
                codeVersion.setSuccess(false);
                e.printStackTrace();
                logger.error("===>1.8程序初始化失败：",e);
            }
            codeVersionMapper.insert(codeVersion);
        }
        logger.info("=====initSystem from code [End]=====");
        System.out.println("=====initSystem from code [End]=====");


    }
}
