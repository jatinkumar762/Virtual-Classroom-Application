package serverapp;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Blob;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


public class ClientThread extends Thread
{
  private Socket client;
  private String type;
  private String logid;
  private String loginTime;
  private String uName;
  private String dirPath;
  private Integer SessID;
  
  public ClientThread(Socket client)
  {
    this.client=client;
    this.start();
  }
  private String sessionDownload(int sessId){
      String html="";
      String topic="";
 
      try{
            String query="select * from sessiondata where SessId='" + sessId+"'";
            ResultSet rs=ConnectionFactory.getInstance().getData(query);
            if(rs.next())
            {
            	topic=rs.getString("Topic");
                html+="<html>"
                        + "<head>"
                        + "<title>" + rs.getString("Title") + "</title>"
                        + "</head>"
                        + "<body>"
                        + "<h1>" + rs.getString("Topic") + "</h1>"
                        + "<table width='100%'>"
                        + "<tr><td>Description</td></tr>"
                        + "<tr><td><textarea rows='10' cols='50'>" + rs.getString("CodeContent")+ "</textarea></td></tr>"
                        + "</table><br/><br/>"
                        + "<h1>'Discussion'</h1>"
                        + "<table>"
                        + "<tr><td>'Sender'</td><td>' Content'</td></tr>";
            }         
            query="select Sender,ContentType,TextContent from discussiondata where SessId="+sessId;           
            Vector main=ConnectionFactory.getInstance().getStoredData(query);
            dirPath+="/";
            for(int i=0,j=0;i<main.size();i++)
            {
                Vector sub=(Vector)main.elementAt(i);
                html+="<tr><td>"+sub.elementAt(0).toString()+"</td><td>";
                if(Integer.parseInt(sub.elementAt(1).toString())==0)
                {
                    html+="<td>"+sub.elementAt(2).toString()+"</td></tr>";
                }    
                else
                {
                    html+="<td><audio src='"+dirPath+topic+ (++j) +".wav' controls='controls'>Your browser does not support the audio element.</audio></td></tr>";
                }
            }   
                       html+="</table>"
                        + "</body>"
                        + "</html>";
            
      }
      catch(Exception ex)
      {
          JOptionPane.showMessageDialog(ServerRes.win,ex.toString(),"Download",JOptionPane.ERROR_MESSAGE);
      }
  
      return html;
  }
  public void run()
  {

       try
       {
         while(true)
         {
        	 
          ObjectInputStream in=new ObjectInputStream(this.client.getInputStream());
          String req=in.readObject().toString();
          
          if(req.equals("LogDetails"))  //request for validating login...
          {
             String logid=in.readObject().toString();
             String pwd=in.readObject().toString();
             String type=in.readObject().toString();
             boolean isValid=false;
             java.sql.ResultSet rs=null;
             if(type.equals("mentor"))
             {
                //check form database
            	String query="select * from mentormaster where Loginid='"+logid.trim()+"' AND Password='"+pwd+"'";
                rs=ConnectionFactory.getInstance().getData(query);
                if(rs.next())
                isValid=true;
             }
             else //student validation
             {
                 //check form database
            	String query="select * from studentmaster where LoginId='"+logid.trim()+"' AND Password='"+pwd+"'";
            	rs=ConnectionFactory.getInstance().getData(query);
                if(rs.next())
                isValid=true;
             }
             ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
             if(isValid)
             {
            	  out.writeObject("Valid");
            	  out.writeObject(rs.getString("UName"));
            	  this.uName=rs.getString("UName");
            	  this.logid=logid;
                  this.type=type;
                  this.loginTime=new java.util.Date().toString();
                  ServerWin.current.txtarea.append("\n" +this.uName + "\t[" + this.type + "]" + "\tLogged In \t" + this.loginTime + "\t" + this.client.getInetAddress().getHostAddress());
          
                  ClientDetails cDetails=new ClientDetails();
                  cDetails.client=this.client;
                  cDetails.logid=this.logid;
                  cDetails.loginTime=this.loginTime;
                  cDetails.type=this.type;
                  ServerRes.loggedIn.add(cDetails);
                  
                  String query="insert into loghistory set "+
                                "LoginId='"+this.logid+"',"+
                                "utype='"+this.type+"',"+
                                "LoginTime='"+this.loginTime+"',"+
                                "MachineIP='"+this.client.getInetAddress().getHostAddress()+"'";
                  ConnectionFactory.getInstance().setData(query);    
             }
             else
             {
            	 out.writeObject("Invalid");
             }
          }
          if(req.equals("mentorschedule"))
          {
             String topic=in.readObject().toString();
             String date=in.readObject().toString();
             String time=in.readObject().toString();
             int duration=Integer.parseInt(in.readObject().toString());
             
             ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());

             //check from database
             String query="select LoginId,SessDate,SessTime from schedulemaster where LoginId='" + this.logid + "' and SessDate='" + date + "' and SessTime='" + time + "'";
             java.sql.ResultSet rs=ConnectionFactory.getInstance().getData(query);
             out.writeObject("SessionValidate");
             if(rs.next()) // A session exists there at this time by this mentor
                 out.writeObject("Error");
             else
             {
            	//database entry
             query="insert into schedulemaster set "+
                          "LoginId='"+this.logid+"',"+
            		      "SessDate='"+date+"',"+
                          "SessTime='"+time+"',"+
            		      "SessDuration='"+duration+"',"+
                          "Topic='"+topic+"',"+
            		      "AddedOn='"+this.loginTime+"',"+
                          "status=0";
                 ConnectionFactory.getInstance().setData(query);
                 out.writeObject("Added");		      
             }	      
          }
          if(req.equals("DownloadInfo"))
          {
        	    String query="select D.SessId,D.Title,D.Topic,D.Date,MM.UName "
        	    		     +"from sessiondata as D, schedulemaster as SM, mentormaster as MM "
        	    		     +"where D.SessId=SM.sessId and SM.LoginId=MM.LoginId";
                Vector main=ConnectionFactory.getInstance().getStoredData(query);
                ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                out.writeObject("DownloadInfo");
                out.writeObject(main);  	 	  
          }
          if(req.equals("SessionInfo"))
          {
        	  if(type.equals("mentor"))
        	  {
        	     String query="select * from schedulemaster where LoginId='" + this.logid + "' and Status=0";
                     Vector main=ConnectionFactory.getInstance().getStoredData(query);
                     ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                     out.writeObject("SessionInfo");
                     out.writeObject(main);
        	  }
        	  else
        	  {
        		  String query="select sessId, S.LoginId, SessDate, SessTime, SessDuration, Topic, UName "
        		  		+ "from schedulemaster as S,mentormaster as M where "
        		  		+ "S.LoginId=M.LoginId "
        		  		+ "and S.Status=0";
                  Vector main=ConnectionFactory.getInstance().getStoredData(query);
                  ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                  out.writeObject("SessionInfo");
                  out.writeObject(main);
        	  }
          }
          if(req.equals("Logout"))
          {
              String logoutTime=new java.util.Date().toString();
               
              //Update Current Status Window
              ServerRes.win.current.txtarea.append("\n" + this.uName +  "\t[" + this.type + "]" + "\tLogged Out \t" + logoutTime);
              
              //Update Loghistory
        	  String query="update loghistory set LogoutTime='"+logoutTime+"' where LoginId='"
        			        +this.logid+"' and LoginTime='"+this.loginTime+"'";
        	  ConnectionFactory.getInstance().setData(query);
        	  
        	  //Remove from vector class
        	  for(int i=0;i<ServerRes.loggedIn.size();i++)
        	  {
        		  ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
        		  if(cDetails.logid==this.logid)
        		  {
        			  ServerRes.loggedIn.remove(i);
        			  break;
        		  }      		  	
        	  }  
        	 break;  	  
          }
          if(req.equals("RemoveSession"))
          {
        	     String str=in.readObject().toString();
                 int ID=Integer.parseInt(str);
                 String query="delete from schedulemaster where sessId='"+ID+"'";
                 ConnectionFactory.getInstance().setData(query);
                 ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                 out.writeObject("Remove");
          }
          if(req.equals("UpdateM"))
          {
               String topic=in.readObject().toString();
               String sessDate=in.readObject().toString();
               String sessTime=in.readObject().toString();
               String sessDuration=in.readObject().toString();
               int ID=Integer.parseInt(in.readObject().toString());
               String query="update schedulemaster set Topic='"+topic+"',"
               		        + "SessDate='"+sessDate+"',"
               		        + "SessTime='"+sessTime+"',"
               		        + "SessDuration='"+sessDuration+"' where sessId='"+ID+"'";
               ConnectionFactory.getInstance().setData(query);
               ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
               out.writeObject("SucessfullyUpdate");
               
          }
          if(req.equals("ActiveSession"))
          {
              ObjectOutputStream tmpout=new ObjectOutputStream(this.client.getOutputStream());
              tmpout.writeObject("ActiveSession");
              if(ServerRes.hasSession)
              {
                  tmpout.writeObject("SessionYes");
                  String query="select sm.SessDuration,sm.Topic,mm.UName from schedulemaster sm,mentormaster mm where sm.sessId=" + ServerRes.sessId + " and mm.LoginId=sm.LoginId ";                
                  ResultSet rs=ConnectionFactory.getInstance().getData(query);
                  rs.next();
                  tmpout.writeObject(rs.getString("UName"));
                  tmpout.writeObject(rs.getString("Topic"));
                  tmpout.writeObject(rs.getString("SessDuration"));
                  tmpout.writeObject(new java.util.Date().toString());
              }
              else
                  tmpout.writeObject("SessionNo");
        	  
          }
          if(req.equals("ActivateSession"))
          {
              String sessId=in.readObject().toString();
        	  ServerRes.hasSession=true;
        	  ServerRes.sessId=sessId;
        	  String query="select sm.SessDuration,sm.Topic,mm.UName from schedulemaster sm,mentormaster mm where sm.sessId=" + sessId + " and mm.LoginId=sm.LoginId ";
        	  ResultSet rs=ConnectionFactory.getInstance().getData(query);
        	  rs.next();
        	  for(int  i=0;i<ServerRes.loggedIn.size();i++)
        	  {
        		  ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
        		  ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());
        		  tmpout.writeObject("SessionStart");
                  tmpout.writeObject(rs.getString("UName"));
                  tmpout.writeObject(rs.getString("Topic"));
                  tmpout.writeObject(rs.getString("SessDuration"));
                  tmpout.writeObject(new java.util.Date().toString());
              }
          }
          
          if(req.equals("MentorText"))
          {
        	 String textType=in.readObject().toString();
        	 String text=in.readObject().toString();
             for(int  i=0;i<ServerRes.loggedIn.size();i++)
             {
               ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
               if(cDetails.type.equals("student")){
               ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());
               tmpout.writeObject("MentorText");
               tmpout.writeObject(textType);
               tmpout.writeObject(text);
               }
             }  
          }
          if(req.equals("DiscussionText"))
          {
         	  String text=in.readObject().toString();
              for(int  i=0;i<ServerRes.loggedIn.size();i++)
              {
                ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
                ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());
                tmpout.writeObject("DiscussionText");
                tmpout.writeObject(text);
                tmpout.writeObject(uName);
              }
              String query="insert into discussiondata set " + 
                      "SessId=" + ServerRes.sessId + "," +
                      "ContentType=0," +
                      "TextContent='" + text + "'," +
                      "Sender='" + uName + "'";
            ConnectionFactory.getInstance().setData(query);
          }
          
          if(req.equals("DiscussionAudio"))
          {
         	  byte barr[]=(byte[])in.readObject();
              for(int  i=0;i<ServerRes.loggedIn.size();i++)
              {
                ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
                ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());
                tmpout.writeObject("DiscussionAudio");
                tmpout.writeObject(barr);
                tmpout.writeObject(uName);
              }
              Class.forName("com.mysql.jdbc.Driver");
              Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost/define", "root", "");
              PreparedStatement pstmt=(PreparedStatement) con.prepareStatement("insert into discussiondata(SessId,ContentType,AudioContent,sender) values(?,?,?,?)");
              pstmt.setInt(1, Integer.parseInt(ServerRes.sessId));
              pstmt.setInt(2,1);
              Blob blob=(Blob) con.createBlob();
              blob.setBytes(1, barr);
              pstmt.setBlob(3,blob);
              pstmt.setString(4, uName);
              pstmt.executeUpdate();
          }
          if(req.equals("SessionDetails"))
          {
              for(int  i=0;i<ServerRes.loggedIn.size();i++)
              {
                ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
                if(cDetails.type.equals("mentor"))
                {
                	ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());
                	tmpout.writeObject("SessionDetails");	
                }
              }  
          }
          if(req.equals("MSessDetails"))
          {
        	  String title=in.readObject().toString();
        	  String codeEditor=in.readObject().toString();
        	 
              for(int  i=0;i<ServerRes.loggedIn.size();i++)
              {
                ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
                if(cDetails.type.equals("student"))
                {
                	ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());
                	tmpout.writeObject("SessionDetails");	
                	tmpout.writeObject(title); //Title
                	tmpout.writeObject(codeEditor); //CodeEditor
                	
                }
              } 
          }
          if(req.equals("EndSession"))
          {
        	  ServerRes.hasSession=false;
        	  String query="update schedulemaster set Status=2 where sessId="+ServerRes.sessId;
        	  ConnectionFactory.getInstance().setData(query);
        	  for(int i=0;i<ServerRes.loggedIn.size();i++)
        	  {
        		  ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
        		  ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());    		  
        		  tmpout.writeObject("EndSession");
        	  }   
          }
          if(req.equals("SaveEndSession"))
          {
        	  ServerRes.hasSession=false;
        	  String query="update schedulemaster set Status=1 where sessId="+ServerRes.sessId;
        	  ConnectionFactory.getInstance().setData(query);
        	  String Topic=in.readObject().toString();
        	  String Title=in.readObject().toString();
        	  String Code=in.readObject().toString();
        	  Integer SessID=Integer.parseInt(ServerRes.sessId);
        	  query="insert into sessiondata set " + 
                      "SessId=" + ServerRes.sessId + "," +
                      "Title='" +Title+"',"+
                      "Topic='" +Topic + "'," +
                      "CodeContent='" + Code + "',"+
                      "Date='"+new java.util.Date().toString()+"'";
        	  ConnectionFactory.getInstance().setData(query);
        	  for(int i=0;i<ServerRes.loggedIn.size();i++)
        	  {
        		  ClientDetails cDetails=(ClientDetails)ServerRes.loggedIn.elementAt(i);
        		  ObjectOutputStream tmpout=new ObjectOutputStream(cDetails.client.getOutputStream());    		  
        		  tmpout.writeObject("EndSession");
        	  }  
          }
          if(req.equals("DownloadSession"))
          {
        	  String topic;
        	  
        	  SessID=Integer.parseInt(in.readObject().toString());	
        	  String query="select Topic from sessiondata where SessId="+SessID+" ";
        	  ResultSet rs=ConnectionFactory.getInstance().getData(query);
        	  rs.next();
        	  topic=rs.getString(1);
        	  
        	  query="select AudioContent from discussiondata where SessId="+SessID+" and ContentType=1 ";
        	  rs=ConnectionFactory.getInstance().getData(query);
        	  Vector Audio=new Vector();
        	  byte barr[];
        	  int len;
        	 
        	  while(rs.next())
        	  {
        		  Blob aud=(Blob) rs.getBlob(1);
        		  len=(int)aud.length();
        		  barr=aud.getBytes(1, len);
        		  aud.free();
        		  Audio.add(barr);
        		  
        	  }
        	  ObjectOutputStream tmpout=new ObjectOutputStream(this.client.getOutputStream());
        	  tmpout.writeObject("DownloadSession");
    		 
    		  tmpout.writeObject(Audio);
    		  tmpout.writeObject(topic);
        
          }
          if(req.equals("DownloadWEB"))
          {
        	  this.dirPath=in.readObject().toString();
        	  String html=sessionDownload(SessID);
        	  ObjectOutputStream tmpout=new ObjectOutputStream(this.client.getOutputStream());
        	  tmpout.writeObject("DownloadWEB");
        	  tmpout.writeObject(html);
          }
          if(req.equals("RemoteCompile"))
          {
        	  String className=in.readObject().toString();
        	  String code=in.readObject().toString();
        	  String result="";
               try
               {
                 String fileName=className + ".java";                      //creating a new class acc. to given filename
                 FileOutputStream fout=new FileOutputStream("I:\\Eclipse\\VirtualClassRoom\\" + fileName);
                 byte barr[]=code.getBytes();                              //converting string to bytes to be written to file.
                 fout.write(barr);
                 fout.close();
                 String command="javac " + "I:\\Eclipse\\VirtualClassRoom\\" + fileName + "  1>out1.txt 2>out2.txt \nexit\n";       //compiled code in out1 and errorneous code in out2
                 fout=new FileOutputStream("test.bat");                     //creating a batch file to execute specified instructions upon runtime
                 barr=command.getBytes();
                 fout.write(barr);
                 fout.close();
                 Runtime.getRuntime().exec("cmd /c start test.bat");
                 //FileInputStream fin=new FileInputStream("out.result");
                 Thread.sleep(1000);
                         
                 File file=new File("out2.txt");
                 int size=(int)file.length();
                 //System.out.println(size);
                 java.io.FileInputStream fin=new java.io.FileInputStream(file);
                 barr=new byte[size];
                 fin.read(barr);
                 result=new String(barr);
                 if(result.equals(""))
                   result="Compiled Successfully!!!";
                 //System.out.println(result);
               }
               catch(Exception ex)
               {
                   JOptionPane.showMessageDialog(ServerRes.win,"Error while compilation : " + ex,"Remote Compiler",JOptionPane.ERROR_MESSAGE);
               }
               ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
               out.writeObject("RemoteCompile");
               out.writeObject(result);       	   	  
          }
          
         }
       }
       catch(Exception ex)
       {
    	    ex.printStackTrace();
            JOptionPane.showMessageDialog(ServerRes.win,ex.toString(),"ClientThread",JOptionPane.ERROR_MESSAGE); 
       }
       
  }
}