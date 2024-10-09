package webserver667.responses.writers;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import webserver667.requests.HttpRequest;
import webserver667.requests.HttpMethods;
import webserver667.responses.IResource;

public class ResponseWriterFactory {
    public static ResponseWriter create(OutputStream out, IResource resource, HttpRequest request) {
        if (!resource.exists()) {
            if (resource.isProtected()) {
                return new UnauthorizedResponseWriter(out, resource, request);
            }
            if (request.getHttpMethod() == HttpMethods.PUT) {
                return new CreatedResponseWriter(out, resource, request);
            }
            return new NotFoundResponseWriter(out, resource, request);
        } else {
          
            if (resource.isProtected()) {
                if (resource.getUserAuthenticator(request) == null || !resource.getUserAuthenticator(request).isAuthenticated()) {
                    return new ForbiddenResponseWriter(out, resource, request);
                }
            }
            if (request.getHttpMethod() == HttpMethods.DELETE) {
                return new NoContentResponseWriter(out, resource, request);
            }
            if (resource.isScript()) {
                return new ScriptResponseWriter(out, resource, request);
            }
            String ifModifiedSince = request.getHeader("If-Modified-Since");
            if (ifModifiedSince != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.US);
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date modifiedSinceDate = dateFormat.parse(ifModifiedSince);
                    if (resource.lastModified() <= modifiedSinceDate.getTime()) {
                        return new NotModifiedResponseWriter(out, resource, request);
                    }
                } catch (ParseException e) {
                }
            }
            return new OkResponseWriter(out, resource, request);
        }
    }
}
