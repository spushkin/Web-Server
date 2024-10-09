package webserver667.requests;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private HttpMethods method;
    private String uri;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public HttpMethods getHttpMethod() {
        return this.method;
    }

    public void setHttpMethod(HttpMethods method) {
        this.method = method;
    }

    public String getUri() {
        if (uri != null && uri.contains("?")) {
            return uri.split("\\?")[0];
        }
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getQueryString() {
        if (uri != null && uri.contains("?")) {
            return uri.split("\\?")[1];
        }
        return null;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue.trim());
    }

    public void addHeader(String headerLine) {
        String[] parts = headerLine.split(": ", 2);
        if (parts.length == 2) {
            addHeader(parts[0], parts[1]);
        }
    }

    public int getContentLength() {
        String contentLength = headers.get("Content-Length");
        return contentLength != null ? Integer.parseInt(contentLength) : 0;
    }

    public byte[] getBody() {
        return body != null ? body : new byte[0];
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public boolean hasBody() {
        return body != null && body.length > 0;
    }
}
