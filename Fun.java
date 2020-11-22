import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedWriter;
import org.jsoup.select.Elements;
public  class Fun{
	public  int Id(String h){
		int Number=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			char charAtpos = h.charAt(i);
			Number=Number*10+charAtpos-'0';
		}
		return Number;
	}
	public String Name(String h){
		String g="";
		int pos=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|'){
				pos=i+1;
				break;
			}
		}
		for(int i=pos;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			g=g+h.charAt(i);
		}
		return g;
	}
	public String Text(String h){
		String g="";
		int pos=0;
		int times=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|'){
				times+=1;
				if(times==2){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			g=g+h.charAt(i);
		}
		return g;
	}
	public String Sound_Tag(String h){
		String g="";
		int pos=0;
		int times=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|'){
				times+=1;
				if(times==3){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			g=g+h.charAt(i);
		}
		return g;
	}
	public String Likes_Number(String h){
		String g="";
		int pos=0;
		int times=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|'){
				times+=1;
				if(times==4){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			g=g+h.charAt(i);
		}
		return g;
	}
	public String Comments_Number(String h){
		String g="";
		int pos=0;
		int times=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|'){
				times+=1;
				if(times==5){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			g=g+h.charAt(i);
		}
		return g;
	}
	public String Shares_Number(String h){
		String g="";
		int pos=0;
		int times=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|'){
				times+=1;
				if(times==6){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			g=g+h.charAt(i);
		}
		return g;
	}

}
