package xyz.kbws;

import xyz.kbws.config.RpcServiceConfig;
import xyz.kbws.hello.HelloService;
import xyz.kbws.remoting.transport.socket.SocketRpcServer;
import xyz.kbws.service.HelloServiceImpl;

import java.net.UnknownHostException;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
public class SocketServiceTest {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(helloService);
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
