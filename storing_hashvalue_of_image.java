import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class storing_hashvalue_of_image {

    private static String dbUrl = "jdbc:mysql://localhost:3306/";
    private static String username = "root";
    private static String password = "1234";
    public static void main(String[] args) {
        String imagePath = "C:\\Users\\madha\\Pictures\\madhav4.jpg";

        byte[] imageArray = convertinmageIntoByteArray(imagePath);

        String hashImage = imagehashvalue(imageArray);

        System.out.println("Image hash value : " + hashImage);
        
        handleSql(imagePath, hashImage);
    }

    public static byte[] convertinmageIntoByteArray(String imagepath){
        try{
            ImagePlus img = IJ.openImage(imagepath);

            ImageProcessor processor = img.getProcessor();

            BufferedImage bufferedImage = processor.getBufferedImage();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String imagehashvalue(byte[] imageByteArray){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(imageByteArray);

            StringBuilder hashString = new StringBuilder();

            for(byte b : hash){
                String hext = Integer.toHexString(0xff & b);
                if(hext.length() == 1){
                    hashString.append("0");
                }
                hashString.append(hext);
            }

            return hashString.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static void handleSql(String imagePath,String hashValue){
        try(Connection connection = DriverManager.getConnection(dbUrl,username,password);
        Statement statement = connection.createStatement()){
            String createdb = "create database if not exists image_hash_value";
            statement.executeUpdate(createdb);

            String usedb = "use image_hash_value";
            statement.executeUpdate(usedb);

            String createtb = "create table if not exists ImgHashValue(imagePath text, imageHash text)";
            statement.executeUpdate(createtb);

            PreparedStatement preparedStatement = connection.prepareStatement("insert into ImgHashValue(imagePath, imageHash) values (?,?)");
            preparedStatement.setString(1, imagePath);
            preparedStatement.setString(2, hashValue);
            preparedStatement.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
