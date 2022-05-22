package serverapp;

import java.util.Vector;

public class ServerRes
{    
	  public static ServerWin win;
      public static Vector loggedIn;
      public static boolean hasSession;
      public static String sessId;
      static
      {
    	  sessId="";
    	  hasSession=false;
    	  win=null;
    	  loggedIn=new Vector();
      }
}
