import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ipconfig {
    public static void main(String[] args) {
        try{
            String command = "ipconfig /all";
            @SuppressWarnings("deprecation")
            Process process = Runtime.getRuntime().exec(command);
            
            String inputLine;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
