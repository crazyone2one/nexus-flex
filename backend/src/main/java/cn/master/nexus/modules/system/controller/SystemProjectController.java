package cn.master.nexus.modules.system.controller;

import cn.master.nexus.common.annotation.Log;
import cn.master.nexus.common.constants.OperationLogType;
import cn.master.nexus.common.util.SessionUtils;
import cn.master.nexus.common.validation.groups.Created;
import cn.master.nexus.common.validation.groups.Updated;
import cn.master.nexus.modules.log.service.SystemProjectLogService;
import cn.master.nexus.modules.system.dto.AddProjectRequest;
import cn.master.nexus.modules.system.dto.ProjectDTO;
import cn.master.nexus.modules.system.dto.UpdateProjectNameRequest;
import cn.master.nexus.modules.system.dto.UpdateProjectRequest;
import cn.master.nexus.modules.system.dto.request.ProjectRequest;
import cn.master.nexus.modules.system.entity.Project;
import cn.master.nexus.modules.system.service.SystemProjectService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目 控制层。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统设置-系统-组织与项目-项目")
@RequestMapping("/system/project")
public class SystemProjectController {

    private final SystemProjectService systemProjectService;

    @PostMapping("save")
    @Operation(summary = "系统设置-系统-组织与项目-项目-创建项目")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ+ADD')")
    @Log(type = OperationLogType.ADD, expression = "#msClass.addLog(#request)", msClass = SystemProjectLogService.class)
    public ProjectDTO save(@RequestBody @Parameter(description = "项目") @Validated({Created.class}) AddProjectRequest request) {
        return systemProjectService.add(request, SessionUtils.getUserName());
    }

    @GetMapping("remove/{id}")
    @Operation(description = "系统设置-系统-组织与项目-项目-删除")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ+DELETE')")
    @Log(type = OperationLogType.DELETE, expression = "#msClass.deleteLog(#id)", msClass = SystemProjectLogService.class)
    public int remove(@PathVariable @Parameter(description = "项目主键") String id) {
        return systemProjectService.delete(id, SessionUtils.getUserName());
    }

    @PostMapping("update")
    @Operation(description = "系统设置-系统-组织与项目-项目-编辑")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ+UPDATE')")
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.updateLog(#request)", msClass = SystemProjectLogService.class)
    public ProjectDTO update(@RequestBody @Parameter(description = "项目主键") @Validated({Updated.class}) UpdateProjectRequest request) {
        return systemProjectService.update(request, SessionUtils.getUserName());
    }

    /**
     * 查询所有项目。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @Operation(description = "查询所有项目")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ')")
    public List<Project> list() {
        return systemProjectService.list();
    }

    /**
     * 根据主键获取项目。
     *
     * @param id 项目主键
     * @return 项目详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(description = "根据主键获取项目")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ')")
    public Project getInfo(@PathVariable @Parameter(description = "项目主键") String id) {
        return systemProjectService.get(id);
    }

    @PostMapping("page")
    @Operation(description = "系统设置-系统-组织与项目-项目-获取项目列表")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ')")
    public Page<ProjectDTO> page(@Parameter(description = "分页信息") @Validated ProjectRequest request) {
        return systemProjectService.page(request);
    }

    @GetMapping("/enable/{id}")
    @Operation(description = "系统设置-系统-组织与项目-项目-启用")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ+UPDATE')")
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.updateLog(#request)", msClass = SystemProjectLogService.class)
    public void enable(@PathVariable String id) {
        systemProjectService.enable(id, SessionUtils.getUserName());
    }

    @GetMapping("/disable/{id}")
    @Operation(description = "系统设置-系统-组织与项目-项目-禁用")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ+UPDATE')")
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.updateLog(#request)", msClass = SystemProjectLogService.class)
    public void disable(@PathVariable String id) {
        systemProjectService.disable(id, SessionUtils.getUserName());
    }

    @PostMapping("/rename")
    @Operation(description = "系统设置-系统-组织与项目-项目-禁用")
    @PreAuthorize("hasPermission('SYSTEM_ORGANIZATION_PROJECT','READ+UPDATE')")
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.updateLog(#request)", msClass = SystemProjectLogService.class)
    public void rename(@RequestBody @Validated({Updated.class}) UpdateProjectNameRequest request) {
        systemProjectService.rename(request, SessionUtils.getUserName());
    }
}
