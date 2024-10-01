import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
// import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class image_processing {
    public static void main(String[] args) {
        // ImagePlus image = IJ.openImage("C:\\Users\\madha\\Pictures\\madhav2.jpg");

        String image = "C:\\Users\\madha\\Pictures\\madhav2.jpg";

        // ImageProcessor processor = image.getProcessor();

        // int width = processor.getWidth();
        // System.out.println("Image Width: " + width);

        // int height = processor.getHeight();
        // System.out.println("Image Height: " + height);
        
        byte[] byteArrayOfImage = convertImageIntoByteArray(image);
        System.out.println("Byte Array Length: " + byteArrayOfImage.length);

        String hashString = imagehash(byteArrayOfImage);
        System.out.println("Hash String: " + hashString);
    }

 
    
    public static byte[] convertImageIntoByteArray(String Path){

        try{
            ImagePlus imagePlus = new ImagePlus(Path);

            ImageProcessor ip = imagePlus.getProcessor();

            BufferedImage bufferedImage = ip.getBufferedImage();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String imagehash(byte[] byteArrayImage){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(byteArrayImage);

            StringBuilder hashString = new StringBuilder();

            for(byte b : hash){
                String hexString = Integer.toHexString(0xff & b);
                if(hexString.length() == 1){
                    hashString.append("0");
                }
                hashString.append(hexString);
            }

            return hashString.toString();

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
