package com.example.demo.abc;

import javax.crypto.Cipher;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

        /** 默认私钥 */
        private static final String DEFAULT_PRIVATE_KEY_STRING = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALOuNbJkZHuQ12WeMNim0QH2YRzXQ1Tnea5Qz/S9vMPyK96RfnO4no/tWuPNPtUi7Wv1T0Hb7FfNIX7a2KcTMGkTzP8MB4vbflmY1RAkUhUa+sZRiziruePbWfio2XB9uOkQfaQ+QEbeOAB4QcxePi2Rojk1v/oqv7IJmlpKfZT7AgMBAAECgYBk8rdQZn7dvV6GxP2EexRqGdYtsfMuPG5stRJg2ki3fdOP1AZLVaogCqZJ3gIzFNB3GvdQdRKvsZvj/WhDVdCYA+tGB/mVVpww3ySdcnlC+2sdq8eMxfj1dUc3ozTgUebQOapWj1CH+a0v9KPocWqfM7gCbURqe+OqSP4iYGWxoQJBAP62v48y6eansUSXtf004I5t2OBx/g+JTVLm7tVzY199l0hjXFe4kFpFgliu1QXxaI/WbwBv2jj3z1vRlmWH1g0CQQC0lniIfG9WzlFeaHudoCgyO9K5HtfJgnes0kjbPN3R88ZcpqtJEhr0C5EbA5fVNJj3Sfssq/x0ndH1unJrHp0nAkEA4hzU3Igz/6Apy1xqIAcKuSVme4h2ItCeJdP/fNjcBG73FzmtRen/q0M9PQCm9omhat7O1zJ0JFJIb5gUzLjTGQJBAKQ1gyrQw8lSWGkZZvaA8Kwk+8s7gYqJ6Vqr6HX61dWY7pHHOE98eCfb8Ll4vfKwHjwW+J+5UBmWfJoOTQac0tcCQQD+oZa16KxdyYd4vzcyAoghTAhYr+pAyTUvqpx4cBe0nnDaClIbPbh3sz/ZZwZAq02H/lqSHiErVPUiEQ8ir4pq";

        /** 默认公钥 */
        public static final String DEFAULT_PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzrjWyZGR7kNdlnjDYptEB9mEc10NU53muUM/0vbzD8ivekX5zuJ6P7VrjzT7VIu1r9U9B2+xXzSF+2tinEzBpE8z/DAeL235ZmNUQJFIVGvrGUYs4q7nj21n4qNlwfbjpEH2kPkBG3jgAeEHMXj4tkaI5Nb/6Kr+yCZpaSn2U+wIDAQAB";

        /**
         * 加密算法RSA
         */
        public static final String KEY_ALGORITHM = "RSA";

        /**
         * 签名算法
         */
        public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

        /**
         * 获取公钥的key
         */
        public static final String PUBLIC_KEY = "RSAPublicKey";

        /**
         * 获取私钥的key
         */
        public static final String PRIVATE_KEY = "RSAPrivateKey";

        /**
         * RSA最大加密明文大小
         */
        private static final int MAX_ENCRYPT_BLOCK = 117;

        /**
         * RSA最大解密密文大小
         */
        private static final int MAX_DECRYPT_BLOCK = 128;

        /**
         * <p>
         * 生成密钥对(公钥和私钥)
         * </p>
         *
         * @return
         * @throws Exception
         */
        public static Map<String, RSAKey> genKeyPair() throws Exception {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, RSAKey> keyMap = new HashMap<String, RSAKey>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        }
        /**
         * <P>
         * 默认私钥解密
         * </p>
         *
         * @param encryptedData 已加密数据
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPrivateKey(byte[] encryptedData)  throws Exception {
            return decryptByPrivateKey(encryptedData,null);
        }

        /**
         * <P>
         * 私钥解密
         * </p>
         *
         * @param encryptedData 已加密数据
         * @param privateKey 私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
                throws Exception {
            if(null==privateKey || "".equals(privateKey)){
                privateKey = DEFAULT_PRIVATE_KEY_STRING;
            }
            byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
            encryptedData = Base64Utils.decode(encryptedData);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }
        /**
         * <p>
         * 默认公钥解密
         * </p>
         *
         * @param encryptedData 已加密数据
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPublicKey(byte[] encryptedData)  throws Exception {
            return decryptByPublicKey(encryptedData, null);
        }
        /**
         * <p>
         * 公钥解密
         * </p>
         *
         * @param encryptedData 已加密数据
         * @param publicKey 公钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
                throws Exception {
            if(null==publicKey || "".equals(publicKey)){
                publicKey = DEFAULT_PUBLIC_KEY_STRING;
            }
            byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
            encryptedData = Base64Utils.decode(encryptedData);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }
        /**
         * <p>
         * 默认公钥加密
         * </p>
         *
         * @param data 源数据
         * @return  base64加密的公钥加密结果
         * @throws Exception
         */
        public static byte[] encryptByPublicKey(byte[] data)  throws Exception {
            return encryptByPublicKey(data, null);
        }
        /**
         * <p>
         * 公钥加密
         * </p>
         *
         * @param data 源数据
         * @param publicKey 公钥(BASE64编码)
         * @return  base64加密的公钥加密结果
         * @throws Exception
         */
        public static byte[] encryptByPublicKey(byte[] data, String publicKey)
                throws Exception {
            if(null==publicKey || "".equals(publicKey)){
                publicKey = DEFAULT_PUBLIC_KEY_STRING;
            }
            byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();

            out.close();
            return  Base64Utils.encode(encryptedData);
        }
        /**
         * <p>
         * 默认私钥加密
         * </p>
         *
         * @param data 源数据
         * @param privateKey 私钥(BASE64编码)
         * @throws Exception
         */
        public static byte[] encryptByPrivateKey(byte[] data)   throws Exception {
            return encryptByPrivateKey(data, null);
        }
        /**
         * <p>
         * 私钥加密
         * </p>
         *
         * @param data 源数据
         * @param privateKey 私钥(BASE64编码)
         * @return base64加密的私钥加密结果
         * @throws Exception
         */
        public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
                throws Exception {
            if(null==privateKey || "".equals(privateKey)){
                privateKey = DEFAULT_PRIVATE_KEY_STRING;
            }
            byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64Utils.encode(encryptedData);
        }

        public static void printPPKeys() throws Exception{
            Map<String, RSAKey> keys=RSAUtils.genKeyPair();
            System.out.println("============================ 公钥 =================================");
            System.out.println(Base64.getEncoder().encodeToString(((Key)keys.get(PUBLIC_KEY)).getEncoded()));
            System.out.println();
            System.out.println("============================ 私钥 =================================");
            System.out.println(Base64.getEncoder().encodeToString(((Key)keys.get(PRIVATE_KEY)).getEncoded()));
        }

        /**
         * <p>
         * 获取私钥
         * </p>
         *
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        public static String getPrivateKey(Map<String, Object> keyMap)
                throws Exception {
            Key key = (Key) keyMap.get(PRIVATE_KEY);
            return new String(Base64Utils.encode(key.getEncoded()));
        }

        /**
         * <p>
         * 获取公钥
         * </p>
         *
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        public static String getPublicKey(Map<String, Object> keyMap)
                throws Exception {
            Key key = (Key) keyMap.get(PUBLIC_KEY);
            return new String(Base64Utils.encode(key.getEncoded()));
        }

        public static boolean doCheck(String content, String sign, String publicKey, String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGNATURE_ALGORITHM);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));

            boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
