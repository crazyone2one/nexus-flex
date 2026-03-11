package cn.master.nexus.modules.system.dto.request;

import cn.master.nexus.modules.system.dto.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectRequest extends BasePageRequest {
    @Schema(description =  "组织ID")
    private String organizationId;
    @Schema(description =  "项目ID")
    private String projectId;
}
