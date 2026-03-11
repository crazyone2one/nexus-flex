package cn.master.nexus.auth.dto;

/**
 * @author : 11's papa
 * @since : 2026/3/4, 星期三
 **/
public record TokenResponse(String accessToken,
                            String refreshToken,
                            UserDTO userDTO,
                            long expiresIn) {
}
