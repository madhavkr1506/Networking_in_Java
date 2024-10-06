import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;


public class madhav_ implements ActionListener{

    JFrame frame = new JFrame("madhav_");
    JTextArea chat_area = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(chat_area);
    JTextField input_field = new JTextField();
    JButton send_button = new JButton();
    Socket client_socket;
    PrintWriter output_writer;
    BufferedReader reader;
    boolean running = true;
    String client_name = "madhav_: ";

    madhav_(){
        prepareGui();
        handle_multiple_client();
        addActionListeners();
    }
    void prepareGui(){
        frame.setSize(300,500);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.decode("#40E0D0"));
        frame.getContentPane().setLayout(null);
        

        chat_area.setBackground(Color.decode("#000080"));
        chat_area.setEditable(false);
        chat_area.setWrapStyleWord(true);
        chat_area.setLineWrap(true);
        chat_area.setForeground(Color.decode("#000000"));
        chat_area.setFont(new Font("Arial",Font.CENTER_BASELINE,15));
        scrollPane.setSize(300,400);
        scrollPane.setLocation(0,0);

        input_field.setSize(170,30);
        input_field.setLocation(10,410);

        send_button.setText("Send");
        send_button.setSize(70,30);
        send_button.setLocation(200,410);
        send_button.setEnabled(false);

        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(input_field);
        frame.getContentPane().add(send_button);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    void addActionListeners(){
        send_button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == send_button){
            String message = input_field.getText().trim();
            if(output_writer != null){
                output_writer.println(client_name + message);
                output_writer.flush();
                input_field.setText("");
            }
        }
    }
    public static void main(String[] args) {
        new madhav_();
    }



    void handle_multiple_client(){
        try{
            client_socket = new Socket("localhost",50000);
            output_writer = new PrintWriter(client_socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            send_button.setEnabled(true);

            new Thread(new Runnable() {
                public void run(){
                    String server_message;
                    try{
                        while (running && (server_message = reader.readLine()) != null) {
                            System.out.println(server_message);
                            chat_area.append(server_message + "\n");
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
}