package io.datalake.auth.api.dto;

import io.datalake.auth.entity.SysUserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CurrentUserDto extends SysUserEntity implements Serializable {

    @ApiModelProperty("角色集合")
    private List<CurrentRoleDto> roles;

    @ApiModelProperty("权限集合")
    private List<String> permissions;
}
