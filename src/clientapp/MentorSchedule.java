package clientapp;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MentorSchedule extends JPanel implements ActionListener
{
  JTextField txttopic,txtdate,txttime,txtduration;
  JLabel topiclbl,datelbl,timelbl,durationlbl,heading,lblSession;
  JButton btnadd,btnEdit,btnRemove,btnActivate,btnCompile;
  
  JTable tblSession;
  Object HEAD[]={"Date","Time","Duration","Topic"};
  Object DATA[][];
  JScrollPane jspSession;
  int row;
  
  JLabel mTopic,mDuration,mTime,mName,mActive;
  
  public MentorSchedule()
  {
   this.setLayout(null);
  
   DATA=new String[100][5];
   
   heading=new JLabel("Schedule Details");
   topiclbl=new JLabel("Enter Topic : ");
   datelbl=new JLabel("Enter Date : ");
   timelbl=new JLabel("Enter Time : ");
   durationlbl=new JLabel("Enter Duration : ");
   lblSession=new JLabel("Your Scheduled Sessions....");

   txttopic=new JTextField();
   txtdate=new JTextField();
   txttime=new JTextField();
   txtduration=new JTextField();
   
   btnadd=new JButton("Add");
   this.add(btnadd);
   btnadd.setBounds(140,210,100,30);
   
     btnEdit=new JButton("Edit");
     btnRemove=new JButton("Remove");
     btnActivate=new JButton("Activate");
     this.btnCompile=new JButton("OnlineCompiler");
     
     this.add(btnCompile);
     this.add(btnEdit);
     this.add(btnRemove);
     this.add(btnActivate);
   
     btnEdit.setBounds(560,270,100,25);
     btnRemove.setBounds(560,305,100,25);
     btnActivate.setBounds(560,340,100,25);
     btnCompile.setBounds(550,10,130,30);
   
     btnCompile.setForeground(Color.BLUE);
     
   this.add(topiclbl);
   this.add(datelbl);
   this.add(timelbl);
   this.add(durationlbl);
   this.add(heading);
   this.add(lblSession);
   
   this.add(txtduration);
   this.add(txttopic);
   
   this.add(txtdate);
   this.add(txttime);
   
   heading.setFont(new Font("Serif",Font.BOLD+Font.ITALIC,20));
   heading.setBounds(160,20,250,30);
   
   topiclbl.setFont(new Font("Garamond",Font.BOLD,20));
   datelbl.setFont(new Font("Garamond",Font.BOLD,20));
   timelbl.setFont(new Font("Garamond",Font.BOLD,20));
   durationlbl.setFont(new Font("Garamond",Font.BOLD,20));
   
   topiclbl.setBounds(30, 65, 150, 25);
   txttopic.setBounds(180,65,200,25);
   datelbl.setBounds(30,105,150,25);
   txtdate.setBounds(180,105,200,25);
   timelbl.setBounds(30,140,150,25);
   txttime.setBounds(180,140,200,25);
   durationlbl.setBounds(30,175,150,25);
   txtduration.setBounds(180,175,200,25);
   
   lblSession.setBounds(30,245,200,20);
   tblSession=new JTable(this.DATA,this.HEAD);
   JScrollPane jspSession=new JScrollPane(tblSession,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
   this.add(jspSession);
   jspSession.setBounds(30,265,520,250);
   
   btnCompile.addActionListener(this);
   btnadd.addActionListener(this);
   btnEdit.addActionListener(this);
   btnRemove.addActionListener(this);
   btnActivate.addActionListener(this);
   
   mActive=new JLabel();
   mTopic=new JLabel();
   mDuration=new JLabel();
   mTime=new JLabel();
   mName=new JLabel();
   
   this.add(mActive);
   this.add(mTopic);
   this.add(mDuration);
   this.add(mTime);
   this.add(mName);
   
   mActive.setBounds(400,20,250,30);
   mTopic.setBounds(400,65,250,25);
   mName.setBounds(400,105,200,25);
   mTime.setBounds(400,140,300,25);
   mDuration.setBounds(400,175,200,25);
   
   mActive.setFont(new Font("Serif", Font.BOLD+Font.ITALIC, 20));
   mTopic.setFont(new Font("Serif", Font.PLAIN, 20));
   mName.setFont(new Font("Serif", Font.PLAIN, 20));
   mTime.setFont(new Font("Serif", Font.PLAIN, 20));
   mDuration.setFont(new Font("Serif", Font.PLAIN, 20));
   
   mActive.setForeground(Color.RED);
   mTopic.setForeground(Color.BLUE);
   mName.setForeground(Color.BLUE);
   mTime.setForeground(Color.BLUE);
   mDuration.setForeground(Color.BLUE);
   
  }
  
  public void actionPerformed(ActionEvent ae)
  {
	 try
	 {
		  if(ae.getSource().equals(btnCompile))
		  {
			  ClientRes.mwin.rmCompile=new RemoteCompileM(); 
	    	  ClientRes.mwin.jtb.addTab("OnlineCompile",ClientRes.mwin.rmCompile); 
	    	  return;
		  }
		 else
		   if(ae.getSource().equals(btnEdit))
		   {
			   int row=this.tblSession.getSelectedRow();
			   if(row<0)
			   {
				   JOptionPane.showMessageDialog(this,"First Select any row!!!","Remove",JOptionPane.WARNING_MESSAGE);
			   }
			   else
			   {
				   txtdate.setText((String)DATA[row][0]);
				   txttime.setText((String)DATA[row][1]);
				   txtduration.setText((String)DATA[row][2]);
				   txttopic.setText((String)DATA[row][3]);
				   btnadd.setText("Update");
				   this.row=row;
			   }
			   return;
		   }
	     else
	    	if(ae.getSource().equals(btnRemove))
	    	{
	    		int row=this.tblSession.getSelectedRow();
	    		if(row<0)
	    		{
	    			JOptionPane.showMessageDialog(this,"First Select any row!!!","Remove",JOptionPane.WARNING_MESSAGE);
	    		}
	    		else
	    		{
	    			//int LoginId=Integer.parseInt((String) DATA[row][4]);
	    			if(JOptionPane.showConfirmDialog(this,"Are you sure ?","Session Activate",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
	    			{
	    			ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
	    			out.writeObject("RemoveSession");
	    			out.writeObject((String) DATA[row][4]);
	    			}
	    		}
	    		
	    		return;
	    	}
	    else
	    	if(ae.getSource().equals(btnActivate))
	    	{
	    		int row=this.tblSession.getSelectedRow();
	    		if(row<0)
	    		{
	    			JOptionPane.showMessageDialog(this,"No Session Selected For Activation!!!","Remove",JOptionPane.WARNING_MESSAGE);
                                return;
	    		}
	    		if(JOptionPane.showConfirmDialog(this,"Are you sure ?","Session Activate",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
	    		{
	    		ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
                        out.writeObject("ActivateSession");
                        out.writeObject((String) DATA[row][4]);  		
	    		
	    		}
	    		return;
	    	}
				 
		 if(txttopic.getText().equals(""))
		 {
		  JOptionPane.showMessageDialog(this,"Topic Field is Empty!!!","Error",JOptionPane.ERROR_MESSAGE);
		  return;
		 }
		 if(txtdate.getText().equals(""))
		 {
		  JOptionPane.showMessageDialog(this,"Date Field is Empty!!!","Error",JOptionPane.ERROR_MESSAGE);
		  return;
		 }
		 if(txttime.getText().equals(""))
		 {
		  JOptionPane.showMessageDialog(this,"Time Field is Empty!!!","Error",JOptionPane.ERROR_MESSAGE);
		  return;
		 }
		 if(txtduration.getText().equals(""))
		 {
		  JOptionPane.showMessageDialog(this,"Duration Field is Empty!!!","Error",JOptionPane.ERROR_MESSAGE);
		  return;
		 }
	  
	  
		  if(ae.getSource().equals(btnadd))
		  {	  	
			ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
			    if(btnadd.getText().equals("Add"))
			    {
		  			out.writeObject("mentorschedule");
		  			out.writeObject(txttopic.getText().trim());
		  			out.writeObject(txtdate.getText().trim());
		  			out.writeObject(txttime.getText().trim());
		  			out.writeObject(txtduration.getText().trim());
			    }
			    else
			    {
			    	  out.writeObject("UpdateM");
				  out.writeObject(txttopic.getText().trim());
				  out.writeObject(txtdate.getText().trim());
			          out.writeObject(txttime.getText().trim());
				  out.writeObject(txtduration.getText().trim());  
				  out.writeObject((String)DATA[row][4]); 
			    }
		  }
		  
		  
			 
	  }
	  catch(Exception ex)
	  {
		  ex.printStackTrace();
		  JOptionPane.showMessageDialog(this,ex.toString() ,"MentorSchedule",JOptionPane.ERROR_MESSAGE );
	  }  
  }
 
}