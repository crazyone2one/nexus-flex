package cn.master.nexus.modules.system.dto;

import cn.master.nexus.common.constants.VariableTypeConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/9, 星期一
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonVariables extends KeyValueParam {
    @Schema(description = "id")
    private String id;
    @Schema(description = "变量类型 CONSTANT LIST JSON")
    private String paramType = VariableTypeConstants.CONSTANT.name();
    @Schema(description = "状态")
    private Boolean enable = true;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "标签")
    private List<String> tags;

    @JsonIgnore
    public boolean isConstantValid() {
        return (VariableTypeConstants.CONSTANT.name().equals(this.paramType) || StringUtils.isBlank(paramType)) && isValid();
    }

    @JsonIgnore
    public boolean isListValid() {
        return VariableTypeConstants.LIST.name().equals(this.paramType) && isValid() && isNotBlankValue() && getValue().contains(",");
    }

    @JsonIgnore
    public boolean isJsonValid() {
        return VariableTypeConstants.JSON.name().equals(this.paramType) && isValid();
    }
}
