import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class krishna_ implements ActionListener{

    static JFrame frame = new JFrame("krishna_");
    static JTextArea chat_area = new JTextArea();
    static JTextField input_field = new JTextField();
    static JButton send_button = new JButton();

    static Socket client_socket;
    static BufferedReader reader;
    static PrintWriter output;
    static boolean running = true;
    static String client_name = "Krishna";


    krishna_(){
        prepareGui();
        handleclient();
        addActionListener();
    }

    public static void prepareGui(){
        frame.setSize(300,500);
        frame.getContentPane().setBackground(Color.decode("#dda15e"));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        chat_area.setSize(300,400);
        chat_area.setLocation(0,0);
        chat_area.setBackground(Color.decode("#fefae0"));
        chat_area.setEditable(false);

        input_field.setSize(120,30);
        input_field.setLocation(15,420);

        send_button.setText("Send");
        send_button.setSize(120,30);
        send_button.setLocation(150,420);
        send_button.setEnabled(false);

        frame.getContentPane().add(chat_area);
        frame.getContentPane().add(input_field);
        frame.getContentPane().add(send_button);

    }

    public static void handleclient(){
        try{
            client_socket = new Socket("localhost",50000);
            reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            output = new PrintWriter(client_socket.getOutputStream());

            send_button.setEnabled(true);

            new Thread(new Runnable() {
                public void run(){
                    
                    try{
                        String serverMessage;
                        while (running && (serverMessage = reader.readLine()) != null) {
                            chat_area.append(serverMessage + "\n");
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        try{
                            if(client_socket != null){
                                client_socket.close();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                }
            }).start();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addActionListener(){
        send_button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e){
        Object object = e.getSource();
        if(object == send_button){
            String message = input_field.getText().trim();
            input_field.setText("");
            if(output != null){
                output.println(client_name + ": " + message);
                output.flush();
            }
            else{
                System.out.println("Connection is not established yet, please try after some time.");
            }
        }
    }


    public static void main(String[] args) {
        new krishna_();
    }
}