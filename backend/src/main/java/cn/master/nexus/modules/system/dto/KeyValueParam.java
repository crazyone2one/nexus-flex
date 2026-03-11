package cn.master.nexus.modules.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : 11's papa
 * @since : 2026/3/9, 星期一
 **/
@Data
public class KeyValueParam {
    private String key;
    private String value;

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(key);
    }

    @JsonIgnore
    public boolean isNotBlankValue() {
        return StringUtils.isNotBlank(value);
    }
}
