package cn.master.nexus.modules.system.entity;

import cn.master.nexus.common.validation.groups.Created;
import cn.master.nexus.common.validation.groups.Updated;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户 实体类。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户")
@Table("system_user")
public class SystemUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.id.not_blank}", groups = {Updated.class})
    @Size(min = 1, max = 50, message = "{user.id.length_range}", groups = {Created.class, Updated.class})
    private String id;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.name.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 255, message = "{user.name.length_range}", groups = {Created.class, Updated.class})
    private String name;

    @Schema(description = "用户邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.email.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 64, message = "{user.email.length_range}", groups = {Created.class, Updated.class})
    private String email;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    private String password;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean enable;

    /**
     * 语言
     */
    @Schema(description = "语言")
    private String language;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "来源：LOCAL OIDC CAS OAUTH2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.source.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 50, message = "{user.source.length_range}", groups = {Created.class, Updated.class})
    private String source;

    /**
     * 当前组织ID
     */
    @Schema(description = "当前组织ID")
    private String lastOrganizationId;

    /**
     * 当前项目ID
     */
    @Schema(description = "当前项目ID")
    private String lastProjectId;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createUser;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    private String updateUser;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "now()")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    private Boolean deleted;

}
