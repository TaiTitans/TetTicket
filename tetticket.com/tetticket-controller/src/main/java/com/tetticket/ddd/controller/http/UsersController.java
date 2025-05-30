package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.application.service.users.UsersService;
import com.tetticket.ddd.controller.model.enums.ResultCode;
import com.tetticket.ddd.controller.model.vo.ResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/api/v1/users")
@RestController
@Tag(name = "Users", description = "Users management APIs")
public class UsersController {
    @Autowired
    private UsersService usersAppService;

    @Operation(
            summary = "Signs up a new user",
            description = "This endpoint allows a new user to sign up by providing their email address and password."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User signed up successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<ResultMessage<String>> signUp(@Valid @RequestBody UsersDTO usersDTO,
            @PathVariable("otp") String otp) {
        try {
            usersAppService.signUp(usersDTO, otp);
            return ResponseEntity.ok(
                    new ResultMessage<String>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult("User signed up successfully")
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
