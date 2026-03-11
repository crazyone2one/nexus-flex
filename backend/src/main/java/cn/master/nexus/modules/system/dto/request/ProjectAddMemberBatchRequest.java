package cn.master.nexus.modules.system.dto.request;

import cn.master.nexus.common.validation.groups.Created;
import cn.master.nexus.common.validation.groups.Updated;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectAddMemberBatchRequest extends ProjectAddMemberRequest {
    @Schema(description = "项目ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<
            @NotBlank(message = "{project.id.not_blank}", groups = {Created.class, Updated.class})
                    String> projectIds;
}
