package com.tetticket.ddd.application.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserProfileDTO {
    private Long userProfile;
    private String fullName;
    private Date age;
    private Integer phoneNumber;
    private String gender;
    private String address;
    private String profilePicture;
    private Long userId;
}
