import java.io.IOException;
import java.util.logging.*;
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;
class TikTokCrawling{
    public static void main (String[] args){
    	String url="https://www.tiktok.com/foryou?lang=en";
    	Document doc = Jsoup.parse(url);
    }
}
