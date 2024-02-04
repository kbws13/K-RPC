package xyz.kbws.socket;

import org.junit.Test;
import xyz.kbws.remoting.dto.RpcRequest;
import xyz.kbws.remoting.transport.socket.SocketRpcClient;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
public class TestSocketClient {
    @Test
    public void testClient() {
        SocketRpcClient socketRpcClient = new SocketRpcClient();
        RpcRequest rpcRequest = RpcRequest.builder().requestId("1").group("test").build();
        socketRpcClient.sendRpcRequest(rpcRequest);
    }
}
