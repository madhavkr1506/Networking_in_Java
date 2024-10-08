import java.net.*;
import java.io.*;

public class client2 {
    public static void main(String[] args) {
        try(Socket client_socket = new Socket("localhost",50000);
        FileInputStream file_read = new FileInputStream("C:\\Users\\madha\\Pictures\\madhav3.jpg");
        OutputStream output = client_socket.getOutputStream()){

            byte[] buffer = new byte[4096];
            int byte_read;

            while ((byte_read = file_read.read(buffer)) > 0) {
                output.write(buffer,0,byte_read);
                output.flush();
            }
            System.out.println("File sent by client2");
            file_read.close();
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
