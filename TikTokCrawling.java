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
   		String url="https://www.tiktok.com/foryou?lang=en";
   		Document doc = Jsoup.connect(url).get();
   		Elements g=doc.getElementsByTag("span");
   		for(Element n:g){
   			Elements h=n.getElementsByTag("div");
   			System.out.println(h.size());
   		}
   	}
}

/*Elements tags=doc.getElementsByTag("div");
   		for(Element d:tags){
   			Elements g=d.getElementsByTag("span");
   			for(Element t:g){
   				Element c=t.getElementsByTag("div").first();
   				Element m=c.getElementsByTag("div").first();
   			}
   		}*/
