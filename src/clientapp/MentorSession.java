package clientapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.spi.AudioFileWriter;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;


public class MentorSession extends JPanel implements ActionListener
{ 
	 JLabel labTopic,labTitle,labCE,labDis,labAnswer;
	 JTextArea txtCE,txtAnswer;
	 JTextField fTopic,fTitle;
	 JButton butsend,btnStart,btnStop,btnSaveEnd,btnEnd;
	 JScrollPane scrollCE,scrollDis,scrollReply;
	 
	 TargetDataLine line;
	 JTable tblDis;

	 Object HEAD[]={"Name","Comment"};
	 Object DATA[][];
	 int index;
	 
     public MentorSession()
     {
    	 this.setLayout(null);
    	
    	 
    	 this.DATA=new Object[500][2];
    	 
    	 for(int i=0;i<500;i++)
    		 for(int j=0;j<2;j++)
    			 DATA[i][j]="";
    	 
     	 this.index=0;
    	 this.labTopic=new JLabel("Topic :"); 
    	 this.labTitle=new JLabel("Title   :");
    	 this.labCE=new JLabel("Code Editor :");
    	 this.labDis=new JLabel("Discussion :");
    	 this.labAnswer=new JLabel("Answer :");
    	 
    	 this.fTopic=new JTextField();
    	 this.fTitle=new JTextField();
    	 
    	 this.txtCE=new JTextArea();
    	 
    	 this.txtAnswer=new JTextArea();
    	 
    	 this.tblDis=new JTable(DATA,HEAD);
    	 this.tblDis.setShowGrid(false);
    	 this.tblDis.setBackground(Color.CYAN);
    	 this.tblDis.setSelectionBackground(Color.RED);
    	 this.tblDis.setSelectionForeground(Color.BLACK);
    	 
    	 
    	 this.butsend=new JButton("Reply");
    	 this.btnStart=new JButton("Start");
    	 this.btnStop=new JButton("Stop");
    	 this.btnSaveEnd=new JButton("Save+End");
    	 this.btnEnd=new JButton("End");
    	 
    	 
    	 this.scrollCE=new JScrollPane(txtCE,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 this.scrollDis=new JScrollPane(tblDis,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 this.scrollReply=new JScrollPane(txtAnswer,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 
    	 this.labTopic.setBounds(20,10,100,25);
    	 this.fTopic.setBounds(100,10,300,25);
    	 this.labTitle.setBounds(20,40,100,25);
    	 this.fTitle.setBounds(100,40,300,25);
    	  
    	 this.labCE.setBounds(20,80,150,20);
    	 this.scrollCE.setBounds(20,110,400,400);
    	 
    	 this.labDis.setBounds(420,10,150,25);
    	 this.scrollDis.setBounds(420,30,265,300);
    	 
    	 this.labAnswer.setBounds(420,330,150,20);
    	 this.scrollReply.setBounds(420,350,265,155);
    	 
    	 this.butsend.setBounds(480,520,100,30);
    	 this.btnStart.setBounds(170,80,100,25);
    	 this.btnStop.setBounds(300,80,100,25);
    	 this.btnSaveEnd.setBounds(80,520,100,30);
    	 this.btnEnd.setBounds(200,520,100,30);
    	
    	 
    	 this.labTopic.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labTitle.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labCE.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labDis.setFont(new Font("Garamond",Font.BOLD,20));
    	 this.labAnswer.setFont(new Font("Garamond",Font.BOLD,20));
    	 
    	 //Add txt Label
    	 this.add(labTopic);
    	 this.add(labTitle);
    	 this.add(labCE);
    	 this.add(labDis);
    	 this.add(labAnswer);
    	 
    	 //Add txt Field
    	 this.add(fTopic);
    	 this.add(fTitle);
    	 
    	 //Add Button
    	 this.add(butsend);
    	 this.add(btnStart);
    	 this.add(btnStop);
    	 this.add(btnSaveEnd);
    	 this.add(btnEnd);
	 
    	 btnStart.setEnabled(true);
    	 btnStop.setEnabled(false);
    	 
    	 //Add ScrollPane
    	 this.add(scrollCE);
    	 this.add(scrollDis);
    	 this.add(scrollReply);
    	 
    	 this.fTopic.setEditable(false);
    	 this.fTitle.addKeyListener(new KeyAdapter(){
    		 public void keyReleased(KeyEvent ke)
    		 {
    			 try
    			 {
    				 ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                                 out.writeObject("MentorText");
                                 out.writeObject("TitleText");
                                 out.writeObject(MentorSession.this.fTitle.getText());
    			 }
    			 catch(Exception ex)
    			 {
    				 JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE);
    			 }
    		 }
    	 });
        	 this.txtCE.addKeyListener(new KeyAdapter(){
    		 public void keyReleased(KeyEvent ke)
    		 {
    			 try
    			 {
    			    ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                            out.writeObject("MentorText");
                            out.writeObject("CodeEditorText");
                            out.writeObject(MentorSession.this.txtCE.getText());
    			 }
    			 catch(Exception ex)
    			 {
    				 JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE);
    			 }
                 }
    	 });
    	
       this.butsend.addActionListener(this);
       this.btnStart.addActionListener(this);
       this.btnStop.addActionListener(this);
       this.btnSaveEnd.addActionListener(this);
       this.btnEnd.addActionListener(this);
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
     				JOptionPane.showMessageDialog(MentorSession.this, "Audio Line Not Supported!!!","Audio Recording",JOptionPane.ERROR_MESSAGE);
     				return;
     			}
     			line=(TargetDataLine)AudioSystem.getLine(info);
     			line.open(format);
     			line.start();
     			
     			AudioInputStream ais=new AudioInputStream(line);
     			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("test.wav"));
    		 }
    		 catch(Exception ex)      
    		 {
    		    JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE); 
    		 }
    	 }
     }
     
    public void actionPerformed(ActionEvent ae)
    {
    	if(ae.getSource()==this.butsend) {
	    	 String answer=txtAnswer.getText();
	    	 txtAnswer.setText("");
	    	 try
	    	 {
		    	 ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
		    	 out.writeObject("DiscussionText");
	   	         out.writeObject(answer);
	    	 }
	    	 catch(Exception ex)
	    	 {
	    		 JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE);
	    	 }
    	}
    	
    	if(ae.getSource()==this.btnStart){
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
	   	    		 JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE);
	   	    	 }
    		}
    		catch(Exception ex)
    		{
    			 JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE);
    		}
    	 }
    	 if(ae.getSource()==this.btnEnd)
    	 {
    		 if(JOptionPane.showConfirmDialog(ClientRes.mwin.mSession,"Are you sure ?","EndSession",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
    		 {
    			 try
    			 {
    				 ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
    				 out.writeObject("EndSession");
    			 }
    			 catch(Exception ex)
    			 {
    				 JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE);
    			 }
    		 }
    	 }
    	 if(ae.getSource()==this.btnSaveEnd)
    	 {
    		 if(JOptionPane.showConfirmDialog(ClientRes.mwin.mSession,"Are you sure ?","EndSession",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
    		 {
    			 try
    			 {
    				 ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
    				 out.writeObject("SaveEndSession");
    				 out.writeObject(fTopic.getText());
    				 out.writeObject(fTitle.getText());
    				 out.writeObject(txtCE.getText());
    			 }
    			 catch(Exception ex)
    			 {
    				 JOptionPane.showMessageDialog(ClientRes.mwin.mSession,ex.toString(),"MentorSession",JOptionPane.ERROR_MESSAGE);
    			 }
    		 } 
    	 }
    	
    }
}
