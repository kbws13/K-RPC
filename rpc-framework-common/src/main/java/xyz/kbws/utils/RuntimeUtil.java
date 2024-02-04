package xyz.kbws.utils;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
public class RuntimeUtil {
    /**
     * 获取CPU的核心数
     *
     * @return cpu的核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
