import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public  class Fun2{
	public  String MySQL_Database_Creation(Video[] table,int coun) throws Exception{
    	Connection c = null;
      try {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
        Statement stmt = c.createStatement();
        stmt.close();
        c.close();
      } catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      return null;
	}
}