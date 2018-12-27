package com.example.demo.hhhh.help;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class RedisHelpImpl implements RedisHelp {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    ValueOperations<String,Object> vo;
   SetOperations<String,Object> so;
    ZSetOperations<String,Object> zso;


    public void set(String key, Object value) {
        so = redisTemplate.opsForSet();
        zso = redisTemplate.opsForZSet();
        so.add("","");
        so.pop("");
        vo.set(key, value);
    }
    public Object get(String key) {
        return vo.get(key);
    }
}
