package io.datalake.auth.service;

import io.datalake.auth.api.dto.CurrentRoleDto;
import io.datalake.auth.entity.SysUserEntity;

import java.util.List;

public interface AuthUserService {


    SysUserEntity getUserById(Long userId);

    SysUserEntity getUserByName(String username);

    SysUserEntity getLdapUserByName(String username);

    SysUserEntity getUserBySub(String sub);

    List<String> roles(Long userId);

    List<String> permissions(Long userId);

    List<CurrentRoleDto> roleInfos(Long userId);

    void clearCache(Long userId);

    boolean supportLdap();

    Boolean supportOidc();

    Boolean pluginLoaded();


}
