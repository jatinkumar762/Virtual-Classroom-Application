/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author define
 */
public class MentorWin extends JFrame 
{
    
    JTabbedPane jtb;
    MentorSchedule mschedule;
    MentorSession mSession;
    RemoteCompileM rmCompile;
    
    public MentorWin()
    {
        this.setLayout(null);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter(){
        	public void windowClosing(WindowEvent we)
        	{
        		if(JOptionPane.showConfirmDialog(MentorWin.this, "Do you want to logout??","Logout",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
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
        this.mschedule=new MentorSchedule();
        jtb.addTab("My Schedule",this.mschedule);
        this.add(jtb);
        jtb.setBounds(0,0,690,590);
    }
    
}
