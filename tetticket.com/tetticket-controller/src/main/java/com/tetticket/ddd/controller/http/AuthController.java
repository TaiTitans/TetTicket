package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.application.service.security.OTPService;
import com.tetticket.ddd.application.service.users.UsersService;
import com.tetticket.ddd.controller.model.enums.ResultCode;
import com.tetticket.ddd.controller.model.vo.ResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    @Autowired
    private OTPService otpService;
    @Autowired
    private UsersService usersService;



    /**
     * Sends a one-time password (OTP) to the provided email address for user sign-up.
     * This method ensures that the OTP is dispatched successfully or returns an appropriate error message in case of failure.
     *
     * @param email the email address to which the OTP will be sent
     * @return a ResponseEntity containing a ResultMessage with the success status, message, result, and code
     */
    @Operation(
            summary = "Send OTP for Sign Up",
            description = "Sends a one-time password (OTP) to the provided email address for user sign-up."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Email already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server")
    })
    @PostMapping("/otp/sign-up")
    public ResponseEntity<ResultMessage<String>> sendOTPForSignUp(@RequestParam("email") String email) {
        try {
            otpService.sendOTPForRegistration(email);
            return ResponseEntity.ok(
                    new ResultMessage<String>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult("OTP sent successfully")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(e.getMessage())
                            .setCode(ResultCode.BAD_REQUEST.code())
                            .setResult("Failed to send OTP")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code())
                            .setResult("Failed to send OTP")
            );
        }
    }

    /**
     * Sends a one-time password (OTP) to facilitate changing a user's email address.
     * This method validates the provided current and new email addresses, ensuring that
     * the current email belongs to a registered user and the new email is not yet registered.
     * If validation succeeds, an OTP is sent to the new email address.
     *
     * @param currentEmail the current email address of the user, which must be registered in the system
     * @param newEmail the new email address to which the OTP will be sent for verification
     * @return a ResponseEntity containing a ResultMessage with the success status,
     *         a message indicating the result, a status code, and the result of the OTP operation
     */
    @Operation(
            summary = "Send OTP for Change Email",
            description = "Sends a one-time password (OTP) to the provided email address for changing the user's email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Current email not registered or new email already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server")
    })
    @PostMapping("/otp/change-email")
    public  ResponseEntity<ResultMessage<String>> sendOTPForChangeEmail(
            @RequestParam("currentEmail") String currentEmail,
            @RequestParam("newEmail") String newEmail) {
        try{
            otpService.sendOTPForChangeEmail(currentEmail, newEmail);
            return ResponseEntity.ok(
                    new ResultMessage<String>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult("OTP sent successfully")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(e.getMessage())
                            .setCode(ResultCode.BAD_REQUEST.code())
                            .setResult("Failed to send OTP")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code())
                            .setResult("Failed to send OTP")
            );
        }
    }
    /**
     * Sends a one-time password (OTP) to the provided email address for the forgotten password feature.
     * This method attempts to send an OTP to the specified email and returns the result of the operation.
     * If the email is invalid or an unexpected error occurs, appropriate error messages are returned.
     *
     * @param email the email address to which the OTP will be sent
     * @return a ResponseEntity containing a ResultMessage with the success status, message, result, and code
     */
    @Operation(
            summary = "Send OTP for Forgot Password",
            description = "Sends a one-time password (OTP) to the provided email address for the forgotten password feature."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Email not registered"),
            @ApiResponse(responseCode = "500", description = "Internal server")
    })
    @PostMapping("/otp/forgot-password")
    public ResponseEntity<ResultMessage<String>> sendOTPForForgotPassword(@RequestParam("email") String email) {
        try {
            otpService.sendOTPForForgotPassword(email);
            return ResponseEntity.ok(
                    new ResultMessage<String>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult("OTP sent successfully")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(e.getMessage())
                            .setCode(ResultCode.BAD_REQUEST.code())
                            .setResult("Failed to send OTP")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code())
                            .setResult("Failed to send OTP")
            );
        }
    }

    /**
     * Handles the sign-up process for registering a new user. This method validates
     * the provided registration details and OTP, maps the user information from the
     * DTO to an entity, assigns default roles, encodes the password, and persists
     * the user entity in the database.
     *
     * @param usersDTO The data transfer object containing user registration details.
     * @param otp The one-time password provided for verifying the user's registration.
     * @return A ResponseEntity containing a ResultMessage indicating the success or
     *         failure of the registration process, along with an appropriate result message
     *         and HTTP status code.
     */
    @Operation(
            summary = "Sign Up Account",
            description = "Handles the registration process for a new user. Validates the provided registration information and OTP, maps the user details from DTO to entity, assigns default roles, encodes the password, and persists the user entity."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration details or OTP"),
            @ApiResponse(responseCode = "500", description = "Internal server")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<ResultMessage<String>> signUp(@RequestBody UsersDTO usersDTO,
                                                          @RequestParam("otp") String otp) {
        try {
            usersService.signUp(usersDTO, otp);
            return ResponseEntity.ok(
                    new ResultMessage<String>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult("User registered successfully")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(e.getMessage())
                            .setCode(ResultCode.BAD_REQUEST.code())
                            .setResult("Failed to register user")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ResultMessage<String>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code())
                            .setResult("Failed to register user")
            );
        }
    }

}
