package xyz.kbws.annotation;

import org.springframework.context.annotation.Import;
import xyz.kbws.spring.CustomScannerRegistrar;

import java.lang.annotation.*;

/**
 * @author kbws
 * @date 2024/2/3
 * @description: 扫描自定义注释
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Documented
public @interface RpcScan {

    String[] basePackage();

}
