import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class server {

    private static HashMap<String, HashMap<String, ClientHandler>> clientMap = new HashMap<>();

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try{
            ServerSocket server_socket = new ServerSocket(5555);
            System.out.println("server is listening on port 5555");
            
            while (true) {
                Socket socket = server_socket.accept();
                new ClientHandler(socket).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized void registerClient(String username, String recipents, ClientHandler client){
        clientMap.put(username, new HashMap<>().put(recipents, client));
        System.out.println(username + " connected");
    }
    public static synchronized void removeClient(String username){
        clientMap.remove(username);
        System.out.println(username + " disconnected");
    }

}

class ClientHandler extends Thread{
    private Socket socket;
    private String username;
    private String recipents;
    ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("please enter your name: ");
            username = reader.readLine();
            writer.println("welcome " + username + "! now you can chat");
            writer.println("please enter recipents name: ");
            recipents = reader.readLine();
            writer.println(username + "are now connected with " + recipents);
            writer.println("you can start with hello message");
            String inputMessage;
            while((inputMessage = reader.readLine()) != null){
                System.out.println(inputMessage);
                writer.println(inputMessage);
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
