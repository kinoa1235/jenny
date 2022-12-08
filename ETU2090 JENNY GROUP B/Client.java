package socket;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    /**
     * @param args
     */
    public static void main(String[] args)throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {

        InetAddress host = InetAddress.getLocalHost();
        final File[] fileToSend=new File[1];
        JFrame f=new JFrame("Client");
        f.setTitle("PROJET SOCKET");
        f.setSize(450, 450);
        f.setLayout(new BoxLayout(f.getContentPane(),BoxLayout.Y_AXIS));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      

        var label=new JLabel("file sender");
        //label.setFont("Arial",Font.BOLD,25);
        label.setBorder(new EmptyBorder(20,0,10,0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label2=new JLabel("choose a file to send");
        //label2.setFont("Arial",Font.BOLD,25);
        label2.setBorder(new EmptyBorder(50,0,0,0));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel p=new JPanel();
        p.setBorder(new EmptyBorder(75,0,10,0));
        JButton b=new JButton("send file");
        b.setPreferredSize(new Dimension(150,75));
        b.setFont(new Font("Arial",Font.BOLD,20));
        p.setBackground(Color.pink);

        JButton button=new JButton("choose file");
        button.setPreferredSize(new Dimension(150,75));
        button.setFont(new Font("Arial",Font.BOLD,20));
        p.add(b);
        p.add(button);

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser chooser=new JFileChooser();
                chooser.setDialogTitle("choose file to send");
                if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
                {
                    fileToSend[0]=chooser.getSelectedFile();
                    label2.setText("the file you want send is"+ "  "+  fileToSend[0].getName());
                }
            }
        });



        b.addActionListener(new ActionListener(){
            @Override
            public  void actionPerformed(ActionEvent e) {
                if(fileToSend[0]==null)
                {
                    label.setText("PLEASE CHOOSE A FILE FIRST");
                }else{
                    try{
                    FileInputStream fileInputStream=new FileInputStream(fileToSend[0].getAbsolutePath());
                    Socket socket=new Socket(InetAddress.getLocalHost(),8070);

                    DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());

                    String filename=fileToSend[0].getName();
                    byte[] filenamebytes=filename.getBytes();

                    byte[] filecontentBytes=new byte[(int)fileToSend[0].length()];
                    fileInputStream.read(filecontentBytes);

                    dataOutputStream.writeInt(filenamebytes.length);
                    dataOutputStream.write(filenamebytes);

                    dataOutputStream.writeInt(filecontentBytes.length);
                    dataOutputStream.write(filecontentBytes);
                    }catch(IOException ev)
                    {
                        ev.printStackTrace();
                    }
                }
            }
        });
        f.add(label);
        f.add(label2);
        f.add(p);
        f.setVisible(true);
    }
}