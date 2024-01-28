package xyz.kbws.provider;

import xyz.kbws.config.RpcServiceConfig;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 存储和提供服务对象
 */
public interface ServiceProvider {
    /**
     * @param rpcServiceConfig RPC 服务相关属性
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceName RPC 服务名称
     * @return 服务对象
     */
    Object getService(String rpcServiceName);

    /**
     * @param rpcServiceConfig RPC 服务相关属性
     */
    void publishService(RpcServiceConfig rpcServiceConfig);
}
