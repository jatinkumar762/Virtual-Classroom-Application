/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import java.net.*;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;


public class MentorThread extends Thread
{
    File file;
    public MentorThread()
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
                if(resp.equals("SessionValidate"))
                {                   
                      String state=in.readObject().toString();
                      if(state.equals("Added"))
                      {
                         JOptionPane.showMessageDialog(ClientRes.mwin,"Session Added Successfully.","Mentor Schedule",JOptionPane.INFORMATION_MESSAGE);
                         //empty fields
                         ClientRes.mwin.mschedule.txttopic.setText("");
                         ClientRes.mwin.mschedule.txtdate.setText("");
                         ClientRes.mwin.mschedule.txttime.setText("");
                         ClientRes.mwin.mschedule.txtduration.setText("");
                         ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
           	         out.writeObject("SessionInfo");
                      }
                      else
                      {
                         JOptionPane.showMessageDialog(ClientRes.mwin,"You already have a session at that time!!!","Mentor Schedule",JOptionPane.ERROR_MESSAGE);
                      }
                 }
                if(resp.equals("SessionInfo"))
                {
                    Vector main=(Vector)in.readObject();
                    int size=main.size();
                    //ClientRes.mwin.mschedule.DATA=new String[size][5];
                    for(int i=0;i<100;i++)
                    {
                    	for(int j=0;j<5;j++)
                    	{
                    		ClientRes.mwin.mschedule.DATA[i][j]="";
                    	}
                    }
                    for(int i=0;i<size;i++)
                    {
                        Vector sub=(Vector)main.elementAt(i);
                        for(int j=2;j<=5;j++)
                        ClientRes.mwin.mschedule.DATA[i][j-2]=sub.elementAt(j).toString();
                        ClientRes.mwin.mschedule.DATA[i][4]=sub.elementAt(0).toString();
                    }

                    ClientRes.mwin.mschedule.tblSession.repaint();
                }
                if(resp.equals("Remove"))
                {
                	JOptionPane.showMessageDialog(ClientRes.mwin,"Successfully Remove...","Remove",JOptionPane.INFORMATION_MESSAGE);
                	 ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
       			 out.writeObject("SessionInfo");
                }
                if(resp.equals("SucessfullyUpdate"))
                {
                	JOptionPane.showMessageDialog(ClientRes.mwin,"Successfully Update...","Update",JOptionPane.INFORMATION_MESSAGE);
                	ClientRes.mwin.mschedule.btnadd.setText("Add");
                	 //empty fields
                    ClientRes.mwin.mschedule.txttopic.setText("");
                    ClientRes.mwin.mschedule.txtdate.setText("");
                    ClientRes.mwin.mschedule.txttime.setText("");
                    ClientRes.mwin.mschedule.txtduration.setText("");
                    ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
      	            out.writeObject("SessionInfo");
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

                         ClientRes.mwin.mschedule.mActive.setText("Current Session");
                         ClientRes.mwin.mschedule.mName.setText("Mentor :"+name);
                         ClientRes.mwin.mschedule.mTopic.setText("Topic :"+topic);
                         ClientRes.mwin.mschedule.mDuration.setText("Duration :"+duration);
                         ClientRes.mwin.mschedule.mTime.setText(startAt);
                         
                         //Deactivate Activate Button
                         ClientRes.mwin.mschedule.btnActivate.setEnabled(false);
                         //to activate new tab on student side
                     }
                     ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                     out.writeObject("SessionInfo");
                	
                }
                if(resp.equals("SessionStart"))
                {
                    String name=in.readObject().toString();
                    String topic=in.readObject().toString();
                    String duration=in.readObject().toString();
                    String startAt=in.readObject().toString();
                    
                    ClientRes.mwin.mschedule.mActive.setText("Current Session");
                    ClientRes.mwin.mschedule.mName.setText("Mentor :"+name);
                    ClientRes.mwin.mschedule.mTopic.setText("Topic :"+topic);
                    ClientRes.mwin.mschedule.mDuration.setText("Duration :"+duration);
                    ClientRes.mwin.mschedule.mTime.setText(startAt);
                    ClientRes.mwin.mschedule.btnActivate.setEnabled(false);
                    ClientRes.mwin.mSession=new MentorSession();
    	    	    ClientRes.mwin.jtb.addTab("Training Session",ClientRes.mwin.mSession);
                    ClientRes.mwin.mSession.fTopic.setText(topic);
                }
                if(resp.equals("DiscussionText"))
 		 {
 		    String text=in.readObject().toString();
 		    String uName=in.readObject().toString();
 		    ClientRes.mwin.mSession.DATA[ClientRes.mwin.mSession.index][0]=uName;
 		    ClientRes.mwin.mSession.DATA[ClientRes.mwin.mSession.index][1]=text;
 		    ClientRes.mwin.mSession.index++;
 		    ClientRes.mwin.mSession.tblDis.repaint();
 		}
                if(resp.equals("SessionDetails"))
                {
                	//Object DATA[][]=new Object[500][2];
                	//for(int i=0;i<500;i++)
                		//for(int j=0;j<2;j++)
                			//DATA[i][j]="";
                	ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                	out.writeObject("MSessDetails");
                	out.writeObject(ClientRes.mwin.mSession.fTitle.getText());
                	out.writeObject(ClientRes.mwin.mSession.txtCE.getText());
                	/*for(int i=0;i<500;i++)
                		for(int j=0;j<2;j++)
                		{
                			if(!ClientRes.mwin.mSession.DATA[i][j].equals(""))
                			{
                				DATA[i][j]=ClientRes.mwin.mSession.DATA[i][j];
                			 }
                			else
                				break;
                		}
                	out.writeObject(DATA);*/
                		
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
 		    jbtn.addActionListener(new ActionListener(){   
 			@Override
 			public void actionPerformed(ActionEvent arg0)
                        {
 			// TODO Auto-generated method stub
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
                                     JOptionPane.showMessageDialog(ClientRes.mwin,"Audio Line Not  Supported...!!!","Audio Recording",JOptionPane.ERROR_MESSAGE);
                                     return;
                                 }
				  Clip clip=(Clip)AudioSystem.getLine(info);
                                  clip.addLineListener(this);
                                  clip.open();
                                  clip.start();
			        }
				catch(Exception ex)
				{
				    JOptionPane.showMessageDialog(ClientRes.mwin,"Cannot Play Audio...!!!" + ex,"Audio Recording Start",JOptionPane.ERROR_MESSAGE);
                                    return;
				}
								
				new SoundPlayer().start();
			     }
			     public void update(LineEvent le)
			     {
				if(le.getType()==LineEvent.Type.START)
				{
				    JOptionPane.showMessageDialog(ClientRes.mwin,"Audio Start...!!!","Audio Recording Start",JOptionPane.ERROR_MESSAGE);
                                    return;
				}
				if(le.getType()==LineEvent.Type.STOP)
			        {
				    JOptionPane.showMessageDialog(ClientRes.mwin,"Audio Stop...!!!","Audio Recording Start",JOptionPane.ERROR_MESSAGE);
				    return;	
				}		
			     }				
		        }
 		     }
 	 	     });		   
 				   ClientRes.mwin.mSession.DATA[ClientRes.mwin.mSession.index][0]=uName;
 	 			   ClientRes.mwin.mSession.DATA[ClientRes.mwin.mSession.index][1]=jbtn;
 	 			   ClientRes.mwin.mSession.index++;
 	 			   ClientRes.mwin.mSession.tblDis.repaint();
 			   }
                           if(resp.equals("EndSession"))
 			   {
                                //Label remove from Mentor Schedule
                                ClientRes.mwin.mschedule.mActive.setText("");
                                ClientRes.mwin.mschedule.mName.setText("");
                                ClientRes.mwin.mschedule.mTopic.setText("");
                                ClientRes.mwin.mschedule.mDuration.setText("");
                                ClientRes.mwin.mschedule.mTime.setText("");
                                //Activae Button Enable  
                                ClientRes.mwin.mschedule.btnActivate.setEnabled(true);
                                ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
        			out.writeObject("SessionInfo");
                                //Remove tab
 				ClientRes.mwin.jtb.remove(ClientRes.mwin.mSession);
 			   }
                           if(resp.equals("RemoteCompile"))
 			   {
 				String result=in.readObject().toString();
 				ClientRes.mwin.rmCompile.txtResult.setText(result);			   
 			   }
            }
        }
        catch(Exception ex)
        {
            
        }
    }
}

 