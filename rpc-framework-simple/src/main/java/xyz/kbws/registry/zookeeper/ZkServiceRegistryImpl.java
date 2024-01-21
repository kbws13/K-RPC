package xyz.kbws.registry.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import xyz.kbws.registry.ServiceRegistry;
import xyz.kbws.utils.CuratorUtils;

import java.net.InetSocketAddress;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 基于 Zookeeper 的服务注册
 */
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
