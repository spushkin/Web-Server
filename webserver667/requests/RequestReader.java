package webserver667.requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import webserver667.exceptions.responses.BadRequestException;
import webserver667.exceptions.responses.MethodNotAllowedException;

public class RequestReader {

  private final InputStream input;

  public RequestReader(InputStream input) {
    this.input = input;
  }

  public HttpRequest getRequest() throws BadRequestException, MethodNotAllowedException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
      String line = reader.readLine();
      if (line == null || line.isEmpty()) {
        throw new BadRequestException("Request line is empty");
      }

      String[] requestLineParts = line.split(" ");
      if (requestLineParts.length != 3) {
        throw new BadRequestException("Malformed request line");
      }

      HttpMethods method;
      try {
        method = HttpMethods.valueOf(requestLineParts[0]);
      } catch (IllegalArgumentException e) {
        throw new MethodNotAllowedException();
      }

      String uri = requestLineParts[1];
      String version = requestLineParts[2];
      HttpRequest request = new HttpRequest();
      request.setHttpMethod(method);
      request.setUri(uri);
      request.setVersion(version);

      String headerLine;
      int contentLength = 0;
      while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
        String[] headerParts = headerLine.split(":", 2);
        if (headerParts.length == 2) {
          String headerName = headerParts[0].trim();
          String headerValue = headerParts[1].trim();
          request.addHeader(headerName, headerValue);
          if ("Content-Length".equalsIgnoreCase(headerName)) {
            contentLength = Integer.parseInt(headerValue);
          }
        }
      }

      if (contentLength > 0) {
        char[] body = new char[contentLength];
        reader.read(body, 0, contentLength);
        request.setBody(new String(body).getBytes());
      }

      return request;
    } catch (IOException e) {
      throw new BadRequestException("Error reading request");
    }
  }
}
