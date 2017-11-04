package util;

import java.util.UUID;

public class KeyGenerator {

    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
