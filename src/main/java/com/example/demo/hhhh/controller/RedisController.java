package com.example.demo.hhhh.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("redis")
public class RedisController {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @RequestMapping("/save")
    public Object save(){
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set("test","232");

        return null;
    }
    @RequestMapping("/get")
    public Object get(){
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return vo.get("test");
    }
}
