package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.abc.Stack;
import com.example.demo.abc.StackImplementByArray;
import com.example.demo.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class tt {
    @Test
   public  void TestStack(){
        Stack stack = new Stack();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        System.out.println(stack.size());
        System.out.println(stack.pop());
    }
    @Test
    public  void TestStack1(){
        StackImplementByArray stack = new StackImplementByArray(5);
        /*for (int i =0;i<40000;i++){

        }*/
        log.info("");


        ArrayList arrayList = new ArrayList();

        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");
        stack.push("5");
        stack.push("6");
        stack.push("7");
        stack.push("30");
        stack.push("31");
        stack.push("32");
        stack.push("33");
        stack.push("34");
        stack.push("35");
        stack.push("36");
        /**
         * 1234567
         * 6 0  7234561
         * 5 1  7634521
         * 4 2  7654321
         * 3 3   ->
         *
         */
 /*       System.out.println(stack.size());
        System.out.println(stack.pop());
        System.out.println(stack.size());
        System.out.println(stack.pop());
        System.out.println(stack.size());
        System.out.println(stack.pop());*/
    }

    @Test
    public void test1() throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\day.txt");
        File file1 = new File("C:\\Users\\Administrator\\Desktop\\dayOut.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        Writer out = new FileWriter(file1);
        List<NameValuePair> params = new ArrayList<>();
        BufferedReader br = new BufferedReader(isr);
        String lineTxt = null;
        while ((lineTxt = br.readLine()) != null) {
            System.out.println(lineTxt);
            params.clear();
            params.add(new BasicNameValuePair("date", lineTxt));
            //工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2
            String s = Utils.doGet("http://api.goseek.cn/Tools/holiday", params);
            JSONObject jsonObject = JSONObject.parseObject(s);
            int val = (int) jsonObject.get("data");
            if (val == 1 || val ==2){
                //INSERT INTO finance.fs_holiday (date) VALUES ('20180512');
                out.write("INSERT INTO fs_holiday (date) VALUES ('"+lineTxt+"');");
                out.write("\r\n");
            }
        }
        br.close();
        out.close();
    }
    //获得明年第一天的日期
    @Test
    public void getNextYearFirst() throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\day.txt");
        Writer out = new FileWriter(file);
        String str = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR,1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str=sdf.format(lastDate.getTime());
        System.out.println(str);
        out.write(str);
        out.write("\r\n");
        for (;;){
            lastDate.roll(Calendar.DAY_OF_YEAR,1);
            str=sdf.format(lastDate.getTime());
            System.out.println(str);
            out.write(str);
            if (getNextYearEnd().equals(str)){
                break;
            }else{
                out.write("\r\n");
            }
        }
        out.close();
    }

    //获得明年最后一天的日期
    public static String getNextYearEnd(){
        String str = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR,1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str=sdf.format(lastDate.getTime());
        return str;
    }
    @Test
    public void dsd(){
        System.out.println("hotel_reserve_number".toUpperCase());
    }
}
