import java.security.MessageDigest;
import java.util.Scanner;

public class hashing_256 {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            System.out.println("Input Message: ");
            String inputMessage = new Scanner(System.in).nextLine();

            byte[] hash = digest.digest(inputMessage.getBytes("UTF-8"));

            StringBuilder hexStringBuilder = new StringBuilder();

            for(byte b : hash){
                String hextString = Integer.toHexString(0xff & b);
                if(hextString.length() == 1){
                    hexStringBuilder.append("0");

                }
                hexStringBuilder.append(hextString);
            }

            System.out.println("Hex String: " + hexStringBuilder.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}




/*  working: 1. we have to find the ascii value of each character.
2. convert all ascii value in binary form.
3. we have to perform padding, make string length multiple of 512 bits.

lets take string "madhav". now madhav has 6 character each of 8 bits. total no of bits 6 x 8 -> 48.
now append 1 bit at the end. then total number of bits -> 49.

now we have to add enough 0 bit to make string of length 448 bit.

now we have 49 bits so we have to add (448 - 49) = 399 bit.

4. now we have to append original length.
the original length is 48 bits.
the 64 bit binary representation of 48 is 0x8 0x8 0x8 0x8 0x8 0x8 0x8 00110000.

*/