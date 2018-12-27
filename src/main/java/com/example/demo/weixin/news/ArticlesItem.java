package com.example.demo.weixin.news;


import com.example.demo.hhhh.XStreamCDATA;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;

@XStreamAlias("item")
@Data
public class ArticlesItem  implements Serializable {
    @XStreamAlias("Title")
    @XStreamCDATA
    private String Title;

    @XStreamAlias("Description")
    @XStreamCDATA
    private String Description;

    @XStreamAlias("PicUrl")
    @XStreamCDATA
    private String PicUrl;

    @XStreamAlias("Url")
    @XStreamCDATA
    private String Url;
}
