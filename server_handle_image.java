import java.net.*;
import java.util.*;
import java.io.*;

public class server_handle_image {

    
    public static void main(String[] args) {
        try(ServerSocket server_socket = new ServerSocket(50000)){
            System.out.println("Hello everyone, server is ready to listen on port 50000");

            while (true) {
                Socket client_socket = server_socket.accept();
                new handle_client(client_socket).start();
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class handle_client extends Thread{

    private static List<OutputStream> client_output_stream = new ArrayList<>();
    private InputStream inputStream;
    private FileOutputStream file_output;
    private OutputStream outputStream;


    private Socket client_socket;
    handle_client(Socket client_socket){
        this.client_socket = client_socket;
    }

    @Override
    public void run(){
        try{

            inputStream = client_socket.getInputStream();
            outputStream = client_socket.getOutputStream();
            file_output = new FileOutputStream("image_" + System.currentTimeMillis() + ".png");

            synchronized(client_output_stream){
                client_output_stream.add(outputStream);
            }

            byte[] buffer = new byte[4096];
            int byte_read;

            while ((byte_read = inputStream.read(buffer)) > 0) {
                file_output.write(buffer, 0, byte_read);
                broad_casting(buffer, byte_read,outputStream);
            }
            System.out.println("File received successfully");

            file_output.close();
            inputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(outputStream != null){
                    synchronized(client_output_stream){
                        client_output_stream.remove(outputStream);
                    }
                    outputStream.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
                if(file_output != null){
                    file_output.close();
                }
                
                client_socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public static void broad_casting(byte[] data, int length, OutputStream sender_output_stream){
        synchronized(client_output_stream){
            for (OutputStream output : client_output_stream) {
                if(output != sender_output_stream){
                    try {
                        output.write(data, 0, length);
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
