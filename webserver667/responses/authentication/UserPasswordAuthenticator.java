package webserver667.responses.authentication;

import webserver667.requests.HttpRequest;
import webserver667.responses.IResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class UserPasswordAuthenticator extends UserAuthenticator {

    public UserPasswordAuthenticator(HttpRequest request, IResource resource) {
        super(request, resource);
    }

    @Override
    public boolean isAuthenticated() {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            return false;
        }

        String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));

        String[] parts = credentials.split(":");
        if (parts.length != 2) {
            return false;
        }

        String username = parts[0];
        String password = parts[1];

        Path passwordFilePath = Paths.get(resource.getPath().toString());
        PasswordFileReader passwordFileReader;
        try {
            passwordFileReader = new PasswordFileReader(passwordFilePath);
        } catch (IOException e) {
            return false;
        }

        return passwordFileReader.isUserAuthorized(username, password);
    }
}
