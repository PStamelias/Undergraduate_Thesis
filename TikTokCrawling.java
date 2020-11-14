import java.io.IOException;
import java.util.logging.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
class Video{
	public int ID;
	public String Name;
	public String Text;
	public String HashTags;
	public String Sound_Tag;
	public int Likes_Number;
	public int Comments_Number;
	public int Shares_Number;
	Video(){

	}
}
class TikTokCrawling{
    public static void main (String[] args) throws IOException{
   		String html="https://www.tiktok.com/foryou?lang=en";
	   	Document doc = Jsoup.connect(html).get();  
	   	
    }
}
