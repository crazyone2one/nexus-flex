package cn.master.nexus.common.annotation;

import cn.master.nexus.common.constants.OperationLogType;

import java.lang.annotation.*;

/**
 * @author : 11's papa
 * @since : 2026/3/4, 星期三
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    OperationLogType type() default OperationLogType.SELECT;
    String expression();
    Class[] msClass() default {};
}
