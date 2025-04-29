package com.tetticket.ddd.application.service.user_profile;

import com.amazonaws.services.kms.model.NotFoundException;
import com.tetticket.ddd.application.mapper.UserProfileMapper;
import com.tetticket.ddd.application.model.UserProfileDTO;
import com.tetticket.ddd.application.service.util.aws.S3Service;
import com.tetticket.ddd.domain.model.entity.UserProfile;
import com.tetticket.ddd.infrastructure.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final S3Service s3Service;


    // SERVICE GET PROFILE -> AFTER TURN ON AUTHORIZATION
    @Transactional
    public UserProfileDTO createProfile(UserProfileDTO profileDTO) {
        try {
            Long userId = profileDTO.getUserId();
            if (!userProfileRepository.existsById(userId)) {
                throw new IllegalArgumentException("User ID does not exist in the users table");
            }

            UserProfile profile = UserProfileMapper.mapperToUserProfile(profileDTO);
            UserProfile savedProfile = userProfileRepository.save(profile);
            return UserProfileMapper.mapperToUserProfileDTO(savedProfile);
        } catch (IllegalArgumentException e) {
            log.error("Invalid user ID provided: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error creating user profile: {}", e.getMessage());
            throw new RuntimeException("Failed to create user profile", e);
        }
    }

    public CompletableFuture<Void> uploadProfilePicture(Long userId, MultipartFile profilePicture) {
        try {
            return CompletableFuture.supplyAsync(() -> s3Service.uploadProfilePicture(userId, profilePicture))
                    .thenAccept(imageUrl -> {
                        try {
                            UserProfile profile = userProfileRepository.findById(userId)
                                    .orElseThrow(() -> new NotFoundException("Profile not found"));
                            profile.setProfile_picture(String.valueOf(imageUrl));
                            userProfileRepository.save(profile);
                        } catch (NotFoundException e) {
                            log.error("Profile not found for user ID {}: {}", userId, e.getMessage());
                            throw e;
                        } catch (Exception e) {
                            log.error("Error updating profile picture: {}", e.getMessage());
                            throw new RuntimeException("Failed to update profile picture", e);
                        }
                    });
        } catch (Exception e) {
            log.error("Error in upload profile picture process: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
// END
    
    
    
    
}
