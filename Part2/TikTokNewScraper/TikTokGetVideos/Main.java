import java.io.File;  
import java.io.*; 
import java.nio.file.Files;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import org.jsoup.nodes.DataNode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.openqa.selenium.chrome.ChromeOptions;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.FileWriter; 
import java.io.IOException; 
import java.io.PrintWriter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;  
import org.openqa.selenium.WebDriver;
import java.io.FileWriter; 
import org.jsoup.Jsoup; 
import org.openqa.selenium.*;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
class Main{
    public static void main(String args[]) throws Exception{
        try{
            int counter1=0;
            try (BufferedReader br = new BufferedReader(new FileReader("/home/prokopis/Desktop/Undergraduate_Thesis/Part2/TikTokNewScraper/TikTokGetNames/result.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    counter1=counter1+1;
                    System.out.println(counter1);
                    Scrape_User(line);
                }
            }
        }
        catch(Exception e){

        }
    }
    public static void Scrape_User(String str){
        System.setProperty("/home/prokopis/Downloads/chromedriver_linux64", "/usr/bin/google-chrome-stable");
        List<String> url_list=new ArrayList<String>();
        List<String> Name_list=new ArrayList<String>();
        List<String> Text_list=new ArrayList<String>();
        List<String> Play_List=new ArrayList<String>();
        ChromeOptions chrome_options = new ChromeOptions();
        chrome_options.addArguments("user-data-dir=selenium"); 
        String name="https://www.tiktok.com/@"+str+"?lang=el-GR";
        ChromeDriver driver = new ChromeDriver(chrome_options=chrome_options);/*Opening the driver*/
        driver.get(name);
        try{
            TimeUnit.SECONDS.sleep(2);
        }
        catch(Exception e){
            System.err.println(e);
        }
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        for(int i=0;i<20;i++){
            jse.executeScript("window.scrollBy(0,100)", "");
        }
        String html = driver.getPageSource();/*get the html code from site*/
        Document doc = Jsoup.parse(html);/*parse the html code*/
        Elements video=doc.select("div.tiktok-yz6ijl-DivWrapper.e1u9v4ua1");
        for(Element elem:video){
            String Likes=elem.text();
            String digits = Likes.replaceAll("[^0-9.K]", "");
            if(digits.charAt(0)=='.')
                digits = digits.substring(1);
            Play_List.add(digits);
            Element elem2=elem.select("a").first();
            String url = elem2.attr("href");
            url_list.add(url);
            Name_list.add(name);
        }
        int counter=0;
        for(Element elem:video){
            counter=counter+1;
            Element elem2=elem.select("img").first();
            String Text = elem2.attr("alt");
            Text_list.add(Text);
        }
        try{
            String filename="/home/prokopis/Desktop/Undergraduate_Thesis/Part2/TikTokData/records.txt";
            FileWriter fw = new FileWriter(filename,true);
            for(int i=0;i<counter;i++){
                fw.write(str+"|");
                fw.write(Text_list.get(i)+"|");
                fw.write(Play_List.get(i)+"|");
                fw.write(url_list.get(i)+"\n");
            }
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
        driver.close();
    }
}
