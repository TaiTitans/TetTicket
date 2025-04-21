package com.tetticket.ddd.application.service.users;

import com.tetticket.ddd.application.mapper.UsersMapper;
import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.application.service.security.OTPService;
import com.tetticket.ddd.domain.model.entity.Roles;
import com.tetticket.ddd.domain.model.entity.Users;
import com.tetticket.ddd.infrastructure.repository.RolesRepository;
import com.tetticket.ddd.infrastructure.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final OTPService otpService;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Handles the registration process for a new user. Validates the provided
     * registration information and OTP, maps the user details from DTO to entity,
     * assigns default roles, encodes the password, and persists the user entity.
     * Logs success or failure during the process.
     *
     * @param usersDTO the data transfer object containing user registration details
     *                 such as username, email, and password
     * @param otp      the one-time password provided for additional security during registration
     * @throws IllegalArgumentException if the username, email already exists, or if the OTP is invalid
     * @throws RuntimeException         if there is an error during user registration
     */
    public void signUp(UsersDTO usersDTO, String otp) {
        validateRegistration(usersDTO, otp);

        try {
            Users user = usersMapper.mapperToUsers(usersDTO);
            prepareAndSaveUser(user);
            log.info("Successfully registered new user with username: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            throw new RuntimeException("Failed to complete user registration", e);
        }
    }

    private void validateRegistration(UsersDTO usersDTO, String otp) {
        if (usersRepository.findByUsername(usersDTO.getUsername()) != null) {
            log.warn("Attempted registration with existing username: {}", usersDTO.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        if (usersRepository.findByEmail(usersDTO.getEmail()) != null) {
            log.warn("Attempted registration with existing email: {}", usersDTO.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        if (!otpService.isOTPValid(usersDTO.getEmail(), otp)) {
            log.warn("Invalid OTP attempt for email: {}", usersDTO.getEmail());
            throw new IllegalArgumentException("Invalid OTP");
        }
    }

    private void prepareAndSaveUser(Users user) {
        Roles defaultRole = rolesRepository.findByRole_name("ROLE_CUSTOMER");
        if (defaultRole == null) {
            throw new RuntimeException("Default role not found");
        }
        user.getRoles().add(defaultRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }



}