package clientapp;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RemoteCompileM extends JPanel implements ActionListener
{
	JLabel labelFile,labelArea,labelRes;
	JTextField txtFile;
	JTextArea txtArea,txtResult;	
	JScrollPane jspCompile,jspResult;
	JButton btnClose,btnSumbit;
	
    public RemoteCompileM()
    {
    	this.setLayout(null);
    	
    	btnClose=new JButton("Close");
    	btnSumbit=new JButton("Sumbit");
    	
    	labelFile=new JLabel("Provide File Name         :");
    	labelArea=new JLabel("Enter your Code Below :");
    	labelRes=new JLabel("Result :");
    	
    	labelFile.setFont(new Font("Garamond",Font.BOLD,15));
    	labelArea.setFont(new Font("Garamond",Font.BOLD,15));
    	labelRes.setFont(new Font("Garamond",Font.BOLD,15));
    	
    	txtFile=new JTextField();
    	txtArea=new JTextArea();
    	txtResult=new JTextArea();
    	
    	jspCompile=new JScrollPane(txtArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	jspResult=new JScrollPane(txtResult,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	
    	labelFile.setBounds(5,10,200,25);
    	labelArea.setBounds(5,45,200,25);
    	txtFile.setBounds(180,10,200,25);
    	btnClose.setBounds(600,10,80,25);
    	jspCompile.setBounds(5,75,680,300);
    	labelRes.setBounds(5, 380, 100, 25);
    	jspResult.setBounds(5,400,680,100);
    	btnSumbit.setBounds(200,520,100,35);
    
    	this.add(btnClose);
    	this.add(labelRes);
    	this.add(labelArea);
    	this.add(labelFile);
    	this.add(txtFile);
    	this.add(jspCompile);
    	this.add(jspResult);
    	this.add(btnSumbit);
    	
    	txtResult.setEditable(false);
    	
    	btnClose.addActionListener(this);
    	btnSumbit.addActionListener(this);
    	
    }
    public void actionPerformed(ActionEvent ae)
    {
    	if(ae.getSource()==btnClose)
    	{
    		 if(JOptionPane.showConfirmDialog(ClientRes.mwin.mSession,"Are you sure ?","Compiler",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
    		 {
    			 ClientRes.mwin.jtb.remove(ClientRes.mwin.rmCompile);
    		 }
    		
    	}
    	if(ae.getSource()==btnSumbit)
    	{
    		 if(JOptionPane.showConfirmDialog(ClientRes.mwin.mSession,"Are you sure ?","Sumbit",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
    		 {
    			 
    			 try
    			 {
    				 ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
    				 out.writeObject("RemoteCompile");
    				 out.writeObject(txtFile.getText());
    				 out.writeObject(txtArea.getText());
    			 }
    			 catch(Exception ex)
    			 {
    				 
    			 }
    		 }
    		
    		
    	}
    	
    }
}
