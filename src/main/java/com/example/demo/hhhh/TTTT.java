package com.example.demo.hhhh;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

/*@RestController*/
public class TTTT {
    @Autowired
    Producter producter;

    @RequestMapping("/tttt")
    public  String  tt(){
        Destination p2pMsg = new ActiveMQQueue("msg.p2p.queue");
        producter.sendMessage(p2pMsg , "hello , this is jms msg");
        return "tttt";
    }
}
