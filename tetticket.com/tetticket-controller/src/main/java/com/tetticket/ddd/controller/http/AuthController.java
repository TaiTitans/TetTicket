package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.service.security.OTPService;
import com.tetticket.ddd.controller.model.ResultForm;
import com.tetticket.ddd.controller.model.enums.ResultCode;
import com.tetticket.ddd.controller.model.enums.ResultUtil;
import com.tetticket.ddd.controller.model.vo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private OTPService otpService;

    /**
     * Sends a one-time password (OTP) to the provided email address for user sign-up.
     * This method ensures that the OTP is dispatched successfully or returns an appropriate error message in case of failure.
     *
     * @param email the email address to which the OTP will be sent
     * @return a ResponseEntity containing a ResultMessage with the success status, message, result, and code
     */
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




}
