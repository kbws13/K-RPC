package xyz.kbws.annotation;

import java.lang.annotation.*;

/**
 * @author kbws
 * @date 2024/2/3
 * @description: RPC引用注释，自动连接服务实现类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {
    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";
}
