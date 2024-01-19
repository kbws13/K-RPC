package xyz.kbws.serialize;

/**
 * @author kbws
 * @date 2024/1/19
 * @description: 序列化接口，所有序列化类都要实现这个接口
 */
public interface Serializer {
    /**
     * 序列化
     * @param obj 要序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);


    /**
     * 反序列化
     * @param bytes 序列化后的字节数组
     * @param clazz 目标类
     * @param <T> 类的类型，如果不知道类的类型的话，使用 Class<?>
     * @return 反序列化的对象
     */
    <T> T deserializer(byte[] bytes, Class<T> clazz);
}
