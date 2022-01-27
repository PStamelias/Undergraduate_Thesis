import java.io.File;  
import java.io.*; 
import java.nio.file.Files;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.FileWriter; 
import java.io.IOException; 
import java.io.PrintWriter;


class InputData{
    public static void main(String args[]) throws Exception{
        Create_Table();
        try (BufferedReader br = new BufferedReader(new FileReader("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/TikTokData/records.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Put_Data_to_Database(line);
            }
        } 
    }
    public static void Put_Data_to_Database(String str) throws Exception{
        //System.out.println(str);
        Class.forName("org.postgresql.Driver");
        Connection c=null;
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
        int pos=0;
        int num=0;
        String name="";
        String Text="";
        String Play_times="";
        String Link="";
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='|'){
                pos++;
                continue;
            }
            if(pos==0)
                name=name+str.charAt(i);
            else if(pos==1)
                Text=Text+str.charAt(i);
            else if(pos==2)
                Play_times=Play_times+str.charAt(i);
            else if(pos==3)
                Link=Link+str.charAt(i);
        }
        /*System.out.println(name);
        System.out.println(Text);
        System.out.println(Play_times);
        System.out.println(Link);*/
        Statement stmt1=c.createStatement();
        ResultSet rs=stmt1.executeQuery("select * from TikTokScrapedVideoTable order by ID desc limit 1");
        while(rs.next()){
            num = rs.getInt(1);
        }
        //System.out.println(num);
        num=num+1;
        stmt1.close();
        PreparedStatement st = c.prepareStatement("INSERT INTO TikTokScrapedVideoTable (ID,NAME,TEXT,PLAY_NUM,LINK) VALUES (?,?,?,?,?)");
        st.setInt(1,num);
        st.setString(2,name);
        st.setString(3,Text);
        st.setString(4,Play_times);
        st.setString(5,Link);
        st.executeUpdate();
        st.close();

    }
    public static void  Create_Table(){
        try{
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
            Statement stmt1 = c.createStatement();
            String text= "CREATE TABLE IF NOT EXISTS TikTokScrapedVideoTable " +"(ID INT PRIMARY KEY NOT NULL,NAME           TEXT   NOT NULL, " +" TEXT           TEXT   NOT NULL, "+" PLAY_NUM            TEXT     NOT NULL, " +" LINK    TEXT NOT NULL)";
            stmt1.executeUpdate(text);                                                                                                                                                                               
            stmt1.close();
            c.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
