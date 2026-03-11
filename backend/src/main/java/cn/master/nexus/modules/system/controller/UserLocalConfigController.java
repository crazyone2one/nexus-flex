package cn.master.nexus.modules.system.controller;

import cn.master.nexus.common.util.SessionUtils;
import cn.master.nexus.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author : 11's papa
 * @since : 2026/3/9, 星期一
 **/
@RestController
@RequestMapping("/user/local/config")
@Tag(name = "系统设置-个人中心-我的设置-本地执行")
public class UserLocalConfigController {
    @Value("${spring.messages.default-locale}")
    private String defaultLocale;

    @GetMapping(value = "/default-locale")
    public String defaultLocale() {
        CustomUserDetails user = SessionUtils.getCurrentUser();
        String language = Optional.ofNullable(user)
                .map(u -> u.user().getLanguage())
                .filter(StringUtils::isNotBlank)
                .orElse(defaultLocale);
        return language.replace("_", "-");
    }
}
