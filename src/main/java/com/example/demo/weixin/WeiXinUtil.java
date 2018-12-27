package com.example.demo.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.hhhh.MsgType;
import com.example.demo.hhhh.mapper.TestMapper;
import com.example.demo.util.Utils;
import com.example.demo.weixin.news.ArticlesItem;
import com.example.demo.weixin.news.ArticlesMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.xml.bind.Element;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class WeiXinUtil {
    public static final String WEIXIN_MENU_DEL_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete";
    public static final String WEIXIN_GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 生成菜单
     * @return
     */
    public static Menu initMenu() {
        Menu menu = new Menu();

        ClickButton button11 = new ClickButton();
        button11.setName("一级菜单1");
        button11.setType("click");
        button11.setKey("11");

        ClickButton button12 = new ClickButton();
        button12.setName("二级菜单1");
        button12.setType("click");
        button12.setKey("12");

        ViewButton button21 = new ViewButton();
        button21.setName("一级菜单2");
        button21.setType("view");
        button21.setUrl("http://www.zhtjunmq.xyz");

        ViewButton button22 = new ViewButton();
        button22.setName("二级菜单2");
        button22.setType("view");
        button22.setUrl("http://www.zhtjunmq.xyz");

        ClickButton button31 = new ClickButton();
        button31.setName("一级菜单3");

        button31.setType("click");
        button31.setKey("31");

        Button button1 = new Button();
        button1.setName("一级菜单A"); //将11/12两个button作为二级菜单封装第一个一级菜单
        button1.setSubButton(new Button[]{button11, button12});

        Button button2 = new Button();
        button2.setName("相关网址"); //将21/22两个button作为二级菜单封装第二个二级菜单
        button2.setSubButton(new Button[]{button11, button12});

        menu.setButton(new Button[]{button1, button2, button31});// 将31Button直接作为一级菜单
        return menu;
    }

    /**
     * 删除菜单
     * @return
     */
    public static String deleteMenu(TestMapper testMapper) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("access_token", getAccessToken(testMapper)));
        return Utils.doGet(WEIXIN_MENU_DEL_URL,params);
    }

    public static String getAccessToken(TestMapper testMapper) {
        Map map = testMapper.find();
        if (map !=null && !map.isEmpty()){
            int expires_in = (int) map.get("expires_in");//过期时间
            Date date = (Date) map.get("date");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
            date = calendar.getTime();

            if(System.currentTimeMillis() -date.getTime()>expires_in*1000){
                JSONObject jsonObject = getAccessTokenByGet(testMapper);
                //更新
                if (StringUtils.hasLength((String) jsonObject.get("access_token"))) {
                    testMapper.updateAccessToken((String) jsonObject.get("access_token"), (Integer) jsonObject.get("expires_in"));
                }
                return  (String) jsonObject.get("access_token");
            }
            return (String) map.get("access_token");
        }else{
            JSONObject jsonObject = getAccessTokenByGet(testMapper);
            //更新
            if (StringUtils.hasLength((String) jsonObject.get("access_token"))) {
                testMapper.saveAccessToken((String) jsonObject.get("access_token"), (Integer) jsonObject.get("expires_in"));
            }
            return  (String) jsonObject.get("access_token");
        }
    }

    public static JSONObject getAccessTokenByGet(TestMapper testMapper) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "client_credential"));
        params.add(new BasicNameValuePair("appid", "wx9ab7d11fc11597e5"));
        params.add(new BasicNameValuePair("secret", "9d78efe6f918e221144e8ee3f9e8ee1d"));
        log.info("getAccessTokenByGet-------------------------》》》》》");
        JSONObject ret = JSON.parseObject(Utils.doGet(WEIXIN_GET_TOKEN_URL,params));
        return  ret;
    }

    public static int createMenu(String menu,TestMapper testMapper) {
        int result = 0;

        JSONObject jsonObject =JSONObject.parseObject( Utils.postJson("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+getAccessToken(testMapper), menu));
        if(jsonObject != null){
            result = jsonObject.getIntValue("errcode");
        }
        return result;
    }



    public static ArticlesMessage getBlogMessage(String toUserName, String fromUserName, Long createTime, RedisTemplate<String,Object> redisTemplate) {
        ArticlesMessage articlesMessage = new ArticlesMessage();
        articlesMessage.setFromUserName(fromUserName);
        articlesMessage.setToUserName(toUserName);
        articlesMessage.setCreateTime(createTime);
        articlesMessage.setMsgType(MsgType.news.toString());


        List<ArticlesItem> articles = new ArrayList<>();
        ArticlesItem item1 = new ArticlesItem();

        HashMap<String,String> myt = (HashMap) redisTemplate.opsForSet().randomMember("myt");

        item1.setTitle(myt.get("title"));
        item1.setDescription(myt.get("title"));
        item1.setPicUrl("http://t2.hddhhn.com/uploads/tu/201610/198/scx30045vxd.jpg");
        item1.setUrl(myt.get("href"));
        articles.add(item1);

        articlesMessage.setArticleCount(articles.size());
        articlesMessage.setArticles(articles);
        return articlesMessage;
    }

}
