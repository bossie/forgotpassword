package org.bossie.forgotpassword;

public interface UserRepository {
    void updatePassword(String userId, String password);
}

class DummyUserRepository implements UserRepository {

    @Override
    public void updatePassword(String userId, String password) {
        System.out.println("changed password for " + userId);
    }
}
