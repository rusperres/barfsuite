import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main{
    static String lastRequest;
    public static void main(String[] args){
        lastRequest = "";
        ServerSocket server = null;
        try{
            server = new ServerSocket(8000);
            while(true){
                // reusable buffer
                byte[] buffer = new byte[4096];

                Socket socket1 = server.accept();

                InputStream socket1InputStream = socket1.getInputStream();
                OutputStream socket1OutputStream = socket1.getOutputStream();

                // no need for a loop since request body is small
                int bytesReadFromSocket1 = socket1InputStream.read(buffer);

                String request = new String(buffer, 0, bytesReadFromSocket1);


                if (!(request.startsWith("GET") || request.startsWith("POST")) || request.equals(lastRequest)) {
                    socket1.close();
                    continue;
                }
                System.out.println(request);
                lastRequest = request;
                Socket socket2 = new Socket("natas0.natas.labs.overthewire.org", 80);

                InputStream socket2InputStream = socket2.getInputStream();
                OutputStream socket2OutputStream = socket2.getOutputStream();

                socket2OutputStream.write(buffer, 0, bytesReadFromSocket1);
                socket2OutputStream.flush();

                StringBuilder response = new StringBuilder();
                int bytesReadFromSocket2;
                while((bytesReadFromSocket2=socket2InputStream.read(buffer))!=-1){
                    socket1OutputStream.write(buffer, 0, bytesReadFromSocket2);
                    response.append(new String(buffer, 0, bytesReadFromSocket2));
                }
                socket1OutputStream.flush();

                System.out.println("Response: ");
                System.out.println(response);
                socket1.close();
                socket2.close();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}