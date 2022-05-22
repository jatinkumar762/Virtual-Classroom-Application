package serverapp;
import java.lang.*;
import java.sql.SQLException;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class RegisterMntr extends JPanel implements ActionListener
{
    JLabel heading,idlbl,pwdlbl,pwdlbl2,namelbl,dgnlbl,foelbl,tblLabel;
    JTextField logid,name,dgn,foe;
    JPasswordField pwd,pwd2;
    JButton regmntr,delmntr;
    JTable mntrTable;
    JScrollPane jspSession;
    
    Object HEAD[]={"LoginID","Name","Designation","Field Of Expertise","AddedOn"};
    Object DATA[][];
    int index;
    
    public RegisterMntr()
    {
        this.setLayout(null);
        

        DATA=new Object[100][5];
        mntrTable=new JTable(DATA,HEAD);
        index=0;
        
        heading=new JLabel("Mentor Registration");
        heading.setFont(new Font("Sans Serif",Font.ITALIC,24));
        idlbl=new JLabel("Login ID : ");
        pwdlbl=new JLabel("Password : ");
        pwdlbl2=new JLabel("Confirm Password : ");
        namelbl=new JLabel("Name : ");
        dgnlbl=new JLabel("Designation : ");
        foelbl=new JLabel("Field of Expertise : ");
        tblLabel=new JLabel("Registered Mentors are.....");

        logid=new JTextField();
        pwd=new JPasswordField();
        pwd2=new JPasswordField();
        name=new JTextField();
        dgn=new JTextField();
        foe=new JTextField();
        
        delmntr=new JButton("Delete");
        delmntr.addActionListener(this);
        regmntr=new JButton("Register");
        regmntr.addActionListener(this);

        this.add(heading);
        this.add(idlbl);
        this.add(pwdlbl);
        this.add(pwdlbl2);
        this.add(namelbl);
        this.add(dgnlbl);
        this.add(foelbl);
        this.add(tblLabel);

        this.add(logid);
        this.add(pwd);
        this.add(pwd2);
        this.add(name);
        this.add(dgn);
        this.add(foe);
        this.add(regmntr);
        this.add(delmntr);

        this.add(mntrTable);
        jspSession=new JScrollPane(mntrTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
        dgnlbl.setBounds(150,220,100,25);
        dgn.setBounds(280,220,150,25);
        foelbl.setBounds(150,260,120,25);
        foe.setBounds(280,260,150,25);
        regmntr.setBounds(470,20,90,25);
        delmntr.setBounds(470, 45, 90,25);

    }
    public void actionPerformed(ActionEvent ae)
    {
        // regmntr.setText("Clicked");
    	//validate data
    	if(ae.getSource()==delmntr)
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
        if(this.dgn.getText().equals(""))
        {
             JOptionPane.showMessageDialog(this,"Empty \"Designation\" Field!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        if(this.foe.getText().equals(""))
        {
             JOptionPane.showMessageDialog(this,"Empty \"Field of Expertise\" Field!!!","Registration",JOptionPane.ERROR_MESSAGE);
             return;
        }
        //Database Entry
        String query="select Loginid from mentormaster where Loginid='"+ this.logid.getText().trim() +"'";
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
        query="insert into mentormaster set " +
                "LoginId='" + this.logid.getText().trim() + "'," +
                "Password ='" + new String(this.pwd.getPassword()).trim() + "'," +
                "UName='" + this.name.getText().trim() + "'," +
                "Desg='" + this.dgn.getText().trim() + "'," +
                "Expertise='" + this.foe.getText().trim() + "'," +
                "AddedOn='" + new java.util.Date().toString() + "'";
        try
        {
             ConnectionFactory.getInstance().setData(query);
             JOptionPane.showMessageDialog(this,"Successfully Registered","Registration",JOptionPane.INFORMATION_MESSAGE);
             
              query="select LoginId,UName,Desg,Expertise,AddedOn from mentormaster";
    	      ResultSet rs=ConnectionFactory.getInstance().getData(query);
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
                 mntrTable.repaint();
                 
             //Empty Entry fields...
             this.logid.setText("");
             this.pwd.setText("");
             this.pwd2.setText("");
             this.name.setText("");
             this.dgn.setText("");
             this.foe.setText("");
        }
        catch(Exception ex)
        {
            System.out.println("Problem in query : " + query + "\nReason: " + ex);
        }
            
    }
    
}