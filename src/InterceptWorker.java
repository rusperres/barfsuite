import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class InterceptWorker implements Runnable {

    private Map<UUID,Transaction> transactions;
    private ServerSocket serverSocket;
    private Socket socket1;
    private Socket socket2;
    private ByteArrayOutputStream requestHeaderBuffer;

    private int id;

    public InterceptWorker(ServerSocket serverSocket, Socket socket1, int id) throws IOException {
        this.serverSocket = serverSocket;
        this.socket1 = socket1;
        this.socket2 = null;
        transactions = new HashMap<>();
        this.id = id;
        requestHeaderBuffer = new ByteArrayOutputStream();
    }

    @Override
    public void run() {
        System.out.println("Thread " + id);
        try{

            InputStream socket1InputStream = socket1.getInputStream();
            int b;
            int lastFour = 0;
            while((b = socket1InputStream.read()) != -1){
                requestHeaderBuffer.write(b);
                lastFour = (lastFour << 8) | (b & 0xFF);
                if(lastFour == 0x0D0A0D0A) break;
            }

            String request = requestHeaderBuffer.toString();
            System.out.println(request);

            StringTokenizer stringTokenizer = new StringTokenizer(request, "\n");
            List<String> headerStrings = new ArrayList<>();
            while(stringTokenizer.hasMoreTokens()){
                headerStrings.add(stringTokenizer.nextToken());
            }
            System.out.println("Printing Header Strings");
            for(int i = 0; i<headerStrings.size(); i++){
                System.out.println(i + " " + headerStrings.get(i));
            }
            stringTokenizer = new StringTokenizer(headerStrings.get(0), " ");
            String method = stringTokenizer.nextToken();
            String url = stringTokenizer.nextToken();
            String version = stringTokenizer.nextToken();

            Map<String, List<String>> headers = new LinkedHashMap<>();
            for(int i = 1; i<headerStrings.size(); i++){
                String headerString = headerStrings.get(i);
                if(headerString.isBlank()) continue;
                stringTokenizer = new StringTokenizer(headerStrings.get(i), ":");
                String key = stringTokenizer.nextToken();
                String valuesString = headerStrings.get(i).substring(key.length()+1);
                stringTokenizer = new StringTokenizer(valuesString, ",");
                List<String> values = new ArrayList<>();
                while(stringTokenizer.hasMoreTokens()){
                    values.add(stringTokenizer.nextToken().trim());
                }
                headers.put(key, values);
            }
            String body = "";
            if(headers.containsKey("Content-Length")){
                int contentLength = Integer.parseInt(headers.get("Content-Length").getFirst().trim());
                byte[] bodyBytes = new byte[contentLength];
                int bytesRead = 0;
                while(bytesRead < contentLength){
                    int result = socket1InputStream.read(bodyBytes, bytesRead, contentLength-bytesRead);
                    if(result==-1)break;
                    bytesRead+=result;
                }
                body = new String(bodyBytes);
            }
            Request requestObject = new Request(method, url, version, headers, body);
            System.out.println("Printing Request Object");
            System.out.println(requestObject);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getSocket1() {
        return socket1;
    }

    public Socket getSocket2() {
        return socket2;
    }


    public int getId() {
        return id;
    }
}
