package socket;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;



/**
 * Serveur
 */
public class Serveur {

    static ArrayList<MyFile> myFiles=new ArrayList<>();
    public static void main(String[] args) throws Exception{
        int fileId=0;
        JFrame f=new JFrame("SERVER");
        f.setTitle("SERVER PROJET");
        f.setSize(400, 400);
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
       
         
        JPanel p=new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.white);
        JScrollPane jPane=new JScrollPane();
        jPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel label=new JLabel("file received");
        label.setFont(new Font("Arial",Font.BOLD,25));
        label.setBorder(new EmptyBorder(20, 0, 10, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        f.add(label);
        f.add(jPane);
        f.setVisible(true);

        ServerSocket socket=new ServerSocket(1243);
        while(true)
        {
          try{
                Socket socket2=socket.accept();
                DataInputStream dataInputStream=new DataInputStream(socket2.getInputStream());
                int  filenamelength=dataInputStream.readInt();
                if(filenamelength>0)
                {
                    byte[] filenamebytes=new byte[filenamelength];
                    dataInputStream.readFully(filenamebytes,0,filenamebytes.length);
                    String filename=new String(filenamebytes);

                    int filecontentlength=dataInputStream.readInt();
                    if(filecontentlength>0)
                    {
                        byte[] filecontentBytes=new byte[filecontentlength];
                        dataInputStream.readFully(filecontentBytes, 0, filecontentlength);
                    JPanel panel=new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                        JLabel label2=new JLabel(filename);
                        label2.setFont(new Font("Arial",Font.BOLD,20));
                        label2.setBorder(new EmptyBorder(10, 0, 10, 0));
                        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
                        if(getFileExtensions(filename).equalsIgnoreCase("txt"))
                        {
                            panel.setName(String.valueOf(fileId));
                            panel.addMouseListener(getMyMouseListener());


                            panel.add(label2);
                            jPane.add(panel);
                            f.validate();
                            
                        }else{
                            panel.setName(String.valueOf(fileId));
                            panel.addMouseListener(getMyMouseListener());

                            panel.add(label2);
                            jPane.add(panel);

                            f.validate();

                    }
                    myFiles.add(new MyFile(fileId, filename, filenamebytes, getFileExtensions(filename))); 
                    fileId++;
                }
  
            }
        }
        catch(Exception e)
        {
            e.getMessage();
            e.printStackTrace();
        }


        }
    }

    public static MouseListener getMyMouseListener(){
        return new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel=(JPanel) e.getSource();
                
                int fileId=Integer.parseInt(jPanel.getName());

                for(MyFile myFile:myFiles)
                {
                    if (myFile.getId()==fileId) {
                        JFrame jF=createFrame(myFile.getName(),myFile.getData(),myFile.getFileExtensions());
                        jF.setVisible(true);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                
            }
            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        };
    }
    public static JFrame createFrame(String fichier,byte[] fileData,String filExtensions) {
        JFrame jFrame=new JFrame("withcode's file download");
        jFrame.setSize(400, 400);

        JPanel jPanel=new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
        JLabel jLabel=new JLabel("file download");
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel.setFont(new Font("Arial",Font.BOLD,25));
        jLabel.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel labb=new JLabel("are you sure you want to dowload"+fichier);
        labb.setFont(new Font("Arial",Font.BOLD,20));
        labb.setBorder(new EmptyBorder(20, 0, 10, 0));
        labb.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton butt=new JButton("yes");
        butt.setPreferredSize(new Dimension(150,20));
        butt.setFont(new Font("Arial",Font.BOLD,20));

        
        JButton butt1Button=new JButton("no");
        butt1Button.setPreferredSize(new Dimension(150,20));
        butt1Button.setFont(new Font("Arial",Font.BOLD,20));
    
        JLabel jLabel2=new JLabel();
        jLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jPanel2=new JPanel();
        jPanel2.setBorder(new EmptyBorder(20, 0, 10, 0));
        jPanel2.add(butt1Button);
        jPanel2.add(butt);

        if(filExtensions.equalsIgnoreCase("txt"))
        {
            jLabel2.setText("<html>"+new String(fileData)+"</html>");
        }
        else{
            jLabel2.setIcon(new ImageIcon(fileData));
        }
        butt.addActionListener(new ActionListener(){
            @Override

            public void actionPerformed(ActionEvent e) {
                File fileToDownload= new File(fichier);

                try {
                    FileOutputStream fileOutputStream=new FileOutputStream(fileToDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                    jFrame.dispose();

                } catch (Exception exception) {

                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    // TODO: handle exception
                }
            }
        });
        butt1Button.addActionListener(new ActionListener() {
            @Override
            
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });

        jPanel.add(jLabel);
        jPanel.add(labb);
        jPanel.add(jLabel2);
        jPanel.add(jPanel2);

        jFrame.add(jPanel);

        return jFrame;

    }
    
    public static String getFileExtensions(String files) {
        int i=files.lastIndexOf(".");
        if(i>0)
        {
            return files.substring(i+1);
        }else{
            return "no extensions found";
        }
    
}
}