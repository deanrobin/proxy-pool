package com.dean.proxy.mq.producer;

import com.dean.proxy.mq.base.MqBaseConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class ProducerTagsKeys {
    public static void main(String[] args) throws Exception {
        // 指定生产组名为my-producer
        DefaultMQProducer producer = new DefaultMQProducer(MqBaseConfig.PRODUCER_GROUP);
        // 配置namesrv地址
        producer.setNamesrvAddr(MqBaseConfig.IP_PORT);
        // 启动Producer
        producer.start();
        // 创建消息对象，topic为：myTopic001，消息内容为：hello world，且tags为：test-tags，keys为test-keys
        Message msg = new Message("myTopic001", "test-tag", "test-keys", "hello world..t1".getBytes());
        // 自定义属性
        msg.putUserProperty("a", "1");

        // 发送消息到mq，同步的
        SendResult result = producer.send(msg);
        System.out.println("发送消息成功！result is : " + result);
        // 关闭Producer
        producer.shutdown();
        System.out.println("生产者 shutdown！");
    }
}
