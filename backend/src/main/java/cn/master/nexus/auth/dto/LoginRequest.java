package cn.master.nexus.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
public record LoginRequest(@NotBlank(message = "{user_name_is_null}")
                           @Size(max = 256, message = "{user_name_length_too_long}")
                           String username,
                           @NotBlank(message = "{password_is_null}")
                           @Size(max = 256, message = "{password_length_too_long}")
                           String password) {
}
