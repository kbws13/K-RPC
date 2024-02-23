package xyz.kbws;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.kbws.annotation.RpcScan;
import xyz.kbws.config.RpcServiceConfig;
import xyz.kbws.hello.HelloService;
import xyz.kbws.remoting.transport.netty.server.NettyRpcServer;
import xyz.kbws.service.HelloServiceImpl2;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
@RpcScan(basePackage = {"xyz.kbws"})
public class NettyServiceTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServiceTest.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        HelloService helloService2 = new HelloServiceImpl2();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test2").version("version2").service(helloService2).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
