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
import javax.swing.JTable;

public class Download extends JPanel implements ActionListener
{
	 JTable dTable;
	 JLabel label;
	 JButton dButton;
	 JScrollPane jspSession;
	 Object HEAD[]={"MName","Topic","Title","Date"};
	 Object DATA[][];
	
    public Download()
    {
    	 this.setLayout(null);
   
    	 DATA=new String[100][5] ;
    	 this.dTable=new JTable(this.DATA,this.HEAD);
         dButton=new JButton("Download");
         label=new JLabel("Available Sessions For Download........");
         label.setFont(new Font("Serif", Font.BOLD+Font.ITALIC, 20));
    	 JScrollPane jspSession=new JScrollPane(dTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	 this.add(label);
    	 this.add(dButton);
    	 this.add(jspSession);
         label.setBounds(30,20,300,25);
         dButton.addActionListener(this);
    	 dButton.setBounds(250,420,100,30);
    	 jspSession.setBounds(30,50,540,350);
    	    	
    }
	
    public void actionPerformed(ActionEvent ae)
    {
    	if(ae.getSource()==dButton)
    	{
    		try
    		{
    			int row=this.dTable.getSelectedRow();
	    		if(row<0)
	    		{
	    		    JOptionPane.showMessageDialog(ClientRes.swin.download,"No Session Selected For Download!!!","Download",JOptionPane.WARNING_MESSAGE);
                            return;
	    		}
	    		if(JOptionPane.showConfirmDialog(this,"Are you sure ?","Session Download",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
	    		{
	    		    ObjectOutputStream out=new ObjectOutputStream(ClientRes.client.getOutputStream());
	    		    out.writeObject("DownloadSession");
	    		    out.writeObject((String)DATA[row][4]);  		
	    		}
	    		return;
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    			JOptionPane.showMessageDialog(ClientRes.swin.download, ex,"Download", JOptionPane.INFORMATION_MESSAGE);
    		}		
    		
    	}
    }
}
