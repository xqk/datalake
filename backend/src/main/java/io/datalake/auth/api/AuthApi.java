package io.datalake.auth.api;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.datalake.auth.api.dto.CurrentUserDto;
import io.datalake.auth.api.dto.LoginDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Api(tags = "权限：权限管理")
@ApiSupport(order = 10)
@RequestMapping("/api/auth")
public interface AuthApi {

    @ApiOperation("登录")
    @PostMapping("/login")
    Object login(LoginDto loginDto) throws Exception;

    @ApiOperation("获取用户信息")
    @PostMapping("/userInfo")
    CurrentUserDto userInfo();

    @ApiOperation("是否使用初始密码")
    @PostMapping("/useInitPwd")
    Boolean useInitPwd();

    @ApiOperation("用户初始密码")
    @PostMapping("/defaultPwd")
    String defaultPwd();

    @ApiOperation("登出")
    @PostMapping("/logout")
    String logout();

    @ApiOperation("验证账号")
    @PostMapping("/validateName")
    Boolean validateName(Map<String, String> nameDto);

    @ApiOperation("是否开启ldap")
    @PostMapping("/isOpenLdap")
    boolean isOpenLdap();

    @ApiOperation("是否开启oidc")
    @PostMapping("/isOpenOidc")
    boolean isOpenOidc();

    @ApiIgnore
    @PostMapping("/isPluginLoaded")
    boolean isPluginLoaded();

    @ApiIgnore
    @GetMapping("/getPublicKey")
    String getPublicKey();

}
