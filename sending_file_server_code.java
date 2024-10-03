import java.io.*;
import java.net.*;

public class sending_file_server_code {
    public static void main(String[] args) {
        try(ServerSocket server_socket = new ServerSocket(55555)){

            System.out.println("Server is listening on the port 55555");

            Socket client_socket = server_socket.accept(); // --> it is a blocking call.

            handle_file_transfer(client_socket);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void handle_file_transfer(Socket client_socket){
        try(InputStream input = client_socket.getInputStream();
        FileOutputStream file_output = new FileOutputStream("received_file.txt")){
            byte[] buffer = new byte[4096];
            int byteRead;

            while ((byteRead = input.read(buffer)) > 0) {
                file_output.write(buffer,0,byteRead);
            }
            System.out.println("File received successfully");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                client_socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
