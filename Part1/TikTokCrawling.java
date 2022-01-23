import java.io.IOException;
import java.util.logging.*;
import org.jsoup.Jsoup;
import java.io.*; 
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 
import org.openqa.selenium.JavascriptExecutor;  
import java.net.URL;
import java.io.File;    
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;    
import java.sql.SQLException; 
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedWriter;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
class Video{
   public int ID;
   public String Name;
   public String Text;
   public String Sound_Tag;
   public String Likes_Number;
   public String Comments_Number;
   public String Shares_Number;
   public String date;
   Video(){
      
   }
   public  void set_ID(int ID){
      this.ID=ID;
   }
   public  void set_Name(String Name){
      this.Name=Name;
   }
   public  void set_Text(String Text){
      this.Text=Text;
   }
   public  void set_SoundTag(String Sound_Tag){
      this.Sound_Tag=Sound_Tag;
   }
   public  void set_Likes_Number(String Likes){
      this.Likes_Number=Likes;
   }
   public  void set_Commend_Number(String Comments){
      this.Comments_Number=Comments;
   }
   public  void set_Shares_Number(String Shares){
      this.Shares_Number=Shares;
   }
   public void set_Date(String Date){
      this.date=Date;
   }
}

class TikTokCrawling{
    public static void main (String[] args) throws IOException{
         String url="https://www.tiktok.com/@anna_amanatidouu";
         Fun2 f2=new Fun2();
         f2.Create_Table_on_Database();
         String file=get_info_from_URL(url);
         Create_Database_and_put_info_on_it(file);
         File f1=new File("out.txt");
         f1.delete();
      }
      public static String get_info_from_URL(String url) throws IOException{
         System.setProperty("/home/prokopis/Downloads/chromedriver_linux64", "/usr/bin/google-chrome-stable");
         //System.setProperty("webdriver.gecko.driver","/home/prokopis/Downloads/geckodriver-v0.29.0-linux64/geckodriver");
         BufferedWriter out = null;
         Fun2 f2=new Fun2();
         int ID=0;
         try{
            ID=f2.Coun_records_on_Database();
         }
         catch(Exception e){
            System.err.println(e);
         }
         ID+=1;
         System.out.println(ID);
         WebDriver driver = new ChromeDriver();/*Opening the driver*/
         //WebDriver driver = new FirefoxDriver();
         driver.get(url);/*get the specific url*/
         JavascriptExecutor jse = (JavascriptExecutor)driver;
         int counter0=0;
         while(true){
            Load_More_classes(jse);
            try{
               Thread.sleep(10000);
            }
            catch(Exception e){
               System.err.println(e);
            }
            counter0=counter0+1;
            if(counter0==2)
               break;
         }  
         //Load_More_classes(jse);                                                                                                                                                                                                                                                                                                                                                                                   
         for(int i=0;i<10;i++){
            System.out.println(i);
            jse.executeScript("window.scrollBy(0, 3000)", "");
            try{
               Thread.sleep(1000L);
            }catch(Exception c){
               System.err.println(c);
            }
         }
         String html = driver.getPageSource();/*get the html code from site*/
         Document doc = Jsoup.parse(html);
         Elements info=doc.select("div.tiktok-16uk8e4-DivItemContainer.e1z53d08");
         for(Element k:info){
            System.out.println("mitsos");
         }
         System.out.println(html);
         //driver.close();
         /*****************************/
         return null;
      }
      public static void Create_Database_and_put_info_on_it(String file) throws IOException{
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int coun=0;
            while ((line = br.readLine()) != null)/*Counting lines here*/
               coun+=1;
            Video[] VideoTable=new Video[coun];
            for(int e=0;e<coun;e++)/*Creating the array of objects*/
               VideoTable[e]=new Video();
            try(BufferedReader q = new BufferedReader(new FileReader(file))){
               int j=0;
               Fun f=new Fun();
               /*Get info and store it to array of objects*/
               while ((line = q.readLine()) != null){
                  String h=line;
                  VideoTable[j].set_ID(f.Id(h));
                  VideoTable[j].set_Name(f.Name(h));
                  VideoTable[j].set_Text(f.Text(h));
                  VideoTable[j].set_SoundTag(f.Sound_Tag(h));
                  VideoTable[j].set_Likes_Number(f.Likes_Number(h));
                  VideoTable[j].set_Commend_Number(f.Comments_Number(h));
                  VideoTable[j].set_Shares_Number(f.Shares_Number(h));
                  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
                  LocalDateTime now = LocalDateTime.now();  
                  VideoTable[j].set_Date(dtf.format(now));
                  j+=1;
               }
               Fun2 f2=new Fun2();
               try{
                  f2.PostgreSQL_Database_Creation(VideoTable,coun); 
                  f2.Creation_of_Second_Database(VideoTable,coun);
                  f2.Insert_records_on_Third_Table(VideoTable,coun);
               }catch(Exception e){
                  System.out.println("Error on database");
                  return ;
              } 
         }
         catch(IOException d){
            System.out.println("Error on processing file");
               return ;
         }
      }catch(IOException e){
         System.out.println("Error on opening file");
         return ;
      }
   }
   public static void Load_More_classes(JavascriptExecutor jse){
      /*Thread[] threads = new Thread[3];
      for (int i = 0; i < threads.length; i++) {
         threads[i] = new Thread(new Runnable() {
            public void run() {
               for(int i=0;i<100;i++){
                  System.out.println(i);
                  jse.executeScript("window.scrollBy(0, 3000)", "");
                  try{
                     Thread.sleep(1000L);
                  }catch(Exception c){
                     System.err.println(c);
                  }
               }
               System.out.println(Thread.currentThread().getName()+" Finished ");
            }
         });
         threads[i].start();
      }
      for (Thread thread : threads) {
         try{
            thread.join();
         }catch(Exception c){
            System.err.println(c);
         }
      }*/
   }  
}
