package clientapp;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class IpEntry extends JFrame implements ActionListener
{
    JLabel addrs;
    JTextField iptxt;
    JButton connect;

    public IpEntry()
    {
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLayout(null);

        addrs=new JLabel("IP Address :");
        iptxt=new JTextField();
        connect=new JButton("Connect");
        //connect.setBackground(Color.lightGray);
        this.add(addrs);
        this.add(iptxt);
        this.add(connect);
 
        addrs.setBounds(30,20,100,25);
        iptxt.setBounds(110,20,140,25);
        connect.setBounds(80,60,100,30);

        connect.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae)
    {
        //connect.setText("Clicked");
        if(this.iptxt.getText().trim().equals(""))
        {
          JOptionPane.showMessageDialog(this,"IP Address Not Entered!!!","IP Entry",JOptionPane.ERROR_MESSAGE);
          return;
        }
        try
        {
           InetAddress inet=InetAddress.getByName(this.iptxt.getText().trim());
           ClientRes.client=new Socket(inet,2200);
           JOptionPane.showMessageDialog(this,"Connected To server Successfully","IP Entry",JOptionPane.INFORMATION_MESSAGE);

        LoginWin win=new LoginWin();
        Toolkit tool=Toolkit.getDefaultToolkit();
        Dimension size=tool.getScreenSize();
        final int WIDTH=500;
        final int HEIGHT=500;
        win.setBounds(size.width/2-WIDTH/2,size.height/2-HEIGHT/2,WIDTH,HEIGHT);
        win.setResizable(false);
        win.setTitle("Login Window");
        win.setVisible(true);
        this.dispose(); 
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this,"Cannot Connect To server!!!","IP Entry",JOptionPane.ERROR_MESSAGE);
        }

    }
}