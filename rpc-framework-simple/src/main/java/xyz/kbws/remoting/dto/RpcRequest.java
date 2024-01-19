package xyz.kbws.remoting.dto;

import lombok.*;
import xyz.kbws.serialize.Serializer;

import java.io.Serializable;

/**
 * @author kbws
 * @date 2024/1/19
 * @description: 客户端请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 4388009947292352411L;

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String group;

    public String getRpcServiceName() {
        return this.getInterfaceName() + this.getGroup() + this.getVersion();
    }
}
