package com.example.demo.weixin;

import lombok.Data;

@Data
public class News {
    private String title;//标题
    private String description;//描述
    private String picUrl;//图片地址
    private String url;//访问地址
}
