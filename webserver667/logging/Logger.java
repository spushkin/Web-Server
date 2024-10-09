package webserver667.logging;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import webserver667.requests.HttpRequest;
import webserver667.responses.IResource;

public class Logger {
    /**
     * 
     * @param ipAddress The IP address of the client.
     * @param request The HTTP request object.
     * @param resource The resource being accessed.
     * @param statusCode The HTTP status code of the response.
     * @param bytesSent The number of bytes sent in the response.
     * @return A log string.
     */
    public static String getLogString(
        String ipAddress,
        HttpRequest request,
        IResource resource,
        int statusCode,
        int bytesSent) {

        String dateTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z"));

        String requestLine = String.format("%s %s %s", request.getHttpMethod(), request.getUri(), request.getVersion());

        return String.format("%s - - [%s] \"%s\" %d %d", ipAddress, dateTime, requestLine, statusCode, bytesSent);
    }
}
