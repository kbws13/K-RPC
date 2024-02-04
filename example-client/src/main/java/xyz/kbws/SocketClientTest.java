package xyz.kbws;

import xyz.kbws.config.RpcServiceConfig;
import xyz.kbws.hello.Hello;
import xyz.kbws.hello.HelloService;
import xyz.kbws.proxy.RpcClientProxy;
import xyz.kbws.remoting.transport.RpcRequestTransport;
import xyz.kbws.remoting.transport.socket.SocketRpcClient;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
public class SocketClientTest {
    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);
    }
}
