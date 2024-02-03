package xyz.kbws.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import xyz.kbws.config.CustomShutdownHook;
import xyz.kbws.config.RpcServiceConfig;
import xyz.kbws.factory.SingletonFactory;
import xyz.kbws.provider.ServiceProvider;
import xyz.kbws.provider.impl.ZkServiceProviderImpl;
import xyz.kbws.utils.ThreadPoolFactoryUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static xyz.kbws.remoting.constants.RpcConstants.PORT;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
@Slf4j
public class SocketRpcServer {

    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;


    public SocketRpcServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandler(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }
}
