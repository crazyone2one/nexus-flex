package cn.master.nexus.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@Data
public class BaseCondition {
    @Schema(description =  "关键字")
    private String keyword;

    @Schema(description =  "过滤字段")
    private Map<String, List<String>> filter;
}
