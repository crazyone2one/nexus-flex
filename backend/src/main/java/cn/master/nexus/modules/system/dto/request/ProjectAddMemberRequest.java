package cn.master.nexus.modules.system.dto.request;

import cn.master.nexus.common.validation.groups.Created;
import cn.master.nexus.common.validation.groups.Updated;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@Data
public class ProjectAddMemberRequest {
    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{project.id.not_blank}")
    private String projectId;

    @Schema(description = "用户ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "{user.ids.not_blank}")
    private List<
            @NotBlank(message = "{user_role_relation.user_id.not_blank}", groups = {Created.class, Updated.class})
                    String> userIds;

    @Schema(description = "用户组ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> userRoleIds;
}
