import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;



import java.sql.*;

public class server_ {

    static List<PrintWriter> client_writer_list = new ArrayList<>();
    static String message = "";
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(50000)){
            System.out.println("Server is ready to listen on port 50000");

            while (true) {
                Socket client_socket = serverSocket.accept();
                new handle_multiple_client(client_socket).start();

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static class handle_multiple_client extends Thread{
        private Socket client_socket;
        private PrintWriter output_writer;
        handle_multiple_client(Socket client_socket){
            this.client_socket = client_socket;
        }

        public void run(){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));

                output_writer = new PrintWriter(client_socket.getOutputStream());
                synchronized(client_writer_list){
                    client_writer_list.add(output_writer);
                }

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    message = inputLine;

                    broad_casting(inputLine);

                    handle_chat();

                }


            }catch(Exception e){
                e.printStackTrace();
            }finally{
                synchronized(client_writer_list){
                    client_writer_list.remove(output_writer);
                }
                try{
                    client_socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        
    }

    static void broad_casting(String message){
        synchronized(client_writer_list){
            for(PrintWriter output_writer : client_writer_list){
                output_writer.println(message);
                output_writer.flush();

            }
        }
    }

    static void handle_chat(){
        String dburl = "jdbc:mysql://localhost:3306/";
        String username = "root";
        String password = "1234";
        try(Connection connection = DriverManager.getConnection(dburl,username,password);
        Statement statement = connection.createStatement()){

            String createdb = "create database if not exists chat";
            statement.executeUpdate(createdb);

            String usedb = "use chat";
            statement.executeUpdate(usedb);

            String table = "create table if not exists chat_history (chat_ text)";
            statement.executeUpdate(table);

            String insert = "insert into chat_history(chat_) values (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setString(1, message);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}