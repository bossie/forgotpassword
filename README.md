# forgotpassword

This is an example in Java of how one might implement a "Forgot password" flow. It involves mailing a secret token that a user can use once
to update his current password. The token itself is essentially a password with a limited lifetime; it is subject to the same treatment
as regular passwords in that it is not stored in plain text, but hashed with a salt. Note that the salt becomes part of the hash and is
not stored separately in the database.

Run `org.bossie.forgotpassword.App` to see the flow in action. `org.bossie.forgotpassword.ForgotPasswordServiceImpl` is where the logic
is; its dependencies are dummy implementations.
