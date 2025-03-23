import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client2 {
    public static void main(String[] args) {
        try{
            Socket clienSocket = new Socket("localhost", 5555);
            System.out.println("client 2 is connecting to server");
            BufferedReader consolReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(clienSocket.getOutputStream(), true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));

            String inputLine;
            while ((inputLine = consolReader.readLine()) != null) {
                writer.println(inputLine);
                System.out.println(reader.readLine());
            }

            writer.close();
            reader.close();
            consolReader.close();
            clienSocket.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
