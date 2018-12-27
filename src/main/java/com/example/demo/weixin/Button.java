package com.example.demo.weixin;

import lombok.Data;

@Data
public class Button {
    private String type;
    private String name;
    private Button[] subButton;
}
