import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class image_send_server_side {
    public static void main(String[] args) {
        try(ServerSocket server_socket = new ServerSocket(55555)){
            System.out.println("Server is here, and listening on port 55555");

            Socket client_socket = server_socket.accept();

            InputStream inputStream = client_socket.getInputStream();
            FileOutputStream file_output = new FileOutputStream("received_file.jpg");

            byte[] buffer = new byte[4096];
            int byteRead;

            while ((byteRead = inputStream.read(buffer)) > 0) {
                file_output.write(buffer,0,byteRead);
            }

            file_output.close();

            System.out.println("File received successfully");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
