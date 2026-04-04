import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Interceptor {
    private static final List<InterceptWorker> interceptWorkers = new ArrayList<>();
    private static final List<Thread> threads = new ArrayList<>();
    private static volatile boolean running;
    private static final  ServerSocket serverSocket;
    static {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void interceptOn(){
        running = true;
        try{
            while(running){
                Socket socket = serverSocket.accept();
                InterceptWorker interceptWorker = new InterceptWorker(serverSocket, socket);
                interceptWorkers.add(interceptWorker);
                Thread thread = new Thread(interceptWorker);
                threads.add(thread);
                thread.start();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void interceptOff(){
        running = false;
        try{
            serverSocket.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        for(Thread thread : threads) thread.interrupt();

    }

}
