package org.bossie.forgotpassword;

import java.util.HashMap;
import java.util.Map;

public interface MailServer {
    void sendMail(String emailAddress, String body);
    String fetchMail(String emailAddress);
}

class DummyMailServer implements MailServer {

    private Map<String, String> mails = new HashMap<>();

    @Override
    public void sendMail(String emailAddress, String body) {
        mails.put(emailAddress, body);
    }

    @Override
    public String fetchMail(String emailAddress) {
        return mails.get(emailAddress);
    }
}
