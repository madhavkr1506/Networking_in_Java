import java.io.*;
public class check_user_privilage {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        try{
            // String command = "netsh interface set interface \"Wi-Fi\" admin=disable";

            String command = "whoami /groups";

            Process process = Runtime.getRuntime().exec(command);

            boolean isAdmin = false;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);

                if(inputLine.contains("S-1-5-32-544")){
                    isAdmin = true;
                }
            }

            
            int exitCode = process.waitFor();

            if(exitCode == 0){
                if(isAdmin){
                    System.out.println("\n\n\nUser is having administrative level privilage");
                }else{
                    System.out.println("User is not having administrative privilage:");
                }
                
            }
            else{
                System.out.println("Failed to check the user privilage:  " + exitCode);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
