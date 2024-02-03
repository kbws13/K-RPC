package xyz.kbws.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {

    KYRO((byte) 0x01, "kyro");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationTypeEnum c : SerializationTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
