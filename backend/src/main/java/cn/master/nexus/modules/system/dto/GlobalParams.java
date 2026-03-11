package cn.master.nexus.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/9, 星期一
 **/
@Data
public class GlobalParams implements Serializable {

    @Schema(description = "请求头")
    private List<KeyValueEnableParam> headers;
    @Schema(description = "全局变量")
    private List<CommonVariables> commonVariables;

    @Serial
    private static final long serialVersionUID = 1L;
}
