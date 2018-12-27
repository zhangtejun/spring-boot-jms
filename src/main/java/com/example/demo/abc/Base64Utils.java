package com.example.demo.abc;

import java.util.Base64;

public class Base64Utils {

    public static byte[] decode(byte[] base64) throws Exception {
        return Base64.getDecoder().decode(base64);
    }

    /**
     * <p>
     * 二进制数据编码为BASE64字符串
     * </p>
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] encode(byte[] bytes) throws Exception {
        return Base64.getEncoder().encode(bytes);
    }

}
