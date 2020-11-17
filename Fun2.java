import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public  class Fun2{
  public String Table_Initialization(){
    String text= "CREATE TABLE IF NOT EXISTS TIKTOK " +"(ID INT PRIMARY KEY     NOT NULL," +" NAME           CHAR(50)   NOT NULL, " +" TEXT            TEXT     NOT NULL, " +" SOUND_TAG     CHAR(500) , " +" LIKES_NUMBER         INT,"+ "COMMENTS_NUMBER         INT,"+ "SHARES_NUMBER      INT)";
    return text;
  }
	public  String MySQL_Database_Creation(Video[] table,int coun) throws Exception{
    	Connection c = null;
      try {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
        Statement stmt = c.createStatement();
        String sql=Table_Initialization();
        stmt.executeUpdate(sql);
        put_Values_toDatabase(c,table,coun,stmt);
        stmt.close();
        c.close();
      } catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      return null;
	}
  public static void put_Values_toDatabase(Connection c,Video[] table,int coun,Statement stmt) throws Exception{
    for(int i=0;i<coun;i++){
      System.out.println("i="+i);
      int Id=table[i].ID;
      String name=table[i].Name;
      String Text=table[i].Text;
      String Sound_Tag=table[i].Sound_Tag;
      int Likes_num=table[i].Likes_Number;
      int Comments_num=table[i].Comments_Number;
      int Shares_num=table[i].Shares_Number;
      String sql = "INSERT INTO TIKTOK (ID,NAME,TEXT,SOUND_TAG,LIKES_NUMBER,COMMENTS_NUMBER,SHARES_NUMBER) "+  "VALUES"+ "("+ Id+","+ name+","+ Text+","+ Sound_Tag+","+Likes_num+","+Comments_num+","+Shares_num+");";
      System.out.println(sql);
      stmt.executeUpdate(sql);
    }
  }
}