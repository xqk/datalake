package io.datalake.controller.sys.request;

import io.datalake.plugins.common.entity.XpackLdapUserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LdapAddRequest implements Serializable {

    private Long deptId;

    private List<Long> roleIds;

    private Long enabled;

    private List<XpackLdapUserEntity> users;
}
