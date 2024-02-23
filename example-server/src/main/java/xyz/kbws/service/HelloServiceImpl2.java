package xyz.kbws.service;

import lombok.extern.slf4j.Slf4j;
import xyz.kbws.annotation.RpcService;
import xyz.kbws.hello.Hello;
import xyz.kbws.hello.HelloService;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
@Slf4j
@RpcService(group = "test2", version = "version2")
public class HelloServiceImpl2 implements HelloService {
    static {
        System.out.println("HelloServiceImpl2被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl2收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl2返回: {}.", result);
        return result;
    }
}
