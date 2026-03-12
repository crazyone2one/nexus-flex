package cn.master.nexus.security.handler;

import cn.master.nexus.common.util.JSON;
import cn.master.nexus.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@NullMarked
@Component
@RequiredArgsConstructor
public class OptimizedPermissionEvaluator implements PermissionEvaluator {
    private final RedissonClient redissonClient;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (!authentication.isAuthenticated()) {
            return false;
        }
        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return false;
        }
        // L1: 检查核心角色 (内存，极快)
        if (userDetails.getAuthorities().stream().anyMatch(a -> Objects.equals(a.getAuthority(), "admin"))) {
            return true;
        }
        // L2: 检查 Redis 中的全量权限集
        // Key: auth:perms:{userId}
        List<String> permissions = redissonClient.getList("auth:perms:" + userDetails.user().getId());
        String resource = (String) targetDomainObject;
        String requiredOp = (String) permission;
        String fullPermission = resource + ":" + requiredOp;
        return permissions.contains(fullPermission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
