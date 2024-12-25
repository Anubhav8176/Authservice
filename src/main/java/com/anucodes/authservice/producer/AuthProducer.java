package com.anucodes.authservice.producer;

import com.anucodes.authservice.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class AuthProducer {

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    private KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Autowired
    AuthProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoDto userInfoDto){
        Message<UserInfoDto> message = MessageBuilder.withPayload(userInfoDto)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME).build();
        kafkaTemplate.send(message);
    }

}
