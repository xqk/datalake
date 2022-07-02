package io.datalake.plugins.server;


import io.datalake.plugins.common.entity.XpackLdapUserEntity;
import io.datalake.plugins.config.SpringContextUtil;
import io.datalake.plugins.xpack.display.dto.response.SysSettingDto;
import io.datalake.plugins.xpack.ldap.dto.response.LdapInfo;
import io.datalake.plugins.xpack.ldap.service.LdapXpackService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
@ApiIgnore
@RequestMapping("/plugin/ldap")
@RestController
public class XLdapServer {


    @GetMapping("/info")
    public LdapInfo getLdapInfo() {
        LdapXpackService ldapXpackService = SpringContextUtil.getBean(LdapXpackService.class);
        return ldapXpackService.info();
    }

    @RequiresPermissions("sysparam:read")
    @PostMapping("/save")
    public void save(@RequestBody List<SysSettingDto> settings) {
        LdapXpackService ldapXpackService = SpringContextUtil.getBean(LdapXpackService.class);
        ldapXpackService.save(settings);
    }

    @PostMapping("/testConn")
    public void testConn() {
        LdapXpackService ldapXpackService = SpringContextUtil.getBean(LdapXpackService.class);
        try {
            ldapXpackService.testConn();
        }catch(Exception e) {
            throw new RuntimeException(e);
        } 
    }

    @PostMapping("/users")
    public List<XpackLdapUserEntity> users() {
        LdapXpackService ldapXpackService = SpringContextUtil.getBean(LdapXpackService.class);
        return ldapXpackService.users();
    }
}
