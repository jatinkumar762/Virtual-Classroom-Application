package clientapp;
import java.lang.*;
import java.net.*;
import java.io.*;

public class ClientRes
{
   public static Socket client;
   public static String logid;
   public static String type;
   public static MentorWin mwin;
   public static StudentWin swin;
   static
   {
       swin=null;
       mwin=null;
       client=null;
       logid="";
       type="";
   }
}