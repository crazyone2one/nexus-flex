package cn.master.nexus.security;

import cn.master.nexus.common.util.LogUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final RedissonClient redissonClient;

    /**
     * 生成用于JWT签名的密钥
     * 确保密钥符合JWT JWA规范要求（至少256位）
     *
     * @return SecretKey 用于JWT签名的安全密钥
     * @throws IllegalArgumentException 当密钥不符合安全要求时抛出异常
     */
    private SecretKey key() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
            // JWT JWA规范要求HMAC-SHA算法的密钥长度必须≥256位
            if (keyBytes.length < 32) {
                LogUtils.error("JWT secret key is too short: {} bits, minimum required: 256 bits", keyBytes.length * 8);
                throw new IllegalArgumentException(
                        "JWT secret key must be at least 256 bits as per RFC 7518, Section 3.2. Current key size: " + (keyBytes.length * 8) + " bits"
                );
            }
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            LogUtils.error("Failed to create JWT signing key: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid JWT configuration", e);
        }
    }

    public String generateToken(UserDetails userDetails, String tokenType) {
        // 验证令牌类型是否有效
        if (!"access".equals(tokenType) && !"refresh".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type");
        }

        // 根据令牌类型设置过期时间
        long expirationTime = System.currentTimeMillis() + (tokenType.equals("access") ? jwtProperties.getAccessTokenValidity() : jwtProperties.getRefreshTokenValidity());
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        claims.put("token_type", tokenType);
        // 构建并返回JWT令牌
        String jwt = Jwts.builder()
                .header().type("JWT").and()
                .subject(userDetails.getUsername())
                .issuedAt(new Date()).expiration(new Date(expirationTime))
                .claims(claims)
                .signWith(key())
                .compact();
        if ("refresh".equals(tokenType)) {
            String redisKey = jwtProperties.getRefreshPrefix() + extractUsername(jwt);
            RBucket<String> bucket = redissonClient.getBucket(redisKey);
            bucket.set(jwt, Duration.ofMillis(jwtProperties.getRefreshTokenValidity()));
        }
        return jwt;
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public Boolean isValidToken(String token, UserDetails userDetails) {
        // 1. 检查是否在黑名单 (Access Token 注销场景)
        if (isBlacklisted(token)) {
            return false;
        }
        // Check whether the username sent the token is the same username of the current user
        final String usernameInToken = extractUsername(token);

        // The token is valid when the username sent the token is the same username of the current user && its expiration data is after the current date
        return usernameInToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isBlacklisted(String token) {
        // 简单实现：直接查 token 字符串是否在黑名单 bucket 中
        // 生产环境建议解析出 JTI (id) 存入黑名单
        return redissonClient.getBucket(jwtProperties.getBlackPrefix() + token).isExists();
    }

    public void deleteRefreshToken(String username) {
        String key = jwtProperties.getRefreshPrefix() + username;
        redissonClient.getBucket(key).delete();
    }

    public void blacklistToken(String token) {
        // 计算剩余有效期
        Date exp = getExpirationDateFromToken(token);
        long ttl = exp.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            String key = jwtProperties.getBlackPrefix() + token; // 或者使用 JTI
            redissonClient.getBucket(key).set("true", Duration.ofMillis(ttl));
        }
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            if (isBlacklisted(refreshToken)) return false;

            // 验证签名和过期时间
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(refreshToken);

            // 验证 Redis 中是否存在该 Token (防止被盗用后原主人修改密码等情况，或者实现单点登录互踢)
            String username = extractUsername(refreshToken);
            String redisKey = jwtProperties.getRefreshPrefix() + username;
            RBucket<String> bucket = redissonClient.getBucket(redisKey);
            String storedToken = bucket.get();

            return refreshToken.equals(storedToken);
        } catch (JwtException e) {
            return false;
        }
    }
}
