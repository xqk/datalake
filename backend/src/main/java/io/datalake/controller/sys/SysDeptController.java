package io.datalake.controller.sys;

import io.datalake.commons.utils.BeanUtils;
import io.datalake.controller.ResultHolder;
import io.datalake.controller.sys.base.BaseGridRequest;
import io.datalake.controller.sys.response.DeptNodeResponse;
import io.datalake.controller.sys.response.DeptTreeNode;
import io.datalake.plugins.common.base.domain.SysDept;
import io.datalake.service.sys.DeptService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@RequestMapping("/api/dept")
public class SysDeptController extends ResultHolder {

    @Autowired
    private DeptService deptService;

    @PostMapping("/childNodes/{pid}")
    public List<DeptNodeResponse> childNodes(@PathVariable("pid") Long pid){
        List<SysDept> nodes = deptService.nodesByPid(pid);
        List<DeptNodeResponse> nodeResponses = nodes.stream().map(node -> {
            DeptNodeResponse deptNodeResponse = BeanUtils.copyBean(new DeptNodeResponse(), node);
            deptNodeResponse.setHasChildren(node.getSubCount() > 0);
            deptNodeResponse.setLeaf(node.getSubCount() == 0);
            deptNodeResponse.setTop(node.getPid() == deptService.DEPT_ROOT_PID);
            return deptNodeResponse;
        }).collect(Collectors.toList());
        return nodeResponses;
    }

    @PostMapping("/search")
    public List<DeptNodeResponse> search(@RequestBody BaseGridRequest request){
        List<SysDept> nodes = deptService.nodesTreeByCondition(request);
        List<DeptNodeResponse> nodeResponses = nodes.stream().map(node -> {
            DeptNodeResponse deptNodeResponse = BeanUtils.copyBean(new DeptNodeResponse(), node);
            deptNodeResponse.setHasChildren(node.getSubCount() > 0);
            deptNodeResponse.setLeaf(node.getSubCount() == 0);
            deptNodeResponse.setTop(node.getPid() == deptService.DEPT_ROOT_PID);
            return deptNodeResponse;
        }).collect(Collectors.toList());
        return nodeResponses;
    }



    @PostMapping("/nodesByDeptId/{deptId}")
    public List<DeptTreeNode> nodesByDeptId(@PathVariable("deptId") Long deptId){
        return deptService.searchTree(deptId);
    }

}
