package serverapp;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;

public class ServerStart
{
   public static void main(String args[])
   {
       try
       {
    	   	ServerSocket server=new ServerSocket(2200);
    	   	System.out.println("Server Started Successfully");
          	//show Server Window
          	ServerRes.win=new ServerWin();
          	Toolkit tool=Toolkit.getDefaultToolkit();
          	Dimension size=tool.getScreenSize();
         	final int WIDTH=600;
         	final int HEIGHT=600;
         	ServerRes.win.setBounds(size.width/2-WIDTH/2,size.height/2-HEIGHT/2,WIDTH,HEIGHT);
         	ServerRes.win.setResizable(false);
         	ServerRes.win.setTitle("Server Window");
         	ServerRes.win.setVisible(true);
          while(true)
          {
           System.out.println("Waiting for client...");
           Socket client=server.accept();
           System.out.println("Client Connected Successfully");
           //invokes thread
           new ClientThread(client);
          }
       }
       catch(Exception ex)
       {
    	  ex.printStackTrace();
          System.out.println("Cannot Start Server!!!");
       }
   }
}