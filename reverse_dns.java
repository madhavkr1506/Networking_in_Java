import java.net.InetAddress;
import java.util.Scanner;

public class reverse_dns {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input IP Address or a Host Name");
        String input = sc.nextLine();

        try{
            InetAddress ipAddress = InetAddress.getByName(input);

            System.out.println("Host Name: " + ipAddress.getHostName());
            System.out.println("IP Address: " + ipAddress.getHostAddress());
        }catch(Exception e){
            e.printStackTrace();
        }
        sc.close();
        

    }
}


/*
 * 
 * if i input hostname(www.lpu.in), then i perform forward dns lookup which resolves the hostname to its ip address.
 * if i input ip address(180.179.213.42) then i perform reverse dns lookup to find host name associated with that ip. if ip address does not have reverse dns entry then getHostName() will return ip address itself.
 */