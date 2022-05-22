package clientapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;

import clientapp.MentorSession.SoundRecorder;

public class StudentSession extends JPanel implements ActionListener
{
	 JLabel labTopic,labTitle,labCE,labDis,labQuery;
	 JTextArea txtCE,txtDis,txtQuery;
	 JTextField fTopic,fTitle;
	 JButton butsend,btnStart,btnStop,btnCompile;
	 JScrollPane scrollCE,scrollDis,scrollReply;
	 
	 TargetDataLine line;
	 JTable tblDis;

	 Object HEAD[]={"Name","Comment"};
	 Object DATA[][];
	 int index;
	 
	 
     public StudentSession()
     {
    	 this.setLayout(null);
    	 //this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	 //this.setTitle("ChatWindow");
    	 this.DATA=new Object[500][2];
    	 this.index=0;  	 
    	 
    	 this.labTopic=new JLabel("Topic :");
    	 this.labTitle=new JLabel("Title   :");
    	 this.labCE=new JLabel("Code Editor :");
    	 this.labDis=new JLabel("Discussion :");
    	 this.labQuery=new JLabel("Query :");
    	 
    	 this.fTopic=new JTextField();
    	 this.fTitle=new JTextField();
    	 
    	 this.txtCE=new JTextArea();
    	 this.txtDis=new JTextArea();
    	 this.txtQuery=new JTextArea();
    	 
    	 this.btnCompile=new JButton("OnlineCompiler");
    	 this.butsend=new JButton("Reply");
    	 this.btnStart=new JButton("Start");
    	 this.btnStop=new JButton("Stop");
    	 this.butsend.setFont(new Font("Garamond",Font.BOLD,20));
    	 
    	 this.tblDis=new JTable(DATA,HEAD);
    	 this.tblDis.setShowGrid(false);
    	 this.tblDis.setBackground(Color.CYAN);
    	 this.tblDis.setSelectionBackground(Color.RED);
    	 this.tblDis.setSelectionForeground(Color.BLACK);
    	 
    	 
    	 this.scrollCE=new JScrollPane(txtCE,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 this.scrollDis=new JScrollPane(tblDis,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 this.scrollReply=new JScrollPane(txtQuery,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 
    	 this.labTopic.setBounds(20,10,100,25);
    	 this.fTopic.setBounds(100,10,300,25);
    	 this.labTitle.setBounds(20,40,100,25);
    	 this.fTitle.setBounds(100,40,300,25);
    	  
    	 this.labCE.setBounds(20,80,150,20);
    	 this.scrollCE.setBounds(20,110,400,400);
    	 
    	 this.labDis.setBounds(420,10,150,25);
    	 this.scrollDis.setBounds(420,30,265,300);
    	 
    	 this.labQuery.setBounds(420,330,150,20);
    	 this.scrollReply.setBounds(420,350,265,155);
    	 
    	 this.butsend.setBounds(480,520,100,30);
    	 this.btnStart.setBounds(170,80,100,25);
    	 this.btnStop.setBounds(300,80,100,25);
    	 this.btnCompile.setBounds(180,520,150,30);
    	 
    	 this.labTopic.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labTitle.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labCE.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labDis.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labQuery.setFont(new Font("Garamond",Font.BOLD,20));
    	 
    	 //Add txt Label
    	 this.add(labTopic);
    	 this.add(labTitle);
    	 this.add(labCE);
    	 this.add(labDis);
    	 this.add(labQuery);
    	 
    	 //Add txt Field
    	 this.add(fTopic);
    	 this.add(fTitle);
    	 
    	 //Add Button
    	 
    	 this.add(butsend);
    	 this.add(btnStart);
    	 this.add(btnStop);
    	 this.add(btnCompile);
    	 
    	 //Add ScrollPane
    	 this.add(scrollCE);
    	 this.add(scrollDis);
    	 this.add(scrollReply);
    	 
    	 this.fTopic.setEditable(false);
    	 this.fTitle.setEditable(false);
    	 this.txtCE.setEditable(false);
    	 this.txtDis.setEditable(false);
    	 
    	 btnStart.setEnabled(true);
    	 btnStop.setEnabled(false);
    	 
    	 this.butsend.addActionListener(this);
    	 this.btnStart.addActionListener(this);
         this.btnStop.addActionListener(this);
         this.btnCompile.addActionListener(this);
 
     }
     
     class SoundRecorder extends Thread
     {
    	 public void run()
    	 {
    		 try
    		 {
    			 DataLine.Info info;
     			
     			AudioFormat format=new AudioFormat(16000,8,2,true,true);
     			
     			info=new DataLine.Info(TargetDataLine.class,format);
     			
     			//Check if System supports the data line
     			if(!AudioSystem.isLineSupported(info))
     			{
     				JOptionPane.showMessageDialog(StudentSession.this, "Audio Line Not Supported!!!","Audio Recording",JOptionPane.ERROR_MESSAGE);
     				return;
     			}
     			line = (TargetDataLine)AudioSystem.getLine(info);
     			line.open(format);
     			line.start();
     			
     			AudioInputStream ais=new AudioInputStream(line);
     			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("F:/test.wav"));
     		
     			btnStart.setEnabled(false);
     			btnStop.setEnabled(true);
     		 
    		 }
    		 catch(Exception ex){
    			 
    		 }
    	 }
     }
     
     
    
     
     
     public void actionPerformed(ActionEvent ae)
     {
    	if(ae.getSource()==this.butsend)
    	{
    		String answer=txtQuery.getText();
    		txtQuery.setText("");
    		try
    		{
    			ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
    			out.writeObject("DiscussionText");
    			out.writeObject(answer);
    		}
    		catch(Exception ex)
    		{
    		 
    		}
    	}
    	if(ae.getSource()==this.btnStart)
    	{
    		try
    		{
    			btnStart.setEnabled(false);
      			btnStop.setEnabled(true);
    			new SoundRecorder().start();		
    		}
    		catch(Exception ex)
    		{
    			JOptionPane.showMessageDialog(this, "Problem in Audio Line!!!","Audio Recording",JOptionPane.ERROR_MESSAGE);
    		}
    	}
    	if(ae.getSource()==this.btnStop)
    	{
    		try
    		{
    			this.line.stop();
    			this.line.close();
    			btnStart.setEnabled(true);
      			btnStop.setEnabled(false);
      			try
	   	    	 {
	   	    	 
                         ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
	   	    	 out.writeObject("DiscussionAudio");
		         File file=new File("test.wav");
		         FileInputStream fin=new FileInputStream(file);
		         byte barr[]=new byte[(int)file.length()];
		         fin.read(barr);
		         fin.close();
		         out.writeObject(barr);
	   	    	 
	   	    	 }
	   	    	 catch(Exception ex)
	   	    	 {
	   	    		 
	   	    	 }
      			
    		}
    		catch(Exception ex)
    		{
    			
    		}
    	}
    	if(ae.getSource()==this.btnCompile)
    	{
    		ClientRes.swin.rsCompile=new RemoteCompileS();
    		ClientRes.swin.jtb.addTab("OnlineCompile",ClientRes.swin.rsCompile);
    	}
     }
}