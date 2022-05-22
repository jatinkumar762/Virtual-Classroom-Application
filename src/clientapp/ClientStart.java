package clientapp;

import java.lang.*;
import java.awt.*;
import java.util.*;

public class ClientStart
{
   public static void main(String args[])
   {
        IpEntry win=new IpEntry();
        Toolkit tool=Toolkit.getDefaultToolkit();
        Dimension size=tool.getScreenSize();
        final int WIDTH=300;
        final int HEIGHT=150; 
        win.setBounds(size.width/2-WIDTH/2,size.height/2-HEIGHT/2,WIDTH,HEIGHT);
        win.setResizable(false);
        win.setTitle("IP Entry");
        win.setVisible(true);
   }
}
