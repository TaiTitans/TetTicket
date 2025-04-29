package com.tetticket.ddd.application.mapper;

import com.tetticket.ddd.application.model.UserProfileDTO;
import com.tetticket.ddd.domain.model.entity.UserProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {
    public static UserProfileDTO mapperToUserProfileDTO(UserProfile userProfile) {
        if(userProfile == null) return null;

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(userProfile, userProfileDTO);

        return userProfileDTO;
    }
    public static UserProfile mapperToUserProfile(UserProfileDTO userProfileDTO) {
        if(userProfileDTO == null) return null;

        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(userProfileDTO, userProfile);

        return userProfile;
    }
}
