package cn.master.nexus.auth.controller;

import cn.master.nexus.auth.dto.LoginRequest;
import cn.master.nexus.auth.dto.RefreshRequest;
import cn.master.nexus.auth.dto.TokenResponse;
import cn.master.nexus.auth.dto.UserDTO;
import cn.master.nexus.auth.service.AuthService;
import cn.master.nexus.common.exception.BusinessException;
import cn.master.nexus.common.result.ResultHolder;
import cn.master.nexus.common.util.SessionUtils;
import cn.master.nexus.common.util.Translator;
import cn.master.nexus.security.CustomUserDetails;
import cn.master.nexus.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Strings;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
@RestController
@Tag(name = "登录接口")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final Translator translator;
    private final AuthService authService;

    @NullMarked
    @PostMapping("login")
    @Operation(description = "用户登录")
    public ResponseEntity<TokenResponse> login(@RequestBody @Parameter @Validated LoginRequest request) {
        CustomUserDetails sessionUser = SessionUtils.getCurrentUser();
        if (Objects.nonNull(sessionUser)) {
            if (!Strings.CS.equals(sessionUser.user().getName(), request.username())) {
                throw new BusinessException(translator.get("please_logout_current_user"));
            }
        }
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if (authenticate.getPrincipal() instanceof CustomUserDetails userDetails) {
            String accessToken = jwtTokenProvider.generateToken(userDetails, "access");
            String refreshToken = jwtTokenProvider.generateToken(userDetails, "refresh");
            UserDTO userDTO = authService.getUserDTO(userDetails.user().getId());
            authService.autoSwitch(userDTO);
            return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken, userDTO, 900));
        }
        return ResponseEntity.status(401).body(null);
    }

    @NullMarked
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtTokenProvider.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(401).body(null);
        }

        // String username = jwtTokenProvider.extractUsername(refreshToken);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 生成新的 Access Token
        String newAccessToken = jwtTokenProvider.generateToken((UserDetails) authentication.getPrincipal(), "access");

        // 可选策略：是否轮换 Refresh Token？
        // 策略 A: 不轮换，直接返回旧的 refreshToken (当前实现)
        // 策略 B: 轮换，生成新 refreshToken，使旧失效 (更安全，需更新 Redis)
        String newRefreshToken = refreshToken;

        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken, new UserDTO(), 900));
    }

    @PostMapping("/logout")
    public ResultHolder logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // 将 Access Token 加入黑名单
            // 需要解析出过期时间来设置 Redis TTL
            try {
                // 这里简化处理，实际应从 token 解析 exp
                jwtTokenProvider.blacklistToken(token);
                // 同时删除 Redis 中的 Refresh Token 以防万一
                String username = jwtTokenProvider.extractUsername(token);
                jwtTokenProvider.deleteRefreshToken(username);
            } catch (Exception e) {
                // Token 可能已经过期，忽略
            }
        }
        return ResultHolder.success("logout success");
    }
}
