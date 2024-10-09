package webserver667.responses.writers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import webserver667.requests.HttpRequest;
import webserver667.responses.IResource;

public class ScriptResponseWriter extends ResponseWriter {

    public ScriptResponseWriter(OutputStream out, IResource resource, HttpRequest request) {
        super(out, resource, request);
    }

    @Override
    public void write() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(resource.getPath().toString());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (InputStream inputStream = process.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String output = reader.lines().collect(Collectors.joining("\n"));
            byte[] outputBytes = output.getBytes(StandardCharsets.UTF_8);

            String responseHeader = "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: text/html\r\n" +
                                    "Content-Length: " + outputBytes.length + "\r\n" +
                                    "\r\n";
            outputStream.write(responseHeader.getBytes(StandardCharsets.UTF_8));
            outputStream.write(outputBytes);
            outputStream.flush();
        }
    }
}
