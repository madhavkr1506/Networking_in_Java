import java.net.*;
import java.io.*;
import java.util.*;


public class server_ {

    private static List<PrintWriter> client_writer_list = new ArrayList<>();
    public static void main(String[] args) {


        try(ServerSocket server_socket = new ServerSocket(50000)){
            System.out.println("Server is ready to listen on port 50000");

            while (true) {
                Socket client_socket = server_socket.accept();
                new handle_multiple_client(client_socket).start();
            }
            


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static class handle_multiple_client extends Thread{
        private Socket client_socket;
        private PrintWriter output;
        public handle_multiple_client(Socket client_socket){
            this.client_socket = client_socket;
        }

        public void run(){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
                output = new PrintWriter(client_socket.getOutputStream());

                synchronized(client_writer_list){
                    client_writer_list.add(output);
                }
                String inputline;
                while ((inputline = reader.readLine()) != null) {
                    System.out.println("Received: " + inputline);
                    broadCasting(inputline);
                }

            }catch(Exception e){
                e.printStackTrace();
            }finally{
                synchronized(client_writer_list){
                    client_writer_list.remove(output);
                }
                try{
                    client_socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }


    }

    private static void broadCasting(String message){
        synchronized(client_writer_list){
            for(PrintWriter writer : client_writer_list){
                writer.println(message);
                writer.flush();
            }
        }
    }
}