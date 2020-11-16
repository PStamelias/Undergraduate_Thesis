import java.io.IOException;
import java.util.logging.*;
import org.jsoup.Jsoup;
import java.sql.*;
import java.io.*; 
import java.util.concurrent.ThreadLocalRandom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedWriter;
import org.jsoup.select.Elements;
import java.io.FileWriter;
class Video{
	public int ID;
	public String Name;
	public String Text;
	public String Sound_Tag;
	public int Likes_Number;
	public int Comments_Number;
	public int Shares_Number;
	Video(){
		this.Likes_Number=0;
		this.Comments_Number=0;
		this.Shares_Number=0;
	}

}

class TikTokCrawling{
    public static void main (String[] args) throws IOException{
   		String url="https://www.tiktok.com/foryou?lang=en";
   		String file=get_info_from_URL(url);
   		Create_Database_and_put_info_on_it(file);

   	}
   	public static String get_info_from_URL(String url) throws IOException{
   		Document doc = Jsoup.connect(url).get();
   		Elements photos = doc.select("div[id=__next]");
   		Elements e = photos.select("div[id=main]");
   		Elements t=e.select("div.jsx-3309473600.main-body.page-with-header");
   		Elements l=t.select("div.jsx-4154131465.share-layout");
   		Elements s=l.select("main.share-layout-main");
   		Elements x=s.select("div");
   		for(Element a:x){
   			System.out.println("s="+a.attr("class"));
   		}
   		BufferedWriter out = null;
   		try {
		    FileWriter fstream = new FileWriter("out.txt", true); //true tells to append data.
		    out = new BufferedWriter(fstream);
		    for(int i=0;i<10000;i++){
		    	int randomNum;
		    	String se=String.valueOf(i+1);
		    	out.write(se);
		    	out.write("|");
		    	out.write("bachelor1");
		    	out.write("|");
		    	out.write("bachelorGR");
		    	out.write("|");
		    	out.write("Λίγο πριν του φύγω #fyp #foryoupage @justgiwrgos");
		    	out.write("|");
		    	randomNum = ThreadLocalRandom.current().nextInt(0,40000000 + 1);
		    	se=String.valueOf(randomNum);
		    	out.write(se);
		    	out.write("|");
		    	randomNum = ThreadLocalRandom.current().nextInt(0,40000000 + 1);
		    	se=String.valueOf(randomNum);
		    	out.write(se);
 				out.write("|");
 				randomNum = ThreadLocalRandom.current().nextInt(0,40000000 + 1);
 				se=String.valueOf(randomNum);
		    	out.write(se);
		    	out.write("\n");
		    }
		}
		catch (IOException b) {
		    System.err.println("Error: " + b.getMessage());
		}
		finally {
		    if(out != null) {
		        out.close();
		    }
		}
		return "out.txt";
   	}
   	public static void Create_Database_and_put_info_on_it(String file) throws IOException{
   		FileReader fr = new FileReader(file); 
	    int i; 
	    int coun=0;
	    while ((i=fr.read()) != -1) {
	    	if(i=='\n')
	    		coun+=1;
	    }
	    Video[] video_Table = new Video[coun];
	    for(int e=0;e<coun;e++)
	    	video_Table[e]=new Video();
   	}
}

