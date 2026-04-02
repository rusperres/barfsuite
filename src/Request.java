import java.util.List;
import java.util.Map;

public class Request {
    private String method;
    private String url;
    private String version;
    Map<String, List<String>> headers;
    private String body;

    public Request(String method, String url, String version, Map<String, List<String>> headers, String body) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public void addHeader(String key, List<String> value){
        headers.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method).append(" ").append(url).append(" ").append(version).append("\n");
        for(Map.Entry<String, List<String>> header: headers.entrySet()){
            stringBuilder.append(header.getKey()).append(": ");
            stringBuilder.append(String.join(", ", header.getValue()));
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        if(body!=null)stringBuilder.append(body);
        return stringBuilder.toString();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
