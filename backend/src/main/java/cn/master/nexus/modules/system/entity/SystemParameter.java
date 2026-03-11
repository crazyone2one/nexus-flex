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
 * 系统参数 实体类。
 *
 * @author 11's papa
 * @since 2026-03-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统参数")
@Table("system_parameter")
public class SystemParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Schema(description = "ID")
    private String id;


    @Schema(description =  "参数名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{system_parameter.param_key.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 64, message = "{system_parameter.param_key.length_range}", groups = {Created.class, Updated.class})
    private String paramKey;

    /**
     * 参数值
     */
    @Schema(description = "参数值")
    private String paramValue;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;

}
