import java.net.InetAddress;


public class access_your_pc {
    public static void main(String[] args) {
        try{
            InetAddress localhost_ip_Address = InetAddress.getLocalHost();
            System.out.println("Local host ip address: " + localhost_ip_Address.getHostAddress());
            System.out.println("Local host name: " + localhost_ip_Address.getHostName());

            // InetAddress find_host_name_by_ip = InetAddress.getByName("172.22.39.255");
            // System.out.println("172.22.32.1 host name: " + find_host_name_by_ip.getHostName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


// Port: it is a communication endpoint that identifies the type of network services.

// http use port 80, ssh(secure shell) use port 22, dns use port 53, ftp use port 20 and 21.



