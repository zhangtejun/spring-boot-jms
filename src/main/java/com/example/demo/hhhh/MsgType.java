package com.example.demo.hhhh;

public enum MsgType {
    text("文本"),image("图片"),location("地理位置"),link("连接"),event("事件"),news("图文信息") ;
    private String value;

    public String getValue() {
        return value;
    }
    private MsgType(String value) {
        this.value = value;
    }
}
