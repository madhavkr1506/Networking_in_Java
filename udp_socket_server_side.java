import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class udp_socket_server_side {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try{
            DatagramSocket server_socket = new DatagramSocket(15689);
            System.out.println("Server id connected to the port");
            byte[] storeData = new byte[1024];

            while (true) {
                DatagramPacket receivedPacket = new DatagramPacket(storeData, storeData.length);
                server_socket.receive(receivedPacket);

                String message = new String(receivedPacket.getData(),0,receivedPacket.getLength());
                System.out.println("Message received: " + message);
                
            }

            


        }catch(Exception e){
            
            e.printStackTrace();
            
        }
    }
}
