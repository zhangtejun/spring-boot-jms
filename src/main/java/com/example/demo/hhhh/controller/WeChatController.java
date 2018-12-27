package com.example.demo.hhhh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.hhhh.MsgType;
import com.example.demo.hhhh.mapper.TestMapper;
import com.example.demo.util.Utils;
import com.example.demo.weixin.WeiXinUtil;
import com.example.demo.weixin.news.ArticlesItem;
import com.example.demo.weixin.news.ArticlesMessage;
import com.thoughtworks.xstream.XStream;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
@Api("wechat")
@RequestMapping("wechat")
@RestController
@Slf4j
public class WeChatController {
    @Autowired
    public  TestMapper testMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @RequestMapping
    public Object checkSign(HttpServletRequest request) throws Exception {
        System.out.println("map--->"+request.getParameterMap());
        Enumeration<String> parameterNames = request.getParameterNames();
        for(Enumeration e=parameterNames;e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            String thisValue=request.getParameter(thisName);
            System.out.println(thisName+"--------------"+thisValue);
        }
        if (StringUtils.hasLength(request.getParameter("echostr"))){
            return Utils.checkSignature(request);
        }
        InputStream in = request.getInputStream();

        SAXReader reader = new SAXReader();
        //Document doc = reader.read(new File("C:\\Users\\Administrator\\Desktop\\q.txt"));
        Document doc = reader.read(in);
        Element root = doc.getRootElement();

        log.info("---------------------------->"+root.element("Content").getTextTrim());
        if (root.element("Content").getTextTrim().equals("1")){
            ArticlesMessage articlesMessage = WeiXinUtil.getBlogMessage(root.element("FromUserName").getTextTrim(),root.element("ToUserName").getTextTrim(),System.currentTimeMillis(),redisTemplate);

            // 将POST流转换为XStream对象
            XStream xs = Utils.createXstream();
            XStream.setupDefaultSecurity(xs);
            xs.allowTypes(new Class[]{ArticlesItem.class, ArticlesMessage.class});
            xs.processAnnotations(ArticlesMessage.class);
            xs.processAnnotations(ArticlesItem.class);
            // 将指定节点下的xml节点数据映射为对象
            xs.alias("xml", ArticlesMessage.class);
            xs.alias("item", ArticlesItem.class);
            log.info(xs.toXML(articlesMessage));
            return xs.toXML(articlesMessage);
        }
        return "<xml>" +
                " <ToUserName><![CDATA["+root.element("FromUserName").getTextTrim()+"]]></ToUserName>" +
                " <FromUserName><![CDATA[gh_5d4e90cb24df]]></FromUserName>" +
                " <CreateTime>"+System.currentTimeMillis()+"</CreateTime>" +
                " <MsgType><![CDATA[text]]></MsgType>" +
                " <Content><![CDATA["+checkMsgType(doc)+"]]></Content>" +
                " </xml>";
    }

    private String checkMsgType(Document doc) {
        Element root = doc.getRootElement();
        String value = root.element("MsgType").getTextTrim();// 消息类型
        System.out.println("msg类型"+value);
        if(MsgType.text.toString().equals(value)){// 文本消息
            String str = "{\"reqType\":0,\"perception\": {\"inputText\": {\"text\": \""+root.element("Content").getTextTrim()+"\"}},\"userInfo\": {\"apiKey\": \"bdc841e305e54a5d9db9180b5985f339\",\"userId\": \"zhangtejun\"}}";
            List<JSONObject> list = JSON.parseArray(JSON.parseObject(Utils.postJson("http://openapi.tuling123.com/openapi/api/v2", str)).getString("results"),JSONObject.class);
            JSONObject values = (JSONObject) list.get(0).get("values");
            return (String) values.get("text");
        }
        //System.out.println(doc.asXML());
        return  null;
    }
    @PostMapping("/createMenu")
    @ApiOperation(value="创建微信菜单", notes="创建微信菜单")
    public Object createMenu(){
        String menu = JSONObject.toJSONString(WeiXinUtil.initMenu());
        log.info("menu: "+menu);
        return WeiXinUtil.createMenu(menu,testMapper);
    }
    @GetMapping("/deleteMenu")
    @ApiOperation(value="删除微信菜单", notes="删除微信菜单")
    public Object delMenu(){
        return WeiXinUtil.deleteMenu(testMapper);
    }

}
