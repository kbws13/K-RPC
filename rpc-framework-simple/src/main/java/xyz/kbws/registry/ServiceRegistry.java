package xyz.kbws.registry;

import java.net.InetSocketAddress;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 服务注册
 */
public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param rpcServiceName    RPC 服务名称
     * @param inetSocketAddress 服务地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
