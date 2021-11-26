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
        int lastnumofile=CheckFolder();
        int counter=0;
        List<String> Name_list=new ArrayList<String>();
        List<String> Creator_list=new ArrayList<String>();
        List<String> Description_list=new ArrayList<String>();
        List<String> Views_list=new ArrayList<String>();
        List<String> DateCreation_list=new ArrayList<String>();
        System.setProperty("/home/prokopis/Downloads/chromedriver_linux64", "/usr/bin/google-chrome-stable");
        int case1=0;
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
        Elements First_Stage=doc.select("div#content.style-scope.ytd-app");
        Elements Sec_Stage=First_Stage.select("ytd-page-manager#page-manager.style-scope.ytd-app");
        Elements Third_Stage=Sec_Stage.select("div#contents.style-scope.ytd-rich-grid-renderer");
        Elements Four_Stages=Third_Stage.select("ytd-rich-grid-row.style-scope.ytd-rich-grid-renderer");
        for(Element elem:Four_Stages){
            counter=counter+1;
            Element info=elem.select("div#details.style-scope.ytd-rich-grid-media").first();
            Element sec_info=info.select("div#meta.style-scope.ytd-rich-grid-media").first();
            Element Name_elem=sec_info.select("h3.style-scope.ytd-rich-grid-media").first();
            //System.out.println(Name_elem.text());
            Name_list.add(Name_elem.text());
            Element Creator=sec_info.select("div#byline-container.style-scope.ytd-video-meta-block").first();
            Element Likes_Views=sec_info.select("div#metadata-line.style-scope.ytd-video-meta-block").first();
            //System.out.println(Creator.text());
            Creator_list.add(Creator.text());
            Elements Likes_Views_Ver2=Likes_Views.select("span.style-scope.ytd-video-meta-block");
            for(Element e:Likes_Views_Ver2){
                //System.out.println(e.text());
                if(e.text().contains("views"))
                    Views_list.add(e.text());
                if(e.text().contains("ago"))
                    DateCreation_list.add(e.text());

            }
        }
        int coun=0;
        for(String e:Creator_list){
            coun+=1;
        }
        int views_coun=0;
        for(String k:Views_list){
            views_coun+=1;
        }
        try{
            FileWriter myWriter = new FileWriter("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Data/out"+lastnumofile+".txt");
            for(int i=0;i<coun;i++){
                myWriter.write(Name_list.get(i)+"|");
                myWriter.write(Creator_list.get(i)+"|");
                myWriter.write(" "+"|");
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
        String text= "CREATE TABLE IF NOT EXISTS Youtube " +"(ID INT PRIMARY KEY     NOT NULL," +" NAME           TEXT   NOT NULL, " +" CREATOR            TEXT     NOT NULL, " +" DESCRIPTION     TEXT NOT NULL," + "VIEWS TEXT NOT NULL,"+ "DATECREATION TEXT NOT NULL)";
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
                coun=num;
        }
        if(coun==-1)
            coun=0;
        else
            coun=coun+1;
        return coun;
    }
}