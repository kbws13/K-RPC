package xyz.kbws.annotation;

import java.lang.annotation.*;

/**
 * @author kbws
 * @date 2024/2/3
 * @description: RPC服务注释，标记在服务实现类上
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";


}
