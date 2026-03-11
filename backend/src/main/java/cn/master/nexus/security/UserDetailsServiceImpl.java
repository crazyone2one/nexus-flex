package cn.master.nexus.security;

import cn.master.nexus.modules.system.entity.SystemUser;
import cn.master.nexus.modules.system.entity.UserRolePermission;
import cn.master.nexus.modules.system.entity.UserRoleRelation;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RedissonClient redissonClient;

    @NullMarked
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return QueryChain.of(SystemUser.class).where(SystemUser::getName).eq(username)
                .oneOpt()
                .map(user -> {
                    List<String> roles = QueryChain.of(UserRoleRelation.class)
                            .where(UserRoleRelation::getUserId).eq(user.getId()).list()
                            .stream().map(UserRoleRelation::getRoleId).toList();
                    List<String> permissions = QueryChain.of(UserRolePermission.class).where(UserRolePermission::getRoleId).in(roles).list().stream()
                            .map(UserRolePermission::getPermissionId).toList();
                    redissonClient.getList("auth:perms:" + user.getId()).addAll(permissions);
                    List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
                    return new CustomUserDetails(user, authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
