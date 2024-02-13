package xyz.kbws.proxy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import xyz.kbws.config.RpcServiceConfig;
import xyz.kbws.enums.RpcErrorMessageEnum;
import xyz.kbws.enums.RpcResponseCodeEnum;
import xyz.kbws.exception.RpcException;
import xyz.kbws.remoting.dto.RpcRequest;
import xyz.kbws.remoting.dto.RpcResponse;
import xyz.kbws.remoting.transport.RpcRequestTransport;
import xyz.kbws.remoting.transport.netty.client.NettyRpcClient;
import xyz.kbws.remoting.transport.socket.SocketRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 动态代理类
 * 当一个动态代理对象调用一个方法时，它实际上调用了以下invoke方法
 * 正是因为有了动态代理，客户端调用的远程方法就像调用本地方法一样（屏蔽了中间进程）
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private static final String INTERFACE_NAME = "interfaceName";

    /**
     * 用于向服务器发送请求
     */
    private final RpcRequestTransport rpcRequestTransport;
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }


    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RpcServiceConfig();
    }

    /**
     * 获取代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 当使用代理对象来调用方法时，实际上会调用此方法。
     * 代理对象是通过getProxy方法获得的对象
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        RpcResponse<Object> rpcResponse = null;
        if (rpcRequestTransport instanceof NettyRpcClient) {
            CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();
        }
        if (rpcRequestTransport instanceof SocketRpcClient) {
            rpcResponse = (RpcResponse<Object>) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }
        this.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
    }

    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
