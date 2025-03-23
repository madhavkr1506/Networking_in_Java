import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.util.HashMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class server {

    private static HashMap<String, ClientHandler> clientMap = new HashMap<>();

    public static void main(String[] args) {
        try{
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("keystore.jks"), "password".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "password".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            
            SSLServerSocket serverSocket = (SSLServerSocket)sslServerSocketFactory.createServerSocket(5555);
            
            
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized void registerClient(String username, ClientHandler client){
        clientMap.put(username, client);
        System.out.println(username + " connected");
    }
    public static synchronized void removeClient(String username){
        clientMap.remove(username);
        System.out.println(username + " disconnected");
    }

    public static synchronized ClientHandler getClient(String recipient){
        return clientMap.get(recipient);
    }

}

class ClientHandler extends Thread{
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("please enter your name: ");
            username = reader.readLine();
            server.registerClient(username, this);
            writer.println("welcome " + username + "! now you can chat");
            
            String inputMessage;
            while((inputMessage = reader.readLine()) != null){
                if(inputMessage.startsWith("@")){
                    String[] parts = inputMessage.split(" ", 2);
                    if(parts.length < 2){
                        writer.println("Invalid message format. Use @recipient message.");
                        continue;
                    }

                    String recipient = parts[0].substring(1);
                    String message = parts[1];

                    ClientHandler recipientClient = server.getClient(recipient);
                    if(recipientClient != null){
                        recipientClient.sendMessage(username + ": " + message);
                    }else{
                        writer.println("User " + recipient + " is not available.");
                    }

                }
                else {
                    writer.println("Use '@recipient message' format to chat.");
                }
                System.out.println(inputMessage);
                writer.println(inputMessage);
            }
            server.removeClient(username);
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        writer.println(message);
    }
}
