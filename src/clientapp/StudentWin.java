package clientapp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class StudentWin extends JFrame
{
   JTabbedPane jtb;
   StudentSchedule sSchedule;
   StudentSession sSession;
   RemoteCompileS rsCompile;
   Download download;
   public StudentWin()
   {
	   this.setLayout(null);
	   this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	   this.addWindowListener(new WindowAdapter(){
       	public void windowClosing(WindowEvent we)
       	{
       		if(JOptionPane.showConfirmDialog(StudentWin.this, "Do you want to logout??","Logout",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
       		{
       			try
       			{
       			ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
       			out.writeObject("Logout");
       			System.exit(1);      			
       			}
       			catch(Exception ex)
       			{
       			 
       					
       			}    			
       		}
       	}
      });
	   
	  jtb=new JTabbedPane();
	  this.sSchedule=new StudentSchedule();
	  this.download=new Download();
      jtb.addTab("Available Schedule",this.sSchedule);
      jtb.addTab("Session Download",this.download);
      this.add(jtb);
      jtb.setBounds(0,0,690,590);
  }
}
