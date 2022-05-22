package clientapp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class StudentSchedule extends JPanel implements ActionListener
{
	 JLabel heading;
	 JTable sTable;
	 Object HEAD[]={"MName","Date","Time","Duration","Topic"};
	 Object DATA[][];
	 JScrollPane jspSession;
	 JButton btnCompile;
	 JLabel sTopic,sDuration,sTime,sName,sActive;
	 
     public StudentSchedule()
     {
    	 this.setLayout(null);
    	 DATA=new String[100][6];
    	 
    	 btnCompile=new JButton("OnlineCompiler");
    	 this.add(btnCompile);
    	 btnCompile.addActionListener(this);
    	 btnCompile.setBounds(550,10,130,30);
    	 
    	 heading=new JLabel("Availabel Sessions are......");
    	 this.add(heading);
    	 heading.setBounds(30,20,250,30);
    	 heading.setFont(new Font("Garamond",Font.BOLD,20));
    	 
    	 this.sTable=new JTable(this.DATA,this.HEAD);
    	 JScrollPane jspSession=new JScrollPane(sTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 this.add(jspSession);
    	 jspSession.setBounds(30,50,540,350);
    	 
    	   sActive=new JLabel();
    	   sTopic=new JLabel();
    	   sDuration=new JLabel();
    	   sTime=new JLabel();
    	   sName=new JLabel();
    	   
    	   this.add(sActive);
    	   this.add(sTopic);
    	   this.add(sDuration);
    	   this.add(sTime);
    	   this.add(sName);
    	   
    	   sActive.setFont(new Font("Serif", Font.BOLD+Font.ITALIC, 20));
    	   
    	   sActive.setBounds(30,415,250,25);
    	   sTopic.setBounds(30,445,200,25);
    	   sName.setBounds(30,470,200,25);
    	   sTime.setBounds(30,495,300,25);
    	   sDuration.setBounds(30,520,200,25);
    	   
    	   sActive.setForeground(Color.RED);
    	   sTopic.setForeground(Color.BLUE);
    	   sName.setForeground(Color.BLUE);
    	   sTime.setForeground(Color.BLUE);
    	   sDuration.setForeground(Color.BLUE);
    	 
     }
     public void actionPerformed(ActionEvent ae)
     {
    	 if(ae.getSource().equals(btnCompile))
    	 {
    		  ClientRes.swin.rsCompile=new RemoteCompileS(); 
	    	  ClientRes.swin.jtb.addTab("OnlineCompile",ClientRes.swin.rsCompile); 
		 }
     }
}
