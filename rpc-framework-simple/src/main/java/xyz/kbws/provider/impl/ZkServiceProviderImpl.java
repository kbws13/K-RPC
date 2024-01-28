package xyz.kbws.provider.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.kbws.config.RpcServiceConfig;
import xyz.kbws.enums.RpcErrorMessageEnum;
import xyz.kbws.exception.RpcException;
import xyz.kbws.provider.ServiceProvider;
import xyz.kbws.registry.ServiceRegistry;
import xyz.kbws.registry.zookeeper.ZkServiceRegistryImpl;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static xyz.kbws.remoting.constants.RpcConstants.PORT;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {

    /**
     * key: RPC 服务名称(interface name + version + group)
     * value: 服务对象
     */
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = new ZkServiceRegistryImpl();
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}
