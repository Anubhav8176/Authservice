package com.anucodes.authservice.serializer;

import com.anucodes.authservice.model.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, UserInfoDto userInfoDto) {
        byte[] returnVal = null;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            returnVal = objectMapper.writeValueAsString(userInfoDto).getBytes();
        }catch (Exception exception){
            exception.printStackTrace();
        }

        return returnVal;

    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
