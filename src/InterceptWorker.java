import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class InterceptWorker implements Runnable {

    private List<Transaction> transactions;
    private ServerSocket serverSocket;
    private Socket socket1;
    private Socket socket2;

    public InterceptWorker(ServerSocket serverSocket, Socket socket1) throws IOException {
        this.serverSocket = serverSocket;
        this.socket1 = socket1;
        this.socket2 = null;
        transactions = new ArrayList<>();
    }

    @Override
    public void run() {
        try{

            InputStream socket1InputStream = socket1.getInputStream();
            OutputStream socket1OutputStream = socket1.getOutputStream();

            ByteArrayOutputStream inputBuffer = new ByteArrayOutputStream();
            int b;
            int lastFour = 0;
            while((b = socket1InputStream.read()) != -1){
                inputBuffer.write(b);
                lastFour = (lastFour << 8) | (b & 0xFF);
                if(lastFour == 0x0D0A0D0A) break;
            }
            byte[] inputBytes = inputBuffer.toByteArray();
            int bytesReadFromSocket1 = inputBytes.length;

            String request = new String(inputBytes);
            System.out.println(request);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }



}
