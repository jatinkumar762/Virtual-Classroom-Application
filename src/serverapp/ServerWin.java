package serverapp;
import java.lang.*;
import java.sql.ResultSet;
import java.awt.*;

import javax.swing.*;


public class ServerWin extends JFrame
{
   public static CurrentStatus current;
   RegisterMntr mentor;
   RegisterStdnt student;

   JTabbedPane jtb;

   public ServerWin()
   {
      this.setLayout(null);

      this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
      current=new CurrentStatus();
      mentor=new RegisterMntr();
      student=new RegisterStdnt();

      jtb=new JTabbedPane();
      jtb.addTab("Current Status",current);
      jtb.addTab("Mentor Reg.",mentor);
      jtb.addTab("Student Reg.",student);

      this.add(jtb);
      jtb.setBounds(2,2,590,567);
      
      
      try
      {
    	  String query="select LoginId,UName,Desg,Expertise,AddedOn from mentormaster";
    	  ResultSet rs=ConnectionFactory.getInstance().getData(query);
          while(rs.next())
          {
        	  for(int j=0;j<5;j++)
        	  {
                       mentor.DATA[mentor.index][j]=rs.getString(j+1);
        	  }
        	  mentor.index++;
          }
          query="select LoginId,UName,Branch,SYear,AddedOn from studentmaster";
          rs=ConnectionFactory.getInstance().getData(query);
          while(rs.next())
          {
        	  for(int j=0;j<5;j++)
        	  {
        		  student.DATA[student.index][j]=rs.getString(j+1);
        	  }
        	  student.index++;
          }
          
      }
      catch(Exception ex)
      {
    	  ex.printStackTrace();
    	  JOptionPane.showMessageDialog(this, ex.toString(), "Registration", JOptionPane.WARNING_MESSAGE);
      }
   }
}