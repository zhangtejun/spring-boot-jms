package com.example.demo.abc;


import lombok.Data;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Stack<T> {
    //1.定义一个内部类来保存每个链表的节点，该节点包括当前的值以及指向下一个的值
    @Data
    class Node {
        T item;
        Node next;
    }
    private int num = 0;// Stack数量
    private Node first = null;

   public void push(T node){
        //1.保存之前的栈顶元素
        Node oldFirst = first;
        //2.
        first = new Node();
        first.item = node;
        first.next = oldFirst;
        num++;
    }
    public T pop(){
        //1.保存之前的栈顶元素
        T item = first.item;
        //2.更新栈顶元素
        first = first.next;
        num --;
        return item;
    }
    public int size(){
       return num;
    }



    /**
     * 随机生成密钥对
     */
    public static Map<String, Object> genKeyPair(String filePath) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flag", "true");
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        try {
            // 得到公钥字符串
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            // 得到私钥字符串
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);

        } catch (Exception e) {
            map.put("flag", "false");
            e.printStackTrace();
        }
        return map;
    }




}
