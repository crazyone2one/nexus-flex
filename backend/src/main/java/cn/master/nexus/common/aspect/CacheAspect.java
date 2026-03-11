package cn.master.nexus.common.aspect;

import cn.master.nexus.common.annotation.CacheablePlus;
import cn.master.nexus.common.config.RedissonCacheComp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final RedissonCacheComp cacheManager;
    private final RedissonClient redissonClient;

    @Around("@annotation(cacheable)")
    public Object around(ProceedingJoinPoint joinPoint, CacheablePlus cacheable) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String cacheName = cacheable.name();
        // 解析 SpEL 生成 Key
        String key = parseKey(cacheable.key(), method, joinPoint.getArgs());
        String fullKey = cacheName + ":" + key;
        Object object = cacheManager.getCache(fullKey, Object.class);
        if (Objects.isNull(object)) {
            RLock lock = redissonClient.getLock("cache:lock:" + key);
            try {
                if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                    Object dbValue = joinPoint.proceed();
                    cacheManager.setCache(key, dbValue);
                    return dbValue;
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                assert lock != null;
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        return object;
    }


    private String parseKey(String keyExpression, Method method, Object[] args) {
        if (keyExpression.isEmpty()) {
            return String.valueOf(args.length > 0 ? args[0] : "no_key");
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        // 绑定参数名 (需要编译时开启 -parameters 或使用 Paranamer)
        // 简单处理：绑定 args[0], args[1] 或 #a0, #a1
        for (int i = 0; i < args.length; i++) {
            context.setVariable("p" + i, args[i]);
            context.setVariable("arg" + i, args[i]);
            // 如果有 @Param 注解或其他机制获取参数名，可在此处设置 #paramName
        }
        // 绑定所有参数数组
        context.setVariable("args", args);

        // 尝试解析第一个参数作为 #id 等 (简化版，完整需反射获取参数名)
        if (args.length > 0) {
            context.setVariable("id", args[0]);
            context.setVariable("root", args[0]);
        }

        try {
            return Objects.requireNonNull(parser.parseExpression(keyExpression).getValue(context, String.class));
        } catch (Exception e) {
            log.warn("SpEL parsing failed for expression: {}, fallback to args[0]", keyExpression);
            return args.length > 0 ? String.valueOf(args[0]) : "unknown";
        }
    }
}
