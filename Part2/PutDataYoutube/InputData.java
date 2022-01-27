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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
class InputData{
    public static void main(String args[]) throws Exception{
        File folder = new File("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Data");
        File[] listOfFiles = folder.listFiles();
        int coun=-1;
        String line;
        for (File file : listOfFiles) {
            int num=0;
            if (file.isFile()) {
                String e=file.getName();
                System.out.println("file="+e);
                boolean isFound = e.contains(".txt");
                if(isFound==true){
                    Scanner scanner=null;    
                    try{
                        scanner = new Scanner(file);
                    }catch(Exception r){
                        System.err.println(r);
                    }
                    boolean readit=false;
                    while (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                        if(line.equals("!-----!")){
                            readit=true;
                        }
                    }
                    if(readit==false)
                        PutData(file);
                }
            }
        }
    }
    public static void PutData(File NameFile) throws Exception{
        String e=NameFile.getName();
        FileWriter fw = null; 
        BufferedWriter bw = null; 
        PrintWriter pw = null;
        try{ 
            fw = new FileWriter("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Data/"+e, true); 
            bw = new BufferedWriter(fw); 
            pw = new PrintWriter(bw);
            Scanner scanner=null;
            String line;    
            try{
                File file = new File("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Data/"+e);
                scanner = new Scanner(file);
            }catch(Exception r){
                System.err.println(r);
            }
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                PuttoTable(line);
            }
            pw.println("\n\n!-----!"); 
            pw.flush(); 
        }finally{ 
            try{ 
                pw.close(); 
                bw.close(); 
                fw.close(); 
            } 
            catch (IOException io) {}
        }
    }
    public static void PuttoTable(String line) throws Exception{
        int num=0;
        Class.forName("org.postgresql.Driver");
        Connection c=null;
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
        String name="";
        String Creator="";
        String Description="";
        String Views="";
        String DateCreation="";
        int pos=1;
        for(int i=0;i<line.length();i++){
            char e=line.charAt(i);
            if(e=='|'){
                pos++;
                continue;
            }
            if(pos==1)
                name=name+e;
            if(pos==2)
                Creator=Creator+e;
            if(pos==3)
                Description=Description+e;
            if(pos==4)
                Views=Views+e;
            if(pos==5)
                DateCreation=DateCreation+e;
        }
        Statement stmt1=c.createStatement();
        ResultSet rs=stmt1.executeQuery("select * from Youtube order by id desc limit 1");
        while(rs.next()){
            num = rs.getInt(1);
        }
        stmt1.close();
        if(DateCreation.contains("watching")==true){
            String temp=DateCreation;
            DateCreation=Views;
            Views=temp;
        }
        if(DateCreation.contains("views")==true||Views.contains("ago")==true){
            String temp=DateCreation;
            DateCreation=Views;
            Views=temp;
        }
        Statement stmt = c.createStatement();
        ResultSet rse = stmt.executeQuery("SELECT NAME FROM Youtube");
        boolean val=false;
        while(rse.next()){
            String  title = rse.getString("NAME");
            if(name.equals(title))
                val=true;
        }
        if(val==true)
            return ;
        stmt.close();
        num=num+1;
        PreparedStatement st = c.prepareStatement("INSERT INTO Youtube (ID, NAME, CREATOR ,DESCRIPTION,VIEWS,DATECREATION) VALUES (?,?,?,?,?,?)");
        st.setInt(1,num);
        st.setString(2,name);
        st.setString(3,Creator);
        st.setString(4,Description);
        st.setString(5,Views);
        st.setString(6,DateCreation);
        st.executeUpdate();
        st.close();
    }
}
