import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.net.Socket;

public class image_send_client_side {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\madha\\Pictures\\madhav2.jpg");

        try(Socket socket = new Socket("localhost",55555);
        FileInputStream file_input = new FileInputStream(file);
        OutputStream output = socket.getOutputStream()){

            System.out.println("Client is connected to the port 55555");

            byte[] buffer = new byte[4096];
            int byteRead;

            while ((byteRead = file_input.read(buffer)) > 0) {
                output.write(buffer,0,byteRead);
            }

            output.close();
            System.out.println("file is sent successfully");


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
