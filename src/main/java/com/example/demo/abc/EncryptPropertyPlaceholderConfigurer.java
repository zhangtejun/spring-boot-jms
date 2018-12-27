package com.example.demo.abc;
/*import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;*/

import static com.example.demo.abc.RSAUtils.genKeyPair;

public class EncryptPropertyPlaceholderConfigurer {
    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKhFOpZkU4fyKgeeZN+WqjvBKwwv7tCR55IEHwFUxFb8Zfpj99h8uQLdx8YHiDRj6o9ySJGGUKsX3r8vA4BGI3CybksPnC0oc5QS7o3hnI3Skrezy4D2O2XEzm1HB+Y2XfxrFduy+yHKXtTepm5sx6/IQdu53fRQ/4IFEtjwl4RJAgMBAAECgYA61+rDCNaFJdQ40dUos0FYoBZzrpdu7Hb0guhsyL3YRW9L/oV/eS4hqHjh8WRaHc661xY1fajcC/7jEIfC5cQiZXw4fBq6RPiF9WudME09FEACYt7iCMMvNV1XK9P2AFwCJ4PBt8wWBBa4WzH1Ue+P2qLwj2fvhVaIxVHtNSq/eQJBANvgG2TGiFO+K5d4v3K+eAaQBk1G8IA0moJybOQddWcSULKEAHj08Vp/N8lWQ12f3g8BcPtFzFTUoNiSO2HpHB8CQQDD6qGwkAEZwRqrvikXguXnkQomRlBCI2PiLWSo8M9DCq0trgOUICxee/4FC7YKbo05s2PnifthDoGDa+wYKlKXAkEAl3Gg7KSiOav+snIEy6/NVBfXf1qhRGz3D2po/iYcO0YuNwccnDN6Ge8OKas080Ot24AOpcPJnZRmxUTTifjZ2QJAM8td/72ybMqecHUPjAWxHi08D65bHYEZsPvrNrajNMgnzIHKtGhlEn2ZRGA6OjvL3ktMyDO0X45kMwhfHGxj9QJBAJFVYTiIsKOA5YsldZO5w+t7chHhcMWdBdSkgXnFosTt1a4CkTxBznm/DbG4civajJh2Btd98pMIv9dSHmTC3Q8=";
    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoRTqWZFOH8ioHnmTflqo7wSsML+7QkeeSBB8BVMRW/GX6Y/fYfLkC3cfGB4g0Y+qPckiRhlCrF96/LwOARiNwsm5LD5wtKHOUEu6N4ZyN0pK3s8uA9jtlxM5tRwfmNl38axXbsvshyl7U3qZubMevyEHbud30UP+CBRLY8JeESQIDAQAB";
    private String[] encryptPropNames = {"jdbc.username", "jdbc.password", "jdbc.url"};
    private static final String charset = "utf-8";

    /*@Override*/
    protected String convertProperty(String propertyName, String propertyValue) {

        // 如果在加密属性名单中发现该属性
        if (isEncryptProp(propertyName)) {
            String decryptValue;
            try {
                decryptValue = new String(RSAUtils.decryptByPrivateKey(propertyValue.getBytes(charset), privateKey), charset);
                return decryptValue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return propertyValue;
        }
    }

    private boolean isEncryptProp(String propertyName) {
        for (String encryptName : encryptPropNames) {
            if (encryptName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) throws Exception {
        RSAUtils.printPPKeys();
        //加密变量
        String url = "jdbc:mysql://devdb.zcjb.com.cn:3306/ebs_cms?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        String username = "ebs";
        String password = "ebs";
        url = new String(RSAUtils.encryptByPublicKey(url.getBytes(charset), publicKey), charset);
        username = new String(RSAUtils.encryptByPublicKey(username.getBytes(charset), publicKey), charset);
        password = new String(RSAUtils.encryptByPublicKey(password.getBytes(charset), publicKey), charset);
        System.out.println("*****加密结果*****");
        System.out.println(url);
        System.out.println("username： "+username);
        System.out.println("password： "+password);

        System.out.println("*****解密结果*****");
        EncryptPropertyPlaceholderConfigurer config = new EncryptPropertyPlaceholderConfigurer();
        System.out.println(config.convertProperty("jdbc.url", url));
        System.out.println(config.convertProperty("jdbc.username", username));
        System.out.println(config.convertProperty("jdbc.password", password));



    }

}