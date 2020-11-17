import java.io.IOException;
import java.util.logging.*;
import org.jsoup.Jsoup;
import java.io.*; 
import java.sql.SQLException; 
import java.sql.Statement;
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
	public  void set_Likes_Number(int Likes){
		this.Likes_Number=Likes;
	}
	public  void set_Commend_Number(int Comments){
		this.Comments_Number=Comments;
	}
	public  void set_Shares_Number(int Shares){
		this.Shares_Number=Shares;
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
   		/*for(Element a:x){
   			System.out.println("s="+a.attr("class"));
   		}*/
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
   		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
   			String line;
   			int coun=0;
			while ((line = br.readLine()) != null) 
				coun+=1;
			Video[] VideoTable=new Video[coun];
			for(int e=0;e<coun;e++)
				VideoTable[e]=new Video();
			try(BufferedReader q = new BufferedReader(new FileReader(file))){
				int j=0;
				Fun f=new Fun();
				while ((line = q.readLine()) != null){
					String h=line;
					VideoTable[j].set_ID(f.Id(h));
					VideoTable[j].set_Name(f.Name(h));
					VideoTable[j].set_Text(f.Text(h));
					VideoTable[j].set_SoundTag(f.Sound_Tag(h));
					VideoTable[j].set_Likes_Number(f.Likes_Number(h));
					VideoTable[j].set_Commend_Number(f.Comments_Number(h));
					VideoTable[j].set_Shares_Number(f.Shares_Number(h));
					j+=1;
				}
				Fun2 f2=new Fun2();
				try{
					f2.MySQL_Database_Creation(VideoTable,coun); 
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
}

