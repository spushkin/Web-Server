package webserver667.responses.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import webserver667.requests.HttpRequest;
import webserver667.responses.IResource;

public class OkResponseWriter extends ResponseWriter {

    public OkResponseWriter(OutputStream out, IResource resource, HttpRequest request) {
        super(out, resource, request);
    }

    @Override
    public void write() throws IOException {
        byte[] content = Files.readAllBytes(resource.getPath());
        String responseHeaders = String.format(
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: %s\r\n" +
            "Content-Length: %d\r\n" +
            "\r\n",
            resource.getMimeType(),
            content.length
        );

        outputStream.write(responseHeaders.getBytes());
        outputStream.write(content);
        outputStream.flush();
    }
}
