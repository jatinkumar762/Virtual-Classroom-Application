package serverapp;

import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.Vector;

public class ConnectionFactory 
{
	 private Statement stmt;
	 private Connection con;
         private static ConnectionFactory conn;
     
     private ConnectionFactory()
     {
    	 try
    	 {
    		 Class.forName("com.mysql.jdbc.Driver");
    		 this.con=DriverManager.getConnection("jdbc:mysql://localhost/vclassroom","root","");
    		 this.stmt=this.con.createStatement(); 		 
    	 }
    	 catch(Exception ex)
    	 {

    	 }
     }
     static
     {
    	 conn=new ConnectionFactory();
     }
     public static ConnectionFactory getInstance()
     {
    	 return conn;
     }
     public int setData(String query)
     throws Exception  //SQLException Generate
     {
    	 int n=-1;
    	 n=this.stmt.executeUpdate(query);
    	 return n;  	 
     }
     public ResultSet getData(String query)
     throws Exception   //SQL Exception generate
     {
    	 ResultSet rs=null;
    	 rs=this.stmt.executeQuery(query);
    	 return rs;
     }
     public Vector getStoredData(String query)
     throws Exception        
     {
        Vector main=new Vector();
        ResultSet rs=this.stmt.executeQuery(query);
        ResultSetMetaData rsmd=rs.getMetaData();
        int col=rsmd.getColumnCount();
        while(rs.next())
        {
            Vector sub=new Vector();
            for(int i=1;i<=col;i++)
            sub.add(rs.getString(i));
            main.add(sub);
        }
        return main;
     
     }
}
