package com.example.demo.hhhh;

import org.jboss.logging.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;


/**
 * 消息消费者
 */
/*@Component*/
public class Consumer {
    String pageName = "tttt";
    private static final Logger LOGGER = Logger.getLogger(Consumer.class);
    String serverIp = "localhost";
    @JmsListener(destination = "msg.p2p.queue")
    @SendTo("return.queue")
    public String  processMessage(String content) {

        for (int i = 0; i < 10; i++) {
            LOGGER.error("Info log [" + i + "].");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

       /* double amount = 0;
        Transaction t = Cat.newTransaction("URL", "tttt");

        try {
            Cat.logEvent("URL.Server", serverIp, Event.SUCCESS, "ip=" + serverIp + "&...");
            Cat.logMetricForCount("PayCount");
            Cat.logMetricForSum("PayAmount", amount);
            System.out.println("Receiving a message------>>>>> :" + content);
            t.setStatus(Transaction.SUCCESS);
        }catch (Exception e) {
            t.setStatus(e);
        }finally {
            t.complete();
        }*/

        return "return mesage" + content;
    }

}
