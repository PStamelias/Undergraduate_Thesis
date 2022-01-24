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
        /*try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Put_Data_to_Database(line);
            }
        } */   
    }
    public static void Put_Data_to_Database(String str){

    }
    public static void  Create_Table(){
        try{
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
            Statement stmt1 = c.createStatement();
            String text= "CREATE TABLE IF NOT EXISTS VideoTikTokInfo " +"(ID INT PRIMARY KEY     NOT NULL," +" NAME           TEXT   NOT NULL, " +" TEXT           TEXT   NOT NULL, "+" SOUND_TAG            TEXT     NOT NULL, " +" LIKES    TEXT NOT NULL," + "COMMENTS TEXT NOT NULL,"+ "SHARES TEXT NOT NULL)";
            stmt1.executeUpdate(text);                                                                                                                                                                               
            stmt1.close();
            c.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
