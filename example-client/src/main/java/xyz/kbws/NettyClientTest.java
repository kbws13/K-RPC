package xyz.kbws;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.kbws.annotation.RpcScan;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
@RpcScan(basePackage = {"xyz.kbws"})
public class NettyClientTest {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientTest.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
