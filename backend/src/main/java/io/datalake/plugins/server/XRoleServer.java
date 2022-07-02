package io.datalake.plugins.server;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.datalake.auth.annotation.DeLog;
import io.datalake.auth.service.ExtAuthService;
import io.datalake.commons.constants.SysLogConstants;
import io.datalake.commons.utils.PageUtils;
import io.datalake.commons.utils.Pager;
import io.datalake.plugins.common.entity.XpackGridRequest;
import io.datalake.plugins.config.SpringContextUtil;
import io.datalake.plugins.xpack.role.dto.response.XpackRoleDto;
import io.datalake.plugins.xpack.role.dto.response.XpackRoleItemDto;
import io.datalake.plugins.xpack.role.service.RoleXpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
@Api(tags = "xpack：角色管理")
@RequestMapping("/plugin/role")
@RestController
public class XRoleServer {

    @Autowired
    private ExtAuthService extAuthService;

    @RequiresPermissions("role:add")
    @ApiOperation("新增角色")
    @PostMapping("/create")
    @DeLog(
        operatetype = SysLogConstants.OPERATE_TYPE.CREATE,
        sourcetype = SysLogConstants.SOURCE_TYPE.ROLE,
        value = "roleId"
    )
    public void create(@RequestBody XpackRoleDto role){
        RoleXpackService roleXpackService = SpringContextUtil.getBean(RoleXpackService.class);
        roleXpackService.save(role);
    }


    @RequiresPermissions("role:del")
    @ApiOperation("删除角色")
    @PostMapping("/delete/{roleId}")
    @DeLog(
        operatetype = SysLogConstants.OPERATE_TYPE.DELETE,
        sourcetype = SysLogConstants.SOURCE_TYPE.ROLE
    )
    public void delete(@PathVariable("roleId") Long roleId){
        RoleXpackService roleXpackService = SpringContextUtil.getBean(RoleXpackService.class);
        extAuthService.clearDeptResource(roleId);
        roleXpackService.delete(roleId);
    }


    @RequiresPermissions("role:edit")
    @ApiOperation("更新角色")
    @PostMapping("/update")
    @DeLog(
        operatetype = SysLogConstants.OPERATE_TYPE.MODIFY,
        sourcetype = SysLogConstants.SOURCE_TYPE.ROLE,
        value = "roleId"
    )
    public void update(@RequestBody XpackRoleDto role){
        RoleXpackService roleXpackService = SpringContextUtil.getBean(RoleXpackService.class);
        roleXpackService.update(role);
    }

    @RequiresPermissions("role:read")
    @ApiOperation("分页查询")
    @PostMapping("/roleGrid/{goPage}/{pageSize}")
    public Pager<List<XpackRoleDto>> roleGrid(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody XpackGridRequest request) {
        RoleXpackService roleXpackService = SpringContextUtil.getBean(RoleXpackService.class);
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        Pager<List<XpackRoleDto>> listPager = PageUtils.setPageInfo(page, roleXpackService.query(request));
        return listPager;
    }

    @ApiIgnore
    @PostMapping("/all")
    public List<XpackRoleItemDto> all() {
        RoleXpackService roleXpackService = SpringContextUtil.getBean(RoleXpackService.class);
        return roleXpackService.allRoles();
    }
}
