package cn.master.nexus.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private long accessTokenValidity;
    private long refreshTokenValidity;
    private String secret;
    private String refreshPrefix;
    private String blackPrefix;
}
