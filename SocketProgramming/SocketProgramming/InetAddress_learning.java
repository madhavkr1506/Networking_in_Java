
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InetAddress_learning{

    private final static Logger logger = Logger.getLogger(InetAddress_learning.class.getName());
    public static void main(String[] args) {
        
        try{

            // logger.log(Level.INFO, "here we are fetching local ip address of host name");

            // String hostname = "www.google.com";
            // InetAddress host_address = InetAddress.getByName(hostname);
            // System.out.println("host address: " + host_address.getHostAddress());

            logger.log(Level.INFO, "\n\nhere we are fetching loopback ip address\n");

            String loopback_host_name = "localhost";
            InetAddress loopback_address = InetAddress.getByName(loopback_host_name);
            System.out.println("loopback address: "+loopback_address.getHostAddress());
            System.out.println("loopback hostname with address: "+InetAddress.getLoopbackAddress());
            System.out.println();


            logger.log(Level.INFO, "\n\nhere we are fetching localhost ip address\n");

            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("localhost address: "+localhost.getHostAddress());
            System.out.println("localhost hostname: "+localhost.getHostName());

            System.out.println();

            logger.log(Level.INFO, "\n\nhere we are checking whether it is loopback address or not\n");

            System.out.println("verification: "+localhost.isLoopbackAddress());
            

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}