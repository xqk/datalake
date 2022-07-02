package io.datalake.auth.service;

import groovy.lang.Lazy;
import io.datalake.auth.api.dto.CurrentRoleDto;
import io.datalake.auth.api.dto.CurrentUserDto;
import io.datalake.auth.entity.SysUserEntity;
import io.datalake.commons.utils.BeanUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProxyAuthService {

    @Autowired
    @Lazy
    private AuthUserService authUserService;

    public CurrentUserDto queryCacheUserDto(Long userId) {

        SysUserEntity user = authUserService.getUserById(userId);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }
        if (user.getEnabled() == 0) {
            throw new AuthenticationException("User is valid!");
        }
        // 使用缓存
        List<CurrentRoleDto> currentRoleDtos = authUserService.roleInfos(user.getUserId());
        // 使用缓存
        List<String> permissions = authUserService.permissions(user.getUserId());
        CurrentUserDto currentUserDto = BeanUtils.copyBean(new CurrentUserDto(), user);
        currentUserDto.setRoles(currentRoleDtos);
        currentUserDto.setPermissions(permissions);
        return currentUserDto;
    }

}
