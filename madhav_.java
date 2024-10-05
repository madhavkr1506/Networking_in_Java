import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class madhav_ implements ActionListener{

    static JFrame frame = new JFrame("madhav_");
    static JTextArea chat_area = new JTextArea();
    static JScrollPane scrollPane = new JScrollPane(chat_area);
    static JTextField input_field = new JTextField();
    static JButton send_button = new JButton();
    static Socket client_socket;
    static PrintWriter output;
    static BufferedReader reader;
    static boolean running = true;
    static String client_name = "Madhav";
    static OutputStream output_stream;
    static FileInputStream file_input;

    static JButton load_image = new JButton("Img");



    madhav_(){
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

        chat_area.setBackground(Color.decode("#fefae0"));
        chat_area.setLineWrap(true);
        chat_area.setWrapStyleWord(true);
        chat_area.setEditable(false);
        scrollPane.setSize(300,400);
        scrollPane.setLocation(0,0);
        

        input_field.setSize(120,30);
        input_field.setLocation(15,420);

        load_image.setText("Img");
        load_image.setSize(60,30);
        load_image.setLocation(135,420);
        load_image.setEnabled(false);

        send_button.setText("Send");
        send_button.setSize(70,30);
        send_button.setLocation(200,420);
        send_button.setEnabled(false);

        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(input_field);
        frame.getContentPane().add(load_image);
        frame.getContentPane().add(send_button);

        frame.revalidate();
        frame.repaint();

    }

    public static void handleclient(){
        try{

            client_socket = new Socket("localhost",50000);
            reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            output = new PrintWriter(client_socket.getOutputStream());

            send_button.setEnabled(true);
            load_image.setEnabled(true);

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
        load_image.addActionListener(this);

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
                System.out.println("Connection is not established yet, please try after sometime.");
            }

        }
        if(object == load_image){
            JFileChooser filechooser = new JFileChooser();
            filechooser.setDialogTitle("Select Img");
            int result = filechooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                File image = filechooser.getSelectedFile();
                try{
                    if(image != null){
                        sendImage(image);
                    }   
                }catch(Exception ex){
                    ex.printStackTrace();
                }

            }
            
        }
    }

    public static void sendImage(File image_path){
        try{
            if(client_socket != null && image_path != null){
                file_input = new FileInputStream(image_path);
                output_stream = client_socket.getOutputStream();

                byte[] buffer = new byte[4096];
                int byteRead;

                while ((byteRead = file_input.read(buffer)) > 0) {
                    output_stream.write(buffer,0,byteRead);
                    output_stream.flush();
                }
                file_input.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new madhav_();
    }
}