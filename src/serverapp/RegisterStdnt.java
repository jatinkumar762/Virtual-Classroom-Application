package serverapp;
import java.lang.*;
import java.sql.SQLException;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

public class RegisterStdnt extends JPanel implements ActionListener
{
    JLabel heading,idlbl,pwdlbl,pwdlbl2,namelbl,brnchlbl,yrlbl,tblLabel;
    JTextField logid,name,brnch,year;
    JPasswordField pwd,pwd2;
    JButton regstdnt,delstdnt;
    JTable stuTable;
    JScrollPane jspSession;
    
    Object HEAD[]={"LoginID","Name","Branch","Year","AddedOn"};
    Object DATA[][];
    int index;
    
    public RegisterStdnt()
    {
        this.setLayout(null);
       
        DATA=new Object[100][5];
        stuTable=new JTable(DATA,HEAD);
        index=0;
        
        heading=new JLabel("Student Registration");
        heading.setFont(new Font("Sans Serif",Font.ITALIC,24));
        idlbl=new JLabel("Login ID : ");
        pwdlbl=new JLabel("Password : ");
        pwdlbl2=new JLabel("Confirm Password : ");
        namelbl=new JLabel("Name : ");
        brnchlbl=new JLabel("Branch : ");
        yrlbl=new JLabel("Year : ");
        tblLabel=new JLabel("Registered Students are.....");
        
        logid=new JTextField();
        pwd=new JPasswordField();
        pwd2=new JPasswordField();
        name=new JTextField();
        brnch=new JTextField();
        year=new JTextField();
        regstdnt=new JButton("Register");
        regstdnt.addActionListener(this);
        delstdnt=new JButton("Delete");
        delstdnt.addActionListener(this);

        this.add(heading);
        this.add(idlbl);
        this.add(pwdlbl);
        this.add(pwdlbl2);
        this.add(namelbl);
        this.add(brnchlbl);
        this.add(yrlbl);
        this.add(tblLabel);

        this.add(logid);
        this.add(pwd);
        this.add(pwd2);
        this.add(name);
        this.add(brnch);
        this.add(year);
        this.add(regstdnt);
        this.add(delstdnt);
        
        this.add(stuTable);
        jspSession=new JScrollPane(stuTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
   	    this.add(jspSession);
   	    jspSession.setBounds(40,330,500,200);
   	    tblLabel.setBounds(40,300,300,25);
   	    tblLabel.setFont(new Font("Garamond",Font.BOLD,20));
        
        heading.setBounds(190,15,250,30);
        idlbl.setBounds(150,60,100,25);
        logid.setBounds(280,60,150,25);
        pwdlbl.setBounds(150,100,100,25);
        pwd.setBounds(280,100,150,25);
        pwdlbl2.setBounds(150,140,120,25);
        pwd2.setBounds(280,140,150,25);
        namelbl.setBounds(150,180,100,25);
        name.setBounds(280,180,150,25);
        brnchlbl.setBounds(150,220,100,25);
        brnch.setBounds(280,220,150,25);
        yrlbl.setBounds(150,260,120,25);
        year.setBounds(280,260,150,25);
        regstdnt.setBounds(470,20,90,25);
        delstdnt.setBounds(470, 45, 90,25);
    }
    public void actionPerformed(ActionEvent ae)
    {
        //regstdnt.setText("Clicked");
    	//validate data
    	if(ae.getSource()==delstdnt)
    	{
    		
    		
    		
    		return;
    	}
        if(this.logid.getText().equals(""))
        {
             JOptionPane.showMessageDialog(this,"Empty \"Login Id\" Field!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        if( new String(this.pwd.getPassword()).equals("") )
        {
             JOptionPane.showMessageDialog(this,"Empty \"Password\" Field!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        if(!( new String(this.pwd2.getPassword()).trim().equals( new String(this.pwd.getPassword()).trim())))
        {
             JOptionPane.showMessageDialog(this,"Password Not Confirmed!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        if(this.name.getText().equals(""))
        {
             JOptionPane.showMessageDialog(this,"Empty \"Name\" Field!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        if(this.brnch.getText().equals(""))
        {
             JOptionPane.showMessageDialog(this,"Empty \"Branch\" Field!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        if(this.year.getText().equals(""))
        {
             JOptionPane.showMessageDialog(this,"Empty \"Year\" Field!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        //Database Entry
        String query="select * from studentmaster where LoginId='"+ logid.getText().trim()+"'";
        try
        {
        	java.sql.ResultSet rs=ConnectionFactory.getInstance().getData(query);
        	if(rs.next())
        	{
        		  JOptionPane.showMessageDialog(this,"Login Id Already Exists!!!","Registration",JOptionPane.ERROR_MESSAGE);
                  return;
        	}
        }
    	catch(Exception ex)
    	{
    		System.out.println("Problem in query : " + query + "\nReason: " + ex);
    	}
        query="insert into studentmaster set "+
    			     "LoginId='" + this.logid.getText().trim() + "'," +
                     "Password='" + new String(this.pwd.getPassword()).trim() + "'," +
                     "UName='" + this.name.getText().trim() + "'," +
                     "Branch='" + this.brnch.getText().trim() + "'," +
                     "SYear='" + this.year.getText().trim() + "'," +
                     "AddedOn='" + new java.util.Date().toString() + "'";
        
        try
        {
             ConnectionFactory.getInstance().setData(query);
             JOptionPane.showMessageDialog(this,"Successfully Registered","Registration",JOptionPane.INFORMATION_MESSAGE);
             
             query="select LoginId,UName,Branch,SYear,AddedOn from studentmaster";
             java.sql.ResultSet rs=ConnectionFactory.getInstance().getData(query);
             for(int i=0;i<100;i++)
                 for(int j=0;j<5;j++)
                     DATA[i][j]="";
             
                index=0;
                while(rs.next())
                {
        	  for(int j=0;j<5;j++)
        	  {
        		 DATA[index][j]=rs.getString(j+1);
        	  }
        	  index++;
                }
                stuTable.repaint();
                
             //Empty Entry fields...
             this.logid.setText("");
             this.pwd.setText("");
             this.pwd2.setText("");
             this.name.setText("");
             this.brnch.setText("");
             this.year.setText("");
        }
        catch(Exception ex)
        {
            System.out.println("Problem in query : " + query + "\nReason: " + ex);
        }	
    }
}