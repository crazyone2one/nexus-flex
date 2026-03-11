package cn.master.nexus.modules.system.controller;

import cn.master.nexus.modules.system.entity.SystemParameter;
import cn.master.nexus.modules.system.service.SystemParameterService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统参数 控制层。
 *
 * @author 11's papa
 * @since 2026-03-09
 */
@RestController
@Tag(name = "系统参数接口")
@RequiredArgsConstructor
@RequestMapping("/system/parameter")
public class SystemParameterController {

    private final SystemParameterService systemParameterService;

    /**
     * 保存系统参数。
     *
     * @param systemParameter 系统参数
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    @Operation(description = "保存系统参数")
    @PreAuthorize("hasPermission('SYSTEM_PARAMETER_SETTING_BASE','READ+UPDATE')")
    public boolean save(@RequestBody @Parameter(description = "系统参数") SystemParameter systemParameter) {
        return systemParameterService.save(systemParameter);
    }

    /**
     * 根据主键删除系统参数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Operation(description = "根据主键删除系统参数")
    public boolean remove(@PathVariable @Parameter(description = "系统参数主键") String id) {
        return systemParameterService.removeById(id);
    }

    /**
     * 根据主键更新系统参数。
     *
     * @param systemParameter 系统参数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(description = "根据主键更新系统参数")
    public boolean update(@RequestBody @Parameter(description = "系统参数主键") SystemParameter systemParameter) {
        return systemParameterService.updateById(systemParameter);
    }

    /**
     * 查询所有系统参数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @Operation(description = "查询所有系统参数")
    public List<SystemParameter> list() {
        return systemParameterService.list();
    }

    /**
     * 根据主键获取系统参数。
     *
     * @param id 系统参数主键
     * @return 系统参数详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(description = "根据主键获取系统参数")
    public SystemParameter getInfo(@PathVariable @Parameter(description = "系统参数主键") String id) {
        return systemParameterService.getById(id);
    }

    /**
     * 分页查询系统参数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(description = "分页查询系统参数")
    public Page<SystemParameter> page(@Parameter(description = "分页信息") Page<SystemParameter> page) {
        return systemParameterService.page(page);
    }

}
