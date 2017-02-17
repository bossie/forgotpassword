package org.bossie.forgotpassword;

import com.lambdaworks.crypto.SCryptUtil;
import org.bossie.forgotpassword.PendingForgotPasswordRequestRepository.PendingForgotPasswordRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public interface ForgotPasswordService {
    void forgotPassword(String emailAddress);
    void changeForgottenPassword(String emailAddress, String token, String password, String passwordConfirm);
}

class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final PendingForgotPasswordRequestRepository pendingForgotPasswordRequestRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final Duration expiryTime;

    ForgotPasswordServiceImpl(PendingForgotPasswordRequestRepository pendingForgotPasswordRequestRepository, EmailService emailService, UserRepository userRepository, Duration expiryTime) {
        this.pendingForgotPasswordRequestRepository = pendingForgotPasswordRequestRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.expiryTime = expiryTime;
    }

    @Override
    public void forgotPassword(String emailAddress) {
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(expiryTime);

        pendingForgotPasswordRequestRepository.addForgotPasswordRequest(emailAddress, hash(token), expiresAt);
        emailService.sendMail(emailAddress, token);
    }

    @Override
    public void changeForgottenPassword(String emailAddress, String token, String password, String passwordConfirm) {
        if (password.equals(passwordConfirm)) {
            PendingForgotPasswordRequest request = pendingForgotPasswordRequestRepository.getForgotPasswordRequest(emailAddress);

            if (isStillValid(request)) {
                if (matches(token, request.hashedToken)) {
                    userRepository.updatePassword(emailAddress, password);
                    pendingForgotPasswordRequestRepository.removeForgotPasswordRequest(emailAddress);
                } else {
                    throw new RuntimeException("invalid token");
                }
            } else {
                throw new RuntimeException("no pending forgot password requests");
            }
        } else {
            throw new RuntimeException("passwords do not match");
        }
    }

    private static String hash(String token) {
        int N = 16384;
        int r = 8;
        int p = 1;

        return SCryptUtil.scrypt(token, N, r, p);
    }

    private static boolean isStillValid(PendingForgotPasswordRequest request) {
        return request != null && Instant.now().isBefore(request.expiresAt);
    }

    private static boolean matches(String token, String hashedToken) {
        return SCryptUtil.check(token, hashedToken);
    }
}
