package org.bossie.forgotpassword;

public interface EmailService {
    void sendMail(String to, String body);
}

class DummyEmailService implements EmailService {

    private final MailServer mailServer;

    DummyEmailService(MailServer mailServer) {
        this.mailServer = mailServer;
    }

    @Override
    public void sendMail(String to, String body) {
        mailServer.sendMail(to, body);
    }
}
