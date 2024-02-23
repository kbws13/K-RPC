package xyz.kbws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.kbws.annotation.RpcReference;
import xyz.kbws.hello.Hello;
import xyz.kbws.hello.HelloService;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
@Component
public class HelloController {

    @RpcReference(version = "version2", group = "test2")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is 222".equals(hello);
        Thread.sleep(12000);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111", "222")));
        }
    }
}
