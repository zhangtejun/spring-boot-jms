package com.example.demo.hhhh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.time.Instant;

/**
 * Description: 消息生产者
 */
/*@Component*/
public class Producter {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    /**
     *
     * @param destination Destination为接收者队列也就是消息的目的地（消息发送给谁）
     * @param message message消息内容
     */
    public void sendMessage(Destination destination, final String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @JmsListener(destination = "return.queue")
    public void receivingMsg(String content){
        System.out.println(Instant.now().getEpochSecond() + " -Receiving a message : " +  content);
    }

}
