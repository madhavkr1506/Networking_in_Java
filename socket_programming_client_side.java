import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class socket_programming_client_side {

    public static void main(String[] args) {
        try(Socket client_socket = new Socket("localhost",55555)){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String inputLine;
            PrintWriter out = new PrintWriter(client_socket.getOutputStream(),true);

            System.out.print("Server is connected, You can type your request.");
            while ((inputLine = reader.readLine()) != null) {
                out.println(inputLine);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}