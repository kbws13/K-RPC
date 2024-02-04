package xyz.kbws.service;

import lombok.extern.slf4j.Slf4j;
import xyz.kbws.hello.Hello;
import xyz.kbws.hello.HelloService;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    static {
        System.out.println("HelloServiceImpl被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
