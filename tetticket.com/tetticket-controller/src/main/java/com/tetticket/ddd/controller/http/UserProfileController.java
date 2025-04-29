package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.model.UserProfileDTO;
import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.application.service.user_profile.UserProfileService;
import com.tetticket.ddd.controller.model.enums.ResultCode;
import com.tetticket.ddd.controller.model.vo.ResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user-profile")
@Tag(name = "UserProfile", description = "UserProfile management APIs")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Operation(
            summary = "Create new user profile",
            description = "This endpoint allows a new user profile to be created."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server")
    })
    @PostMapping("/create")
    public ResponseEntity<ResultMessage<String>> signUp(@Valid @RequestPart("dto") UserProfileDTO userProfileDTO,
                                                        @RequestPart("file") MultipartFile file) {
        try {
            UserProfileDTO profile = userProfileService.createProfile(userProfileDTO);
            userProfileService.uploadProfilePicture(profile.getUserId(), file);
            return ResponseEntity.ok(
                    new ResultMessage<String>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult("User profile created successfully")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(e.getMessage())
                            .setCode(ResultCode.BAD_REQUEST.code())
                            .setResult("Invalid input data")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code())
                            .setResult("Internal server error")
            );
        }
    }

}
