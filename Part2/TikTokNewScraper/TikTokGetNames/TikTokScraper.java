import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;  
import org.openqa.selenium.WebDriver;
import java.io.FileWriter; 
import org.jsoup.Jsoup;
import org.openqa.selenium.chrome.ChromeOptions;
import java.sql.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;  
import java.sql.Statement;
import java.util.*; 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
class TikTokScraper{
   public static void main(String[] args) throws Exception{
      int counter=0;
      List<String> Name_list=new ArrayList<String>();
      List<String> Final_List=new ArrayList<String>();
      System.setProperty("/home/prokopis/Downloads/chromedriver_linux64", "/usr/bin/google-chrome-stable");
      ChromeOptions chrome_options = new ChromeOptions();
      chrome_options.addArguments("user-data-dir=selenium"); 
      ChromeDriver driver = new ChromeDriver(chrome_options=chrome_options);/*Opening the driver*/
      driver.get("https://www.tiktok.com/foryou?lang=en");/*get the specific url*/
      try{
         TimeUnit.SECONDS.sleep(15);
      }
      catch(Exception e){
         System.err.println(e);
      }
      JavascriptExecutor jse = (JavascriptExecutor)driver;
      for(int i=0;i<800;i++){
         System.out.println(i);
         jse.executeScript("window.scrollBy(0,800)", "");
      }
      String html = driver.getPageSource();/*get the html code from site*/
      Document doc = Jsoup.parse(html);/*parse the html code*/
      //System.out.println(doc);
      Element first_step=doc.select("div.tiktok-1id9666-DivMainContainer.e7i8kv30").first();
      Elements videos_info=first_step.select("div.tiktok-1p48f7x-DivItemContainer.e1eulw5o0");
      for(Element video:videos_info){
         Element name=video.select("h3.tiktok-debnpy-H3AuthorTitle.e10yw27c0").first();
         System.out.println(name.text());
         Name_list.add(name.text());
      }
      HashSet<String> uniqueValues = new HashSet<>(Name_list);
      for(String value:uniqueValues){
         Final_List.add(value);
         counter=counter+1;
      }
      driver.close();
      try{
         String filename= "result.txt";
         FileWriter fw = new FileWriter(filename,true);
         for (String value :Final_List)
            fw.write(value+"\n");
         fw.close();
      }
      catch(IOException ioe)
      {
         System.err.println("IOException: " + ioe.getMessage());
      }
      cut_double_values();
   }
   public static void cut_double_values() throws Exception{
      String file="result.txt";
      List<String> First_List=new ArrayList<String>();
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
         String line;
         while ((line = br.readLine()) != null) {
            First_List.add(line);
         }
      }
      PrintWriter writer = new PrintWriter(file);
      writer.print("");
      writer.close();
      List<String> newList = new ArrayList<String>(new HashSet<String>(First_List));
      try{
         String filename= "result.txt";
         FileWriter fw = new FileWriter(filename,true);
         for (String value :newList){
            boolean exists_value=check_if_exist_ontable(value);
            if(exists_value==true)
               continue;
            fw.write(value+"\n");
         }
         fw.close();
      }
      catch(IOException ioe)
      {
         System.err.println("IOException: " + ioe.getMessage());
      }
   }
   public static boolean check_if_exist_ontable(String str) throws Exception{
      Class.forName("org.postgresql.Driver");
      Connection c=null;
      String name;
      c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
      Statement stmt1=c.createStatement();
      ResultSet rs=stmt1.executeQuery("select * from WEBSCRAPEDTIKTOKDATA order by ID desc limit 1");
      while(rs.next()){
         name = rs.getString(2);
         System.out.println(name);
         if(name.equals(str)==true)
            return true;
      }
      stmt1.close();
      return false;
   }
}