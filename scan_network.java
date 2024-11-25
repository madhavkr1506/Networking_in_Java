import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class scan_network {

    public static void main(String[] args) {
        try{
            
            String command = "ipconfig";
            String subnetMask = returnSubnetMask(command);

            if(subnetMask.equals("Not Found") || subnetMask.equals("Error While Fetching")){
                return;
            }

            InetAddress subnetAddress = InetAddress.getByName(subnetMask);
            InetAddress ipAddress = InetAddress.getLocalHost();

            byte[] subnetByte = subnetAddress.getAddress();
            byte[] ipByte = ipAddress.getAddress();
            
            byte[] networkByte = new byte[ipByte.length];

            for(int i=0;i<ipByte.length;i++){
                networkByte[i] = (byte)(ipByte[i] & subnetByte[i]);
            }

            InetAddress networkAddress = InetAddress.getByAddress(networkByte);

            System.out.println("IP Address = " + ipAddress.getHostAddress());
            System.out.println("Subnet Mask = " + subnetAddress.getHostAddress());
            System.out.println("Network Address = " + networkAddress.getHostAddress());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private static String  returnSubnetMask(String command){
        try{
           Process process = Runtime.getRuntime().exec(command);
           BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
           
           String inputLine;
           while((inputLine = reader.readLine()) != null){
            if(inputLine.trim().startsWith("Subnet Mask")){
                return inputLine.split(":")[1].trim();
            }
           }

           return "Not Found";
        }catch(Exception e){
            e.printStackTrace();
            return "Error While Fetching";
        }
    }
}