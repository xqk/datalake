package io.datalake.plugins.server;

import io.datalake.auth.entity.SysUserEntity;
import io.datalake.auth.entity.TokenInfo;
import io.datalake.auth.service.AuthUserService;
import io.datalake.auth.util.JWTUtils;
import io.datalake.commons.exception.DEException;
import io.datalake.commons.utils.CodingUtil;
import io.datalake.commons.utils.LogUtil;
import io.datalake.commons.utils.ServletUtils;
import io.datalake.plugins.config.SpringContextUtil;
import io.datalake.plugins.xpack.display.dto.response.SysSettingDto;
import io.datalake.plugins.xpack.oidc.dto.SSOToken;
import io.datalake.plugins.xpack.oidc.dto.SSOUserInfo;
import io.datalake.plugins.xpack.oidc.service.OidcXpackService;
import io.datalake.service.sys.SysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@RequestMapping("/sso")
@Controller
public class SSOServer {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/callBack")
    public ModelAndView callBack(@RequestParam("code") String code, @RequestParam("state") String state) {
        ModelAndView modelAndView = new ModelAndView("redirect:/"); 
        HttpServletResponse response = ServletUtils.response();   
        OidcXpackService oidcXpackService = null;
        String idToken = null;
        try {
            Map<String, OidcXpackService> beansOfType = SpringContextUtil.getApplicationContext().getBeansOfType((OidcXpackService.class));
            if(beansOfType.keySet().size() == 0) {
                DEException.throwException("缺少oidc插件");
            }
            oidcXpackService = SpringContextUtil.getBean(OidcXpackService.class);
            Boolean suuportOIDC = oidcXpackService.isSuuportOIDC();
            if (!suuportOIDC) {
                DEException.throwException("未开启oidc");
            }
            Map<String, String> config = config(oidcXpackService);            
            SSOToken ssoToken = oidcXpackService.requestSsoToken(config, code, state);
            idToken = ssoToken.getIdToken();
            Cookie cookie_id_token = new Cookie("IdToken", ssoToken.getIdToken());cookie_id_token.setPath("/");
            response.addCookie(cookie_id_token);
            
            SSOUserInfo ssoUserInfo = oidcXpackService.requestUserInfo(config, ssoToken.getAccessToken());

            
            SysUserEntity sysUserEntity = authUserService.getUserBySub(ssoUserInfo.getSub());
            if(null == sysUserEntity){
                sysUserService.validateExistUser(ssoUserInfo.getUsername(), ssoUserInfo.getNickName(), ssoUserInfo.getEmail());
                sysUserService.saveOIDCUser(ssoUserInfo);
                sysUserEntity = authUserService.getUserBySub(ssoUserInfo.getSub());
            }
            TokenInfo tokenInfo = TokenInfo.builder().userId(sysUserEntity.getUserId()).username(sysUserEntity.getUsername()).build();
            String realPwd = CodingUtil.md5(sysUserService.defaultPWD());
            String token = JWTUtils.sign(tokenInfo, realPwd);
            ServletUtils.setToken(token);
            
            
            Cookie cookie_token = new Cookie("Authorization", token);cookie_token.setPath("/");
           
            Cookie cookie_ac_token = new Cookie("AccessToken", ssoToken.getAccessToken());cookie_ac_token.setPath("/");

            response.addCookie(cookie_token);
            
            response.addCookie(cookie_ac_token);
        }catch(Exception e) {
            
            String msg = e.getMessage();
            if (null != e.getCause()) {
                msg = e.getCause().getMessage();
            }
            try {
                msg = URLEncoder.encode(msg, "UTF-8");
                LogUtil.error(e);
                Cookie cookie_error = new Cookie("OidcError", msg);
                cookie_error.setPath("/");
                response.addCookie(cookie_error);
                if (ObjectUtils.isNotEmpty(oidcXpackService) && ObjectUtils.isNotEmpty(idToken)) {
                    oidcXpackService.logout(idToken);
                }
                return modelAndView;
            } catch (UnsupportedEncodingException e1) {
                e.printStackTrace();
            }
            
            
        } 
        return modelAndView;
    }
    private Map<String, String> config(OidcXpackService oidcXpackService) {
        List<SysSettingDto> sysSettingDtos = oidcXpackService.oidcSettings();
        Map<String, String> config = sysSettingDtos.stream().collect(HashMap::new,(m, v)->m.put(v.getParamKey(), v.getParamValue()), HashMap::putAll);

        return config;
    }
    

    
    
}
