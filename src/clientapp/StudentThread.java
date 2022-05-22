package clientapp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class StudentThread extends Thread
{
   boolean hasSession=false;
   File file;
   public StudentThread()
   {
	   this.start();
   }
   public void run()
   {
	   try
	   {
             while(true)
             {
	           ObjectInputStream in=new ObjectInputStream(ClientRes.client.getInputStream());
               String resp=in.readObject().toString();
               if(resp.equals("SessionInfo"))
               {
            	   Vector main=(Vector)in.readObject();
                   int size=main.size();
                   //ClientRes.mwin.mschedule.DATA=new String[size][5];
                   for(int i=0;i<100;i++)
                   {
                   	for(int j=0;j<6;j++)
                   	{
                   		ClientRes.swin.sSchedule.DATA[i][j]="";
                   	}
                   } 
                   for(int i=0;i<size;i++)
                   {
                       Vector sub=(Vector)main.elementAt(i);
                       for(int j=2;j<=5;j++)
                       ClientRes.swin.sSchedule.DATA[i][j-1]=sub.elementAt(j).toString();
                       ClientRes.swin.sSchedule.DATA[i][5]=sub.elementAt(0).toString();
                       ClientRes.swin.sSchedule.DATA[i][0]=sub.elementAt(6).toString();
                   }

                   ClientRes.swin.sSchedule.sTable.repaint();
                   
                   if(hasSession)
                   {         
                	   ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                	   out.writeObject("SessionDetails");
                   }
                  
   	           }
               if(resp.equals("ActiveSession"))
               {
            	   String state=in.readObject().toString();
                   if(state.equals("SessionYes"))
                   {
                       String name=in.readObject().toString();
                       String topic=in.readObject().toString();
                       String duration=in.readObject().toString();
                       String startAt=in.readObject().toString();
                    
                       //to activate new tab on student side
                       JOptionPane.showMessageDialog(ClientRes.swin,"New Session Avialbale","Session Information",JOptionPane.INFORMATION_MESSAGE);
                       
                       ClientRes.swin.sSchedule.sActive.setText("Currently Running Session.......");
                       ClientRes.swin.sSchedule.sName.setText("Mentor :"+name);
                       ClientRes.swin.sSchedule.sTopic.setText("Topic :"+topic);
                       ClientRes.swin.sSchedule.sDuration.setText("Duration :"+duration);
                       ClientRes.swin.sSchedule.sTime.setText("At Time :"+startAt);
                       
                       ClientRes.swin.sSession=new StudentSession();
       	    	       ClientRes.swin.jtb.addTab("Training Session",ClientRes.swin.sSession);
       	    		   
       	    	       ClientRes.swin.sSession.fTopic.setText(topic);
       	    	       hasSession=true;
                   } 
            	   ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                   out.writeObject("DownloadInfo");
               }
               if(resp.equals("SessionStart"))
               {   
            	   String name=in.readObject().toString();
                   String topic=in.readObject().toString();
                   String duration=in.readObject().toString();
                   String startAt=in.readObject().toString();
            	   JOptionPane.showMessageDialog(ClientRes.swin,"New Session Avialbale","Session Information",JOptionPane.INFORMATION_MESSAGE); 
            	   
                   ClientRes.swin.sSchedule.sActive.setText("Currently Running Session.......");
                   ClientRes.swin.sSchedule.sName.setText("Mentor :"+name);
                   ClientRes.swin.sSchedule.sTopic.setText("Topic :"+topic);
                   ClientRes.swin.sSchedule.sDuration.setText("Duration :"+duration);
                   ClientRes.swin.sSchedule.sTime.setText("At Time :"+startAt);
                   
            	   ClientRes.swin.sSession=new StudentSession();
            	   ClientRes.swin.jtb.addTab("Training Session",ClientRes.swin.sSession);  
            	   
            	   ClientRes.swin.sSession.fTopic.setText(topic);
               }
	       if(resp.equals("MentorText"))
	       {
	            String textType=in.readObject().toString();
                    String text=in.readObject().toString();
                   if(textType.equals("TitleText"))
                   {
                       ClientRes.swin.sSession.fTitle.setText(text);   
                   }
                   if(textType.equals("CodeEditorText"))
                   {
                       ClientRes.swin.sSession.txtCE.setText(text);
                       
                   }
	       }
	       if(resp.equals("DiscussionText"))
	       {
		    String text=in.readObject().toString();
		    String uName=in.readObject().toString();
		    ClientRes.swin.sSession.DATA[ClientRes.swin.sSession.index][0]=uName;
	 	    ClientRes.swin.sSession.DATA[ClientRes.swin.sSession.index][1]=text;
	 	    ClientRes.swin.sSession.index++;
	 	    ClientRes.swin.sSession.tblDis.repaint();
	       }	   
	       if(resp.equals("DiscussionAudio"))
	       {
		byte barr[]=(byte[])in.readObject();
		String uName=in.readObject().toString();
		file=new File("Audio1.wav");
		FileOutputStream fout=new FileOutputStream(file);
		fout.write(barr);
		fout.close();
		JButton jbtn=new JButton("Audio");
		jbtn.setSize(30, 25);
		    jbtn.addActionListener(new ActionListener()
		    {		
			public void actionPerformed(ActionEvent arg0)
		        {
			    class SoundPlayer extends Thread implements LineListener
			    {
				public void run()
			        {
				    try
				    {
				        DataLine.Info info;
					AudioInputStream ais=AudioSystem.getAudioInputStream(new File("AudioDis/Audio" + ClientRes.swin.sSession.index + ".wav"));
					AudioFormat format=ais.getFormat();
					info=(DataLine.Info)new DataLine.Info(Clip.class, format);
					if(!AudioSystem.isLineSupported(info))
                                        {
                                            JOptionPane.showMessageDialog(ClientRes.swin,"Audio Line Not  Supported...!!!","Audio Recording",JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
				        Clip clip=(Clip)AudioSystem.getLine(info);
                                        clip.addLineListener(this);
                                        clip.open();
                                        clip.start();
				    }
				    catch(Exception ex)
				    {
				      JOptionPane.showMessageDialog(ClientRes.swin,"Cannot Play Audio...!!!" + ex,"Audio Recording Start",JOptionPane.ERROR_MESSAGE);
                                      return;
				    }
								
				    new SoundPlayer().start();
				}
				public void update(LineEvent le)
				{
				 if(le.getType()==LineEvent.Type.START)
				 {
				   JOptionPane.showMessageDialog(ClientRes.swin,"Audio Start...!!!","Audio Recording Start",JOptionPane.ERROR_MESSAGE);
                                   return;
				 }
			         if(le.getType()==LineEvent.Type.STOP)
				 {
				    JOptionPane.showMessageDialog(ClientRes.swin,"Audio Stop...!!!","Audio Recording Start",JOptionPane.ERROR_MESSAGE);
                                    return;	
				 }
								
				}
							
	         	    }
						
			}
	 	    });
				   
		 ClientRes.swin.sSession.DATA[ClientRes.swin.sSession.index][0]=uName;
	 	 ClientRes.swin.sSession.DATA[ClientRes.swin.sSession.index][1]=jbtn;
	 	 ClientRes.swin.sSession.index++;
	 	 ClientRes.swin.sSession.tblDis.repaint();
		}
		if(resp.equals("SessionDetails"))
		{		   
		    String fTitle=in.readObject().toString();
		    String txtCE=in.readObject().toString();
		    ClientRes.swin.sSession.fTitle.setText(fTitle);
		    ClientRes.swin.sSession.txtCE.setText(txtCE);	  
	        }
	        if(resp.equals("EndSession"))
		{
                    //Notify to the student
		    JOptionPane.showMessageDialog(ClientRes.swin.sSession,"Session is closed\nThanku You","EndSession",JOptionPane.INFORMATION_MESSAGE);
		    ClientRes.swin.jtb.remove(ClientRes.swin.sSession);
		    ClientRes.swin.sSchedule.sActive.setText("");
                    ClientRes.swin.sSchedule.sName.setText("");
                    ClientRes.swin.sSchedule.sTopic.setText("");
                    ClientRes.swin.sSchedule.sDuration.setText("");
                    ClientRes.swin.sSchedule.sTime.setText("");
                    
                    //If mentor save any sessiom then update the table
                    ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                    out.writeObject("DownloadInfo");
	        }
	        if(resp.equals("DownloadInfo"))
	        {
		    Vector main=(Vector)in.readObject();
                    int size=main.size();
                    //ClientRes.mwin.mschedule.DATA=new String[size][5];
                    for(int i=0;i<100;i++)
                    {
                       for(int j=0;j<5;j++)
                       {
                           ClientRes.swin.download.DATA[i][j]="";
                       }
                    }        
                    for(int i=0;i<size;i++)
                    {
                     Vector sub=(Vector)main.elementAt(i);
                     for(int j=1;j<=3;j++)
                     ClientRes.swin.download.DATA[i][j]=sub.elementAt(j).toString();
                     ClientRes.swin.download.DATA[i][4]=sub.elementAt(0).toString();
                     ClientRes.swin.download.DATA[i][0]=sub.elementAt(4).toString();
                    }
                   
                        ClientRes.swin.download.dTable.repaint(); //Update the table
                        ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                        out.writeObject("SessionInfo");
				   
	        }
		if(resp.equals("RemoteCompile"))
		{
		   String result=in.readObject().toString();
		   ClientRes.swin.rsCompile.txtResult.setText(result);
	        }
	        if(resp.equals("DownloadSession"))
		{  
		    Vector audio=(Vector)in.readObject();
		    String topic=in.readObject().toString();  
	            JFrame parentFrame = new JFrame();
				   
	            JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Specify a Directory to save Audio File");   
					    
	            int userSelection = fileChooser.showSaveDialog(parentFrame);
					    
		    File fileToSave=null;
		    if (userSelection == JFileChooser.APPROVE_OPTION)
		    {
			fileToSave = fileChooser.getSelectedFile();
			//System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		    }
		    file = new File(fileToSave.getAbsolutePath());
						 
		    // if file doesnt exists, then create it
		    if (!file.exists()) 
		    {
			file.mkdirs();
		    }	
		    for(int i=0;i<audio.size();i++)
		    {
			byte barr[]=(byte[])audio.elementAt(i);
			File file2=new File(fileToSave.getAbsolutePath()+"/"+topic+(i+1)+".wav");
			FileOutputStream fout=new FileOutputStream(file2);
			fout.write(barr);
			fout.close();	    	 
		    }
		        ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
	                out.writeObject("DownloadWEB");	
	                out.writeObject(fileToSave.getAbsolutePath());
			     
	        }
		if(resp.equals("DownloadWEB"))
		{
		    String html=in.readObject().toString();
				   
		    JFrame parentFrame = new JFrame(); 
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Specify a html File to save");   
					    
		    int userSelection = fileChooser.showSaveDialog(parentFrame);
					    
		    File fileToSave=null;
		    if (userSelection == JFileChooser.APPROVE_OPTION)
		    {
			fileToSave = fileChooser.getSelectedFile();
					      
		    }
			File file = new File(fileToSave.getAbsolutePath()); 
			// if file doesnt exists, then create it
			if (!file.exists()) 
			{
			    file.createNewFile();
			}
			 
		        FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(html);
			bw.close();
		        //System.out.println(html);			
		    }
		   }
	   }
	   catch(Exception ex)
	   {
		   ex.printStackTrace();
		   JOptionPane.showMessageDialog(ClientRes.swin,ex.toString(),"StudentSession",JOptionPane.ERROR_MESSAGE);
		   
	   }
	   
	   
   }
}
