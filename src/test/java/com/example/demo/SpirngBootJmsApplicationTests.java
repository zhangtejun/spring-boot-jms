package com.example.demo;

import com.example.demo.hhhh.Producter;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpirngBootJmsApplicationTests {

    @Autowired
    private Producter producter;

    @Test
    public void contextLoads() {
        // 参考https://www.jianshu.com/p/c345a6df002e
        Destination p2pMsg = new ActiveMQQueue("msg.p2p.queue");

        producter.sendMessage(p2pMsg , "hello , this is jms msg");
    }

}
