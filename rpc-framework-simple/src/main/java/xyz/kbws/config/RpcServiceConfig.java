package xyz.kbws.config;

import lombok.*;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 注册服务的配置类
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceConfig {
    /**
     * 服务版本
     */
    private String version = "";
    /**
     * 当接口具有多个实现类时，按组进行区分
     */
    private String group = "";

    /**
     * 目标服务
     */
    private Object service;

    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
