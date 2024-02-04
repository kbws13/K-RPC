package xyz.kbws.remoting.transport.socket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.kbws.exception.RpcException;
import xyz.kbws.registry.ServiceDiscovery;
import xyz.kbws.registry.zookeeper.ZkServiceDiscoveryImpl;
import xyz.kbws.remoting.dto.RpcRequest;
import xyz.kbws.remoting.transport.RpcRequestTransport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 基于 Socket 传输 RpcRequest
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RpcRequestTransport {

    private final ServiceDiscovery serviceDiscovery = new ZkServiceDiscoveryImpl();


    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 通过输出流将数据发送到服务端
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 从输入流中读取RpcResponse
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败:", e);
        }
    }
}
