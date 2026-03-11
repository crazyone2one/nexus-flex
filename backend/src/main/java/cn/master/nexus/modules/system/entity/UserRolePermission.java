package cn.master.nexus.modules.system.entity;

import cn.master.nexus.common.validation.groups.Created;
import cn.master.nexus.common.validation.groups.Updated;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import java.io.Serial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户组权限 实体类。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户组权限")
@Table("user_role_permission")
public class UserRolePermission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Schema(description =  "", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user_role_permission.id.not_blank}", groups = {Updated.class})
    @Size(min = 1, max = 64, message = "{user_role_permission.id.length_range}", groups = {Created.class, Updated.class})
    private String id;

    @Schema(description =  "用户组ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user_role_permission.role_id.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 64, message = "{user_role_permission.role_id.length_range}", groups = {Created.class, Updated.class})
    private String roleId;

    @Schema(description =  "权限ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user_role_permission.permission_id.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 128, message = "{user_role_permission.permission_id.length_range}", groups = {Created.class, Updated.class})
    private String permissionId;

}
