package cn.master.nexus.modules.system.dto;

import cn.master.nexus.common.validation.groups.Created;
import cn.master.nexus.common.validation.groups.Updated;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author : 11's papa
 * @since : 2026/3/9, 星期一
 **/
public record UpdateProjectNameRequest(
        @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{project.id.not_blank}", groups = {Updated.class})
        @Size(min = 1, max = 50, message = "{project.id.length_range}", groups = {Updated.class})
        String id,
        @Schema(description = "组织ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{project.organization_id.not_blank}", groups = {Created.class, Updated.class})
        @Size(min = 1, max = 50, message = "{project.organization_id.length_range}", groups = {Created.class, Updated.class})
        String organizationId,
        @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{project.name.not_blank}", groups = {Created.class, Updated.class})
        @Size(min = 1, max = 255, message = "{project.name.length_range}", groups = {Created.class, Updated.class})
        String name
) {
}
