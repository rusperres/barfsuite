import java.util.List;
import java.util.Map;

public class Response {
    private String version;
    private int statusCode;
    private String message;
    private Map<String, List<String>> headers;
    private String body;

    public Response(String version, int statusCode, String message, Map<String, List<String>> headers, String body) {
        this.version = version;
        this.statusCode = statusCode;
        this.message = message;
        this.headers = headers;
        this.body = body;
    }

    public void addHeader(String key, List<String> value){
        headers.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(version).append(" ").append(statusCode).append(" ").append(message).append("\n");
        for(Map.Entry<String, List<String>> header: headers.entrySet()){
            stringBuilder.append(header.getKey()).append(": ");
            stringBuilder.append(String.join(", ",header.getValue()));
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        if(body!=null)stringBuilder.append(body);
        return stringBuilder.toString();
    }

    public String getVersion() {
        return version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage(){
        return message;
    }
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
