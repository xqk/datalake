package io.datalake.service.sys;


import io.datalake.controller.sys.base.BaseGridRequest;
import io.datalake.controller.sys.response.RoleUserItem;
import io.datalake.ext.ExtSysRoleMapper;
import io.datalake.plugins.common.base.domain.SysRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysRoleService {

    @Resource
    private ExtSysRoleMapper extSysRoleMapper;

    public List<SysRole> query(BaseGridRequest request) {
        List<SysRole> result = extSysRoleMapper.query(request.convertExample());

        return result;
    }

    public List<RoleUserItem> allRoles() {
        return extSysRoleMapper.queryAll();
    }


}
