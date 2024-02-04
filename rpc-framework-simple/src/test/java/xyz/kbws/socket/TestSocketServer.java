package xyz.kbws.socket;

import org.junit.Test;
import xyz.kbws.config.RpcServiceConfig;
import xyz.kbws.remoting.transport.socket.SocketRpcServer;

import java.net.UnknownHostException;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
public class TestSocketServer {
    @Test
    public void testServer() throws UnknownHostException {
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setGroup("test");
        rpcServiceConfig.setService("test");
        rpcServiceConfig.setVersion("1");
        socketRpcServer.start();
    }
}
