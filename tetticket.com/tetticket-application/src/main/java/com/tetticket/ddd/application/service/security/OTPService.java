package com.tetticket.ddd.application.service.security;

import com.tetticket.ddd.application.service.mail.EmailService;
import com.tetticket.ddd.domain.model.entity.Users;
import com.tetticket.ddd.infrastructure.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;
@Service
@Slf4j
@RequiredArgsConstructor 
public class OTPService {
        private final UsersRepository userRepository;
        private final EmailService emailService;
        private final RedissonClient redissonClient;

        private static final long OTP_EXPIRATION_MINUTES = 3;
        private static final String ERROR_EMAIL_ALREADY_REGISTERED = "Email is already registered";
        private static final String ERROR_CURRENT_EMAIL_NOT_REGISTERED = "Current email is not registered";
        private static final String ERROR_NEW_EMAIL_ALREADY_REGISTERED = "New email is already registered";
        private static final String ERROR_EMAIL_NOT_REGISTERED = "Email is not registered";

    /**
     * Sends a one-time password (OTP) to the given email address for user registration.
     * If the email address is already registered, an exception is thrown.
     *
     * @param email the email address to which the OTP will be sent
     * @throws IllegalArgumentException if the email address is already registered
     */
    public void sendOTPForRegistration(String email) {
        if (userRepository.findByEmail(email) != null) {
            log.warn("Attempt to register with already registered email: {}", email);
            throw new IllegalArgumentException(ERROR_EMAIL_ALREADY_REGISTERED);
        }

        String otp = generateAndSaveOTP(email);
        emailService.sendOTPEmail(email, otp);
    }

    /**
     * Generates a one-time password (OTP) and associates it with the given email by saving it for further use.
     *
     * @param email the email address for which the OTP will be generated and saved
     * @return the generated OTP as a string
     */
    private String generateAndSaveOTP(String email) {
        String otp = generateOTP();
        saveOTP(email, otp);
        return otp;
    }

    /**
     * Sends a one-time password (OTP) to facilitate the email change process for a user.
     *
     * This method validates the current and new email addresses. It ensures that the current
     * email is registered and the new email is not yet associated with a user. If the validation
     * passes, an OTP is generated and sent to the new email address.
     *
     * @param currentEmail the current email address of the user that is registered in the system
     * @param newEmail the new email address to which the OTP will be sent for verification
     * @throws IllegalArgumentException if the current email is not registered or the new email is already registered
     */
    public void sendOTPForChangeEmail(String currentEmail, String newEmail) {
        Users user = userRepository.findByEmail(currentEmail);
        if (user == null) {
            log.warn("Attempt to change email for unregistered current email: {}", currentEmail);
            throw new IllegalArgumentException(ERROR_CURRENT_EMAIL_NOT_REGISTERED);
        }

        if (userRepository.findByEmail(newEmail) != null) {
            log.warn("Attempt to change to an already registered new email: {}", newEmail);
            throw new IllegalArgumentException(ERROR_NEW_EMAIL_ALREADY_REGISTERED);
        }

        String otp = generateAndSaveOTP(newEmail);
        emailService.sendOTPEmail(newEmail, otp);
    }

    /**
     * Sends a one-time password (OTP) to the given email address for the purpose of
     * resetting the user's password. Ensures the email is associated with a registered user.
     *
     * @param email the email address of the user requesting the OTP for password reset
     * @throws IllegalArgumentException if the provided email is not registered
     */
    public void sendOTPForForgotPassword(String email) {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Attempt to reset password for unregistered email: {}", email);
            throw new IllegalArgumentException(ERROR_EMAIL_NOT_REGISTERED);
        }

        String otp = generateAndSaveOTP(email);
        emailService.sendOTPEmail(email, otp);
    }

    /**
     * Generates a random 6-digit one-time password (OTP).
     *
     * @return a randomly generated 6-digit OTP as a string
     */
    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }


    /**
     * Saves the provided one-time password (OTP) associated with a specific email address
     * into a Redis storage with a predefined expiration time.
     *
     * @param email the email address to associate with the OTP
     * @param otp   the one-time password to be stored
     */
    public void saveOTP(String email, String otp) {
                RBucket<String> bucket = redissonClient.getBucket(email);
                bucket.set(otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
                log.info("OTP saved for email: {}", email);
            }

            /**
             * Retrieves a stored one-time password (OTP) associated with the specified email
             * from the Redis data store.
             *
             * @param email the email address associated with the OTP to retrieve
             * @return the OTP stored in Redis for the specified email, or {@code null} if no OTP is found
             */
            public String getOTPFromRedis(String email) {
                RBucket<String> bucket = redissonClient.getBucket(email);
                return bucket.get();
            }
            
            /**
             * Validates whether the provided OTP matches the stored OTP for a given email.
             *
             * @param email the email address associated with the OTP
             * @param otp   the one-time password to be validated
             * @return true if the provided OTP matches the stored OTP for the email, false otherwise
             */
            public boolean isOTPValid(String email, String otp) {
                String storedOtp = getOTPFromRedis(email);
                boolean isValid = storedOtp != null && storedOtp.equals(otp);
                log.info("OTP validation for email: {}, isValid: {}", email, isValid);
                return isValid;
            }
    }