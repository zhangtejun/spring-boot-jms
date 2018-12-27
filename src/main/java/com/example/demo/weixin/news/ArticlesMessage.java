package com.example.demo.weixin.news;


import com.example.demo.hhhh.XStreamCDATA;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.List;

/**
 * 图文消息类
 */
@Data
public class ArticlesMessage extends  BaseMessage {
    @XStreamAlias("ArticleCount")
    private int ArticleCount;

    @XStreamAlias("Articles")
    private List<ArticlesItem> Articles;
}
