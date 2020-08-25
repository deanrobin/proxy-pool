package com.dean.proxy.mq.consumer;

import java.util.List;

import com.alibaba.fastjson.JSON;

import com.dean.proxy.mq.base.MqBaseConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

public class ConsumerOrder {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my-consumer");
        consumer.setNamesrvAddr(MqBaseConfig.IP_PORT);
        consumer.subscribe("orderTopic", "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println(new String(msg.getBody()) + " Thread:" + Thread.currentThread().getName() + " queueid:" + msg.getQueueId());
                    //System.out.println("brokerName:" + msg.getBrokerName());
                    //System.out.println("msg id:" + msg.getMsgId());
                    //System.out.println("buy id:" + msg.getBuyerId());
                    //System.out.println("a:" + msg.getProperty("args"));
                    System.out.println(JSON.toJSON(msg.getProperties()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        // 最大线程数1
        consumer.setConsumeThreadMax(1);
        // 最小线程数
        consumer.setConsumeThreadMin(1);

        consumer.start();
        System.out.println("Consumer start...");
    }
}
