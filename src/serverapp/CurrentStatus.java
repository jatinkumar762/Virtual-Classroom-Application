package serverapp;
import java.lang.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class CurrentStatus extends JPanel // implements ActionListener
{
    JTextArea txtarea;
    JScrollPane jsp;

    public CurrentStatus()
    {
        this.setLayout(null);
        
        txtarea=new JTextArea();
        jsp=new JScrollPane(txtarea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        txtarea.setEditable(false);
        txtarea.append("Name"+ "\tType "+"\tActivity "+ "\tDate & Time "+"\t\tClient Ip Addr");
        this.add(jsp);
        jsp.setBounds(20,20,560,500);
    }
}