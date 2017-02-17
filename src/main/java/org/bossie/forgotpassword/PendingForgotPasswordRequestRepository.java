package org.bossie.forgotpassword;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public interface PendingForgotPasswordRequestRepository {
    void addForgotPasswordRequest(String emailAddress, String hashedToken, Instant expiresAt);
    PendingForgotPasswordRequest getForgotPasswordRequest(String emailAddress);
    void removeForgotPasswordRequest(String emailAddress);

    class PendingForgotPasswordRequest {
        public final String emailAddress;
        public final String hashedToken;
        public final Instant expiresAt;

        public PendingForgotPasswordRequest(String emailAddress, String hashedToken, Instant expiresAt) {
            this.emailAddress = emailAddress;
            this.hashedToken = hashedToken;
            this.expiresAt = expiresAt;
        }
    }
}

class DummyPendingForgotPasswordRequestRepository implements PendingForgotPasswordRequestRepository {

    private final Map<String, PendingForgotPasswordRequest> requests = new HashMap<>();

    @Override
    public void addForgotPasswordRequest(String emailAddress, String hashedToken, Instant expiresAt) {
        requests.put(emailAddress, new PendingForgotPasswordRequest(emailAddress, hashedToken, expiresAt));
    }

    @Override
    public PendingForgotPasswordRequest getForgotPasswordRequest(String emailAddress) {
        return requests.get(emailAddress);
    }

    @Override
    public void removeForgotPasswordRequest(String emailAddress) {
        requests.remove(emailAddress);
    }
}
