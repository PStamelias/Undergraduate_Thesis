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
   		Elements photos = doc.select("div[id=__next]");
   		Elements e = photos.select("div[id=main]");
   		Elements t=e.select("div.jsx-3309473600.main-body.page-with-header");
   		Elements l=t.select("div.jsx-4154131465.share-layout");
   		Elements s=l.select("main.share-layout-main");
   		System.out.println("size="+s.size());
   	}
}

