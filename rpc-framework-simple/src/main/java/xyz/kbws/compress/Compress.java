package xyz.kbws.compress;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
public interface Compress {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);

}
