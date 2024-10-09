package webserver667.responses.authentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PasswordFileReader {
    private Map<String, String> users;

    public PasswordFileReader(Path passwordFilePath) throws IOException {
        users = new HashMap<>();
        Files.readAllLines(passwordFilePath).stream().forEach(line -> {
            String[] parts = line.replace("{SHA-1}", "").split(":");
            if (parts.length == 2) {
                users.put(parts[0], parts[1]);
            }
        });
    }

    public boolean isUserAuthorized(String username, String password) {
        String storedPasswordHash = users.get(username);
        if (storedPasswordHash == null) {
            return false;
        }
        String passwordHash = encryptClearPassword(password);
        return storedPasswordHash.equals(passwordHash);
    }

    private String encryptClearPassword(String password) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] result = mDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            return "";
        }
    }
}
