import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;  
import org.openqa.selenium.WebDriver;
import java.io.FileWriter; 
import org.jsoup.Jsoup;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;  
import java.sql.Statement;
import java.util.*; 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.concurrent.TimeUnit;
class YoutubeScraper{
    public static void main(String[] args) {
        create_table();
        CheckFolder();
        List<String> Name_list=new ArrayList<String>();
        List<String> Creator_list=new ArrayList<String>();
        List<String> Description_list=new ArrayList<String>();
        List<String> Views_list=new ArrayList<String>();
        List<String> DateCreation_list=new ArrayList<String>();
        System.setProperty("/home/prokopis/Downloads/chromedriver_linux64", "/usr/bin/google-chrome-stable");
        int num=0;
        WebDriver driver = new ChromeDriver();/*Opening the driver*/
        driver.get("https://www.youtube.com/");/*get the specific url*/
        try{
            TimeUnit.SECONDS.sleep(15);
        }
        catch(Exception e){
            System.err.println(e);
        }
        /*Scraping the DATA*/
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        for(int i=0;i<150;i++)
            jse.executeScript("window.scrollBy(0, 1000)", "");
        String html = driver.getPageSource();/*get the html code from site*/
        Document doc = Jsoup.parse(html);/*parse the html code*/
        //System.out.println(doc);
        Elements pro_titles=doc.select("ytd-rich-item-renderer.style-scope.ytd-rich-grid-renderer");
        Elements g=pro_titles.select("div#meta.style-scope.ytd-rich-grid-media");
        Elements name=g.select("yt-formatted-string#video-title.style-scope.ytd-rich-grid-media");
        for(Element a:name){
            Name_list.add(a.text());
        }
        Elements creat=pro_titles.select("a.yt-simple-endpoint.style-scope.yt-formatted-string");
        for(Element te:creat){
            Creator_list.add(te.text());
        }
        Elements views=pro_titles.select("span.style-scope.ytd-video-meta-block");
        int kala=0;
        for(Element t:views){
            System.out.println("view="+t.text());
            if(num==0){
                Views_list.add(t.text());
                num=1;
            }
            else{
                DateCreation_list.add(t.text());
                num=0;
            }
            kala+=1;
        }
        Elements description=pro_titles.select("yt-formatted-string.description-snippet.style-scope.ytd-rich-grid-media");
        for(Element c:description){
            Description_list.add(c.text());
        }
        int coun=0;
        for(String e:Creator_list){
            coun+=1;
        }
        int views_coun=0;
        for(String k:Views_list){
            views_coun+=1;
        }
        int lastnumofile=CheckFolder();
        try{
            FileWriter myWriter = new FileWriter("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Data/out"+lastnumofile+".txt");
            for(int i=0;i<coun;i++){

                myWriter.write(Name_list.get(i)+"|");
                myWriter.write(Creator_list.get(i)+"|");
                myWriter.write(Description_list.get(i)+"|");
                if(i>=views_coun-1){
                    myWriter.write(" | ");
                    myWriter.write("\n");
                    continue;
                }
                myWriter.write(Views_list.get(i)+"|");
                myWriter.write(DateCreation_list.get(i)+"\n");
                
            }
            myWriter.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
        /**/
        driver.close();
    }
    public static void create_table(){
        try{
         Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
         Statement stmt1 = c.createStatement();
         String text= "CREATE TABLE IF NOT EXISTS YoutubeTable " +"(ID INT PRIMARY KEY     NOT NULL," +" NAME           CHAR(50)   NOT NULL, " +" CREATOR            TEXT     NOT NULL, " +" DESCRIPTION     TEXT NOT NULL," + "VIEWS TEXT NOT NULL,"+ "DATECREATION TEXT NOT NULL)";
         stmt1.executeUpdate(text);                                                                                                                                                                               
         stmt1.close();
         c.close();
      }
      catch(Exception e){
         System.err.println(e);
      }
    }
    public static int CheckFolder(){
        File folder = new File("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Data");
        File[] listOfFiles = folder.listFiles();
        int coun=-1;
        for (File file : listOfFiles) {
            int num=0;
            if (file.isFile()) {
                String e=file.getName();
                for(int i=0;i<e.length();i++){
                    if(e.charAt(i)>='0'&&e.charAt(i)<='9'){
                        num=num*10+e.charAt(i)-'0';
                    }
                }
            }
            if(num>coun)
                coun=num+1;
        }
        if(coun==-1)
            coun=0;
        return coun;
    }
}