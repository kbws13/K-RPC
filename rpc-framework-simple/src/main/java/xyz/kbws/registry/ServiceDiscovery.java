package xyz.kbws.registry;

import xyz.kbws.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 服务发现
 */
public interface ServiceDiscovery {
    /**
     * 按RpcServiceName查找服务
     *
     * @param rpcRequest RPC 服务实体类
     * @return 服务地址
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
