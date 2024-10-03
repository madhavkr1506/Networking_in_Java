import java.io.*;
import java.net.Socket;

public class sending_file_client_code {
    public static void main(String[] args) {
        File file = new File("send_file.txt");

        try(Socket client_socket = new Socket("localhost",55555);
        FileInputStream fileinput = new FileInputStream(file);
        OutputStream output = client_socket.getOutputStream()){
            byte[] buffer = new byte[4096];
            int byteRead;

            System.out.println("Sending file to the server");

            while ((byteRead = fileinput.read(buffer)) > 0) {
                output.write(buffer,0,byteRead);
            }
            System.out.println("File sent successfully");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
