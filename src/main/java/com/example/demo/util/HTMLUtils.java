package com.example.demo.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTMLUtils {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://www.importnew.com/").get();
        Element element =  doc.getElementById("tab-latest-comments");
        Elements href = element.getElementsByAttribute("href");
        for (int i=0;i<href.size();i++){
            Element e = href.get(i);
            System.out.println(e.attr("href"));
            System.out.println(e.attr("title"));
        }

    }

    public static  Elements getEs() throws IOException {
        Document doc = Jsoup.connect("http://www.importnew.com/").get();
        Element element =  doc.getElementById("tab-latest-comments");
        Elements href = element.getElementsByAttribute("href");
        return href;
    }
}
