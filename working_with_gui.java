import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;


import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;



public class working_with_gui implements ActionListener{

    static JFrame frame = new JFrame("Gemini");
    static JPanel panel = new JPanel();
    static JTextField question_field = new JTextField();
    static JButton submit_button = new JButton("Submit");
    static JTextArea answer_field = new JTextArea();
    static JScrollPane scrollPane;
    static String question = "";
    static String bot_response = "";


    private static String dburl = "jdbc:mysql://localhost:3306/";
    private static String username = "root";
    private static String password = "1234";
    working_with_gui(){
        prepareGui();
        addActionEvent();
    }

    public static void prepareGui(){
        frame.setSize(300,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(null);

        panel.setLayout(null);
        panel.setLocation(40,10);
        panel.setSize(200,400);
        panel.setBackground(Color.decode("#D2D4C8"));
        

        answer_field.setLineWrap(true);
        answer_field.setWrapStyleWord(true);
        answer_field.setEditable(false);
        
        scrollPane = new JScrollPane(answer_field);
        scrollPane.setBounds(10,10,180,380);

        panel.add(scrollPane);

        question_field.setSize(120,30);
        question_field.setLocation(40,410);

        submit_button.setBounds(165,410,75,30);

        frame.getContentPane().add(submit_button);
        frame.getContentPane().add(question_field);
        frame.getContentPane().add(panel);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        frame.revalidate();
        frame.repaint();


    }

    public void add_answerField(String answer){
        answer_field.append(answer + "\n");
    
        panel.revalidate();
        panel.repaint();
    }

    public void addActionEvent(){
        submit_button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e){
        Object object = e.getSource();

        if(object == submit_button){
            question = question_field.getText();
            apiHandling(question);
            add_answerField("Question: " + question + "\nAnswer: " + bot_response);
        }
    }
    public static void main(String[] args) {
        new working_with_gui();
    }

    @SuppressWarnings("deprecation")
    public String apiHandling(String question){

        try{
            String apiurl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyBZ_CESLpFQErBAN-rnhzz0rWse1JSs85o";
            URL url = new URL(apiurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setDoOutput(true);

            String requestMethod = "{\"contents\":[{\"parts\":[{\"text\":\"" + question + "\"}]}]}";

            try(PrintWriter out = new PrintWriter(connection.getOutputStream(),true)){
                out.println(requestMethod);
            }

            int responseCode = connection.getResponseCode();
            // String responseMessage = connection.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder packet = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    packet.append(inputLine);                    
                }

                JSONObject jsonObject = new JSONObject(packet.toString());

                JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
                if(candidatesArray.length() > 0){
                    JSONObject candidateObject = candidatesArray.getJSONObject(0);
                    JSONObject content = candidateObject.getJSONObject("content");

                    JSONArray parts = content.getJSONArray("parts");

                    if(parts.length() > 0){
                        JSONObject part = parts.getJSONObject(0);
                        bot_response = part.getString("text");


                    }
                }
            }else{
                bot_response = "Error Code: " + responseCode;
            }

            chatDataBase(question, bot_response);
            
            return bot_response;
            
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }

    }


    public static void chatDataBase(String question, String answer){

        try(Connection connection = DriverManager.getConnection(dburl,username,password);
        Statement statememt = connection.createStatement()){

            String createdb = "create database if not exists chatbot";
            statememt.executeUpdate(createdb);

            String usedb = "use chatbot";
            statememt.executeUpdate(usedb);

            String createtb = "create table if not exists query (question text, answer text)";
            statememt.executeUpdate(createtb);

            String insertquery = "insert into query (question,answer) values (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertquery);
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, bot_response);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}
