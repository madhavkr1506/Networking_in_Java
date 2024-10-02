import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class udp_socket_client_side {
    public static void main(String[] args) {
        try{

            DatagramSocket client_socket = new DatagramSocket();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String inputLine;
            
            System.out.println("Server is connected you can send your request.");

            InetAddress ipAddress = InetAddress.getByName("localhost");
            while ((inputLine = reader.readLine()) != null ) {
                byte[] inputByte = inputLine.getBytes();
                DatagramPacket send_message = new DatagramPacket(inputByte, inputByte.length,ipAddress,15689);

                client_socket.send(send_message);

                System.out.println("Message send");
            }
            client_socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
