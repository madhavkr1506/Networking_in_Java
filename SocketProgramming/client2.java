import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client2 {
    public static void main(String[] args) {
        try {
            Socket clienSocket = new Socket("localhost", 5555);
            System.out.println("client 2 is connecting to server");

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
