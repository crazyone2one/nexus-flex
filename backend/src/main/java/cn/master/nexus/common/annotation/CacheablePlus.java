package cn.master.nexus.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheablePlus {
    /**
     * 缓存名称 (对应 Redis Key 的前缀)
     */
    String name();

    /**
     * 缓存 Key 的 SpEL 表达式，默认使用所有参数 (#root.args)
     * 示例: "#id", "#user.id", "#type + '_' + #id"
     */
    @AliasFor("key")
    String value() default "";

    @AliasFor("value")
    String key() default "";

    /**
     * 本地缓存 (L1) 过期时间
     */
    long localTtl() default 5;

    /**
     * 分布式缓存 (L2/Redis) 过期时间
     */
    long remoteTtl() default 30;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 是否忽略空值 (防止缓存穿透：如果方法返回 null，是否也缓存？通常建议 false)
     */
    boolean allowNull() default false;

    /**
     * 同步策略：INVALIDATE (删除其他节点缓存) 或 UPDATE (推送新值)
     */
    SyncStrategy syncStrategy() default SyncStrategy.INVALIDATE;

    enum SyncStrategy {
        INVALIDATE, UPDATE
    }
}
