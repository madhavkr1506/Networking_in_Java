import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class client1 {
    public static void main(String[] args) {
        try {

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("truststore.jks"), "password".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");

            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocket clienSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 5555);

            System.out.println("client 1 is connecting to server");

            BufferedReader consolReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(clienSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));

            System.out.println(reader.readLine());

            String username = consolReader.readLine();
            writer.println(username);
            System.out.println(reader.readLine());

            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = reader.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }).start();

            System.out.println("To chat, use '@recipient message' format.");

            String inputLine;
            while ((inputLine = consolReader.readLine()) != null) {
                writer.println(inputLine);
            }

            writer.close();
            consolReader.close();
            reader.close();
            clienSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}