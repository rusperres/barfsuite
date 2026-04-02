import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Interceptor {

    private List<Transaction> transactions;
    private ServerSocket serverSocket;
    private Socket socket1;
    private Socket socket2;

    public Interceptor(ServerSocket serverSocket, String host, int port) throws IOException {
        this.serverSocket = serverSocket;
        this.socket1 = serverSocket.accept();
        this.socket2 = new Socket(host, port);
        transactions = new ArrayList<>();
    }

    public void interceptOn(){
        try{
            while(true){
                byte[] buffer = new byte[4096];

                InputStream socket1InputStream = socket1.getInputStream();
                OutputStream socket1OutputStream = socket1.getOutputStream();

                StringBuilder requestBuilder = new StringBuilder();
                int bytesReadFromSocket1;
                while((bytesReadFromSocket1 = socket1InputStream.read(buffer)) != -1){
                    requestBuilder.append(new String(buffer, 0, bytesReadFromSocket1));
                }
            }
        }catch (IOException e){}
    }
}
