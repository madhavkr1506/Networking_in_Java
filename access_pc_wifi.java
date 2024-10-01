import java.io.BufferedReader;
import java.io.InputStreamReader;

public class access_pc_wifi {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        try{
            String command = "netsh wlan show interfaces";

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String inputLine;

            // String SSID = "";

            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                
                // if(inputLine.trim().startsWith("SSID")){
                //     SSID = inputLine.split(":")[1].trim();
                // }
            }

            // System.out.println("SSID: " + SSID);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
