package xyz.kbws.loadBalancer;

import org.springframework.util.CollectionUtils;
import xyz.kbws.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @author kbws
 * @date 2024/1/23
 * @description: 负载平衡策略的抽象类
 */
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (CollectionUtils.isEmpty(serviceAddresses)) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest);

}
