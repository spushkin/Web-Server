package webserver667.responses.writers;

import java.io.IOException;
import java.io.OutputStream;

import webserver667.requests.HttpRequest;
import webserver667.responses.IResource;

public class BadRequestResponseWriter extends ResponseWriter {

    public BadRequestResponseWriter(OutputStream out, IResource resource, HttpRequest httpRequest) {
        super(out, resource, httpRequest);
    }

    @Override
    public void write() throws IOException {
        String response = "HTTP/1.1 400 Bad Request\r\n" +
                          "Content-Length: 0\r\n" +
                          "\r\n";
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}
