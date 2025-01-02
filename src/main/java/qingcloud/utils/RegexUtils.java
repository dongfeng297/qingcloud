package qingcloud.utils;

public class RegexUtils {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean isEmailInvalid(String email) {
        if (email == null || email.isEmpty()) {
            return true;
        }
        return !email.matches(EMAIL_REGEX);
    }
}
