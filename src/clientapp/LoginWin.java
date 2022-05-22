package clientapp;
import java.lang.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;


public class LoginWin extends JFrame implements ActionListener
{
    JLabel logas,idlbl,pwdlbl;
    JTextField logid;
    JPasswordField pwd;
    JButton logbtn;
    ButtonGroup cbg;
    JRadioButton rdstdnt,rdmntr;


    public LoginWin()
    {
       this.setLayout(null);

       this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

       logas=new JLabel("Login As : ");
       cbg=new ButtonGroup();
       rdstdnt=new JRadioButton("Student");
       rdmntr=new JRadioButton("Mentor");
       rdstdnt.setSelected(true);
         cbg.add(rdstdnt);
         cbg.add(rdmntr);

       idlbl=new JLabel("Login ID :");
       pwdlbl=new JLabel("Password :");
       logid=new JTextField();
       pwd=new JPasswordField();
       logbtn=new JButton("Login");
       logbtn.addActionListener(this);
       this.add(logas);
       //this.add(cbg);
       this.add(rdstdnt);
       this.add(rdmntr);
       this.add(idlbl);
       this.add(logid);
       this.add(pwdlbl);
       this.add(pwd);
       this.add(logbtn);

       logas.setBounds(40,30,100,25);
       rdstdnt.setBounds(120,30,100,25);
       rdmntr.setBounds(120,70,100,25);
       idlbl.setBounds(40,110,100,25);
       logid.setBounds(120,110,150,25);
       pwdlbl.setBounds(40,150,100,25);
       pwd.setBounds(120,150,150,25);
       logbtn.setBounds(100,190,100,25);
    }
    public void actionPerformed(ActionEvent ae)
    {
       //validate input

        try
        {
         String type="";
         if(this.rdmntr.isSelected())
            type="mentor";
         else
            type="student";

          ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
          out.writeObject("LogDetails");
          out.writeObject(this.logid.getText().trim());
          out.writeObject(new String(this.pwd.getPassword()).trim());
          out.writeObject(type);
          
          ObjectInputStream in=new ObjectInputStream(ClientRes.client.getInputStream());
          String resp=in.readObject().toString();
          
          if(resp.equals("Valid"))
          {
        	  JOptionPane.showMessageDialog(this,"Successfully Logged In","Login Entry",JOptionPane.INFORMATION_MESSAGE);
        	  ClientRes.type=type;
        	  ClientRes.logid=this.logid.getText().trim();
        	  if(type.equals("mentor"))
                  {
        		     String name=in.readObject().toString();  
                             
        		     new MentorThread();   //Create a Mentor Thread
                             
                     ClientRes.mwin=new MentorWin();
                     Toolkit tool=Toolkit.getDefaultToolkit();
                     Dimension size=tool.getScreenSize();
                     final int WIDTH=700;
                     final int HEIGHT=620;
                     ClientRes.mwin.setBounds(size.width/2-WIDTH/2,size.height/2-HEIGHT/2,WIDTH,HEIGHT);
                     ClientRes.mwin.setResizable(false);
                     ClientRes.mwin.setTitle("Welcome: " + name + "[Mentor]");
                     ClientRes.mwin.setVisible(true);
                    
                     ObjectOutputStream tmpout=new ObjectOutputStream(ClientRes.client.getOutputStream());
                     tmpout.writeObject("ActiveSession"); 
                     
                     this.dispose(); 
              }
        	  else
        	  {
        		  String name=in.readObject().toString();
                          
     		          new StudentThread(); //Create a Student Thread
                          
                  ClientRes.swin=new StudentWin();
                  Toolkit tool=Toolkit.getDefaultToolkit();
                  Dimension size=tool.getScreenSize();
                  final int WIDTH=700;
                  final int HEIGHT=620;
                  ClientRes.swin.setBounds(size.width/2-WIDTH/2,size.height/2-HEIGHT/2,WIDTH,HEIGHT);
                  ClientRes.swin.setResizable(false);
                  ClientRes.swin.setTitle("Welcome: " + name + "[Student]");
                  ClientRes.swin.setVisible(true);
                 
                  ObjectOutputStream tmpout=new ObjectOutputStream(ClientRes.client.getOutputStream());
                  tmpout.writeObject("ActiveSession"); 
        
                  this.dispose();  
        	  }
        	    
          }
          else
          {
        	  JOptionPane.showMessageDialog(this,"Invalid Login/Password!!!","Login Entry",JOptionPane.ERROR_MESSAGE);    	  
          }
          
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	JOptionPane.showMessageDialog(this,ex.toString(),"Error",JOptionPane.ERROR_MESSAGE);
        }

    }
}