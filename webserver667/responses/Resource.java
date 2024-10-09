package webserver667.responses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import startup.configuration.MimeTypes;
import webserver667.requests.HttpRequest;
import webserver667.responses.authentication.UserAuthenticator;

public class Resource implements IResource {
    private final Path resourcePath;
    private final MimeTypes mimeTypes;

   
    public Resource(HttpRequest request, String documentRoot, MimeTypes mimeTypes) {
        this.resourcePath = Paths.get(documentRoot, request.getUri());
        this.mimeTypes = mimeTypes;
    }

    @Override
    public boolean exists() {
        return Files.exists(resourcePath);
    }

    @Override
    public Path getPath() {
        return resourcePath;
    }

    @Override
    public boolean isProtected() {
        return Files.exists(resourcePath.getParent().resolve(".passwords"));
    }

    @Override
    public boolean isScript() {
    return resourcePath.toString().contains("scripts");
    }




    @Override
    public UserAuthenticator getUserAuthenticator(HttpRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'getUserAuthenticator'");
    }

    @Override
    public String getMimeType() {
        String fileName = resourcePath.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return mimeTypes.getMimeTypeByExtension(extension);
    }

    @Override
    public long getFileSize() throws IOException {
        return Files.size(resourcePath);
    }

    @Override
    public byte[] getFileBytes() throws IOException {
        return Files.readAllBytes(resourcePath);
    }

    @Override
    public long lastModified() {
        try {
            return Files.getLastModifiedTime(resourcePath).toMillis();
        } catch (IOException e) {
            return 0;
        }
    }
}
