package webserver667.responses.writers;

import java.io.IOException;
import java.io.OutputStream;

import webserver667.requests.HttpRequest;
import webserver667.responses.IResource;

public class UnauthorizedResponseWriter extends ResponseWriter {

    public UnauthorizedResponseWriter(OutputStream out, IResource resource, HttpRequest request) {
        super(out, resource, request);
    }

    @Override
    public void write() throws IOException {
        String response = "HTTP/1.1 401 Unauthorized\r\n" +
                          "WWW-Authenticate: Basic realm=\"667 Server\"\r\n" +
                          "Content-Length: 0\r\n" +
                          "\r\n";
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}
