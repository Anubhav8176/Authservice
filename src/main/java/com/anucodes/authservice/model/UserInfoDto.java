package com.anucodes.authservice.model;


import com.anucodes.authservice.entity.UserInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
