package cn.master.nexus.modules.system.dto;

import cn.master.nexus.modules.system.entity.SystemUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/13, 星期五
 **/

@Data
public class UserExtendDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userName;
    /**
     * 是否管理员(组织, 项目)
     */
    @Schema(description = "是否组织/项目管理员, 是: 展示管理员标识, 否: 不展示管理员标识")
    private boolean adminFlag;

    /**
     * 是否成员(组织, 项目)
     */
    @Schema(description = "是否组织/项目成员, 是: 勾选禁用, 否: 勾选启用")
    private boolean memberFlag;

    /**
     * 是否勾选用户组
     */
    @Schema(description = "是否属于用户组, 是: 勾选禁用, 否: 勾选启用")
    private boolean checkRoleFlag;

    @Schema(description = "组织ID")
    private String sourceId;
    @Schema(description = "用户所属用户组")
    private List<UserRoleOptionDto> userRoleList = new ArrayList<>();
}
