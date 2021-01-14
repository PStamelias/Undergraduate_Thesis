import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public  class Fun2{
  public static String Table_Initialization(){
    String text= "CREATE TABLE IF NOT EXISTS TikTokVideoDataTable " +"(ID INT PRIMARY KEY     NOT NULL," +" NAME           CHAR(50)   NOT NULL, " +" TEXT            TEXT     NOT NULL, " +" SOUND_TAG     CHAR(500) , " +" LIKES_NUMBER         TEXT ,"+ "COMMENTS_NUMBER         TEXT,"+ "SHARES_NUMBER      TEXT , DATE TEXT)";
    return text;
  }
  public static String Table_Initialization2(){
    String text= "CREATE TABLE IF NOT EXISTS TikTokVideoHashTagInfoTable " +"(ID INT PRIMARY KEY     NOT NULL," +" NAME           CHAR(50)   NOT NULL, " +" DATE            TEXT     NOT NULL, " +" HASHTAG     TEXT NOT NULL)" ;
    return text;
  }
	public  String PostgreSQL_Database_Creation(Video[] table,int coun) throws Exception{/*Creating DataBase Function*/
    	Connection c = null;
      try{
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
        Statement stmt = c.createStatement();
        String sql=Table_Initialization();
        stmt.executeUpdate(sql);
        put_Values_toDatabase(c,table,coun,stmt);
        stmt.close();
        c.close();
      }catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      return null;
	}
  public static void put_Values_toDatabase(Connection c,Video[] table,int coun,Statement stmt) throws Exception{/*Insert Values from out.txt file to Database*/
    for(int i=0;i<coun;i++){
      int Id=table[i].ID;
      String name=table[i].Name;
      String Text=table[i].Text;
      String Sound_Tag=table[i].Sound_Tag;
      String Likes_num=table[i].Likes_Number;
      String Comments_num=table[i].Comments_Number;
      String Shares_num=table[i].Shares_Number;
      String date=table[i].date;
      PreparedStatement st = c.prepareStatement("INSERT INTO TikTokVideoDataTable (ID, NAME, TEXT, SOUND_TAG,LIKES_NUMBER,COMMENTS_NUMBER,SHARES_NUMBER,DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      if(isNumeric(Likes_num)==false)
        continue;
      st.setInt(1, Id);
      st.setString(2, name);
      st.setString(3, Text);
      st.setString(4, Sound_Tag);
      st.setInt(5,Integer.parseInt(Likes_num));
      st.setInt(6,Integer.parseInt(Comments_num));
      if("Share".equals(Shares_num))
        st.setInt(7,0);
      else
        st.setInt(7,Integer.parseInt(Shares_num));
      st.setString(8,date);
      st.executeUpdate();
      st.close();
    }
  }
  public  int Coun_records_on_Database() throws Exception{
    int num=0;
    Class.forName("org.postgresql.Driver");
    Connection c=null;
    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
    String sql=Table_Initialization();
    Statement stmt = c.createStatement();
    stmt.executeUpdate(sql);
    Statement stmt1=c.createStatement();
    ResultSet rs=stmt1.executeQuery("select * from TikTokVideoDataTable order by id desc limit 1");
    while(rs.next()){
      num = rs.getInt(1);
    }
    stmt.close();
    stmt1.close();
    c.close();
    return num+1;
  }
  public static  int Coun_records_on_Database2() throws Exception{
    int num=0;
    Class.forName("org.postgresql.Driver");
    Connection c=null;
    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
    String sql=Table_Initialization();
    Statement stmt = c.createStatement();
    stmt.executeUpdate(sql);
    Statement stmt1=c.createStatement();
    ResultSet rs=stmt1.executeQuery("select * from TikTokVideoHashTagInfoTable order by id desc limit 1");
    while(rs.next()){
      num = rs.getInt(1);
    }
    stmt.close();
    stmt1.close();
    c.close();
    return num+1;
  }
  public static boolean isNumeric(final String str) {
        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
  }
  public static void Creation_of_Second_Database(Video[] table,int coun) throws Exception{
      try{
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
        Statement stmt = c.createStatement();
        String sql=Table_Initialization2();
        stmt.executeUpdate(sql);
        stmt.close();
        for(int i=0;i<coun;i++){
          int  special_num=Coun_records_on_Database2();
          String Name=table[i].Name;
          String Date=table[i].date;
          String Text=table[i].Text;
          String hash="";
          int done=0;
          int start=0;
          for(int j=0;j<Text.length();j++){
            if(Text.charAt(j)=='#'){
              start=1;
              if(done==0)
                done=1;
              else{
                if(hash!=null){
                  PreparedStatement st = c.prepareStatement("INSERT INTO TikTokVideoHashTagInfoTable (ID, NAME, DATE ,HASHTAG) VALUES (?,?,?,?)");
                  special_num+=1;
                  st.setInt(1,special_num);
                  st.setString(2,Name);
                  st.setString(3,Date);
                  st.setString(4,hash);
                  st.executeUpdate();
                  st.close();
                }
                hash="";
              }
            }
            if(start==1)
              hash=hash+Text.charAt(j);
          }
        }
        c.close();
      }
      catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
      }
    }
  }