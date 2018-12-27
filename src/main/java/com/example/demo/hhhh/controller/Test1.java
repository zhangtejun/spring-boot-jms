package com.example.demo.hhhh.controller;

import com.example.demo.hhhh.mapper.TestMapper;
import com.example.demo.util.HTMLUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Api("testApi")
@RequestMapping("/RM")
public class Test1 {
    @Autowired
private TestMapper testMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public  Object getUserById(@PathVariable(value = "id") Integer id){

        System.out.println("------------> getUserById"+testMapper.findById(String.valueOf(id)));
        return "ok: "+testMapper.findById(String.valueOf(id));
    }

    /**
     * 查询用户列表
     * @return
     */
    @ApiOperation(value="获取用户列表", notes="获取用户列表")
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public Object getUserList (){
        System.out.println("------------> getUserList"+testMapper.findAll());
        return "ok: getUserList"+testMapper.findAll();
    }
    /**
     * 查询用户列表
     * @return
     */
    @ApiOperation(value="获取用户列表", notes="获取用户列表")
    @RequestMapping(value = "users", method = RequestMethod.POST)
    public Object getUserList1 (){
        System.out.println("------------> getUserList"+testMapper.findAll());
        return "ok: getUserList"+testMapper.findAll();
    }

    /**
     * redis test
     * @return
     */
    @ApiOperation(value="redisTest", notes="redisTest")
    @RequestMapping(value = "redisTest/{id}", method = RequestMethod.GET)
    public Object redistest (@PathVariable(value = "id") Integer id){
        System.out.println("------------> getUserById id: "+id);
        //return "ok: redisTest"+redisTemplate.opsForSet().pop("myt");
        return "ok: redisTest"+redisTemplate.opsForSet().randomMember("myt");
    }

    /**
     * redis test
     * @return
     */
    @ApiOperation(value="redisTestSave", notes="redisTestSave")
    @RequestMapping(value = "redisTestSave/{id}", method = RequestMethod.GET)
    public Object redistestSave (@PathVariable(value = "id") Integer id) throws IOException {
        System.out.println("------------> getUserById id: "+id);
        Map map =  new HashMap<>();
        map.put("defult",String.format("通过id 未查询到用户信息 id%d", id));


        Elements elements = HTMLUtils.getEs();
        for (int i=0;i<elements.size();i++){
            Element e = elements.get(i);
            map.put("href",e.attr("href"));
            map.put("title",e.attr("title"));
            System.out.println(e.attr("href"));
            System.out.println(e.attr("title"));
            redisTemplate.opsForSet().add("myt",map);
        }
        return "ok: redisTestSave";
    }

}
