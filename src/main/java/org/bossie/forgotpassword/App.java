package org.bossie.forgotpassword;

import java.time.Duration;

public class App {
    public static void main(String... args) {
        MailServer mailServer = new DummyMailServer();

        EmailService emailService = new DummyEmailService(mailServer);

        UserRepository userRepository = new DummyUserRepository();

        PendingForgotPasswordRequestRepository pendingForgotPasswordRequestRepository =
                new DummyPendingForgotPasswordRequestRepository();

        ForgotPasswordService forgotPasswordService =
                new ForgotPasswordServiceImpl(pendingForgotPasswordRequestRepository, emailService, userRepository,
                        Duration.ofHours(1));

        // submits the 'Forgot password' form
        forgotPasswordService.forgotPassword("john.doe@example.com");

        // receives an e-mail with a temporary token
        String token = mailServer.fetchMail("john.doe@example.com");

        // uses this token to submit a new password
        forgotPasswordService.changeForgottenPassword("john.doe@example.com", token, "p@ssw0rd", "p@ssw0rd");

        // the token is invalidated upon a successful password update
        try {
            forgotPasswordService.changeForgottenPassword("john.doe@example.com", token, "s3cr3t", "s3cr3t");
        } catch (Exception expected) {
            System.err.println("whoa: " + expected.getMessage());
        }
    }
}
