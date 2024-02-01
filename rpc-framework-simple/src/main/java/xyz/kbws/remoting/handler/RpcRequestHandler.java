package xyz.kbws.remoting.handler;

import lombok.extern.slf4j.Slf4j;
import xyz.kbws.exception.RpcException;
import xyz.kbws.factory.SingletonFactory;
import xyz.kbws.provider.ServiceProvider;
import xyz.kbws.provider.impl.ZkServiceProviderImpl;
import xyz.kbws.remoting.dto.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: RpcRequest处理器
 */
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 处理rpcRequest：调用相应的方法，然后返回该方法
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 获取方法执行结果
     *
     * @param rpcRequest 客户端请求
     * @param service    服务对象
     * @return 目标方法执行的结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
