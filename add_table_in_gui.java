import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.awt.*;


public class add_table_in_gui {
    
    static JFrame frame = new JFrame("PC Details");
    static DefaultTableModel dynamicmodel;
    static JTable table;
    static JScrollPane scrollPane;
    static String column[] = {"Name","ID","Marks obt.","Total Mar","Grade"};

    add_table_in_gui(){
        prepareGui();
        inputValue();
    }
    public static void prepareGui(){
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.decode("#f9dcc4"));
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        
        dynamicmodel = new DefaultTableModel(column,0);
        table = new JTable(dynamicmodel);
        
        table.setBackground(Color.decode("#a4c3b2"));
        table.setForeground(Color.decode("#0d0a0b"));
        table.setFont(new Font("Arial",Font.CENTER_BASELINE,12));

        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300,400));

        frame.getContentPane().add(scrollPane);        

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void inputValue(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input Details: ");
        String inputLine;
        try{
            while ((inputLine = reader.readLine()) != null) {
                String[] data = inputLine.trim().split(" ");
                dynamicmodel.addRow(data);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
        

    }
    public static void main(String[] args) {
        new add_table_in_gui();
    }
}
