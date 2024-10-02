import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class socket_programming_server_side {
    public static void main(String[] args) {
        try(ServerSocket server_socket = new ServerSocket(55555)){
            
            Socket client_socket = server_socket.accept();

            System.out.println("Server is connected to the port 12345");

            BufferedReader reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Request from client side: " + inputLine);
            }



        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
