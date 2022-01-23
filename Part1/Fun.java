import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedWriter;
import org.jsoup.select.Elements;
public  class Fun{
	public  int Id(String h){/*Function that return the Id from specific line of file*/
		int Number=0;
		for(int i=0;i<h.length();i++){
			if(h.charAt(i)=='|')
				break;
			char charAtpos = h.charAt(i);
			Number=Number*10+charAtpos-'0';
		}
		return Number;
	}
	public String Name(String str){/*Function that return the Name from specific line of file*/
		String new_str="";
		int pos=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='|'){
				pos=i+1;
				break;
			}
		}
		for(int i=pos;i<str.length();i++){
			if(str.charAt(i)=='|')
				break;
			new_str=new_str+str.charAt(i);
		}
		return new_str;
	}
	public String Text(String str){/*Function that return the Text from specific line of file*/
		String new_str="";
		int pos=0;
		int times=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='|'){
				times+=1;
				if(times==2){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<str.length();i++){
			if(str.charAt(i)=='|')
				break;
			new_str=new_str+str.charAt(i);
		}
		return new_str;
	}
	public String Sound_Tag(String str){/*Function that return the Sound_Tag from specific line of file*/
		String new_str="";
		int pos=0;
		int times=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='|'){
				times+=1;
				if(times==3){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<str.length();i++){
			if(str.charAt(i)=='|')
				break;
			new_str=new_str+str.charAt(i);
		}
		return new_str;
	}
	public String Likes_Number(String str){/*Function that return the Likes_Number from specific line of file*/
		String new_str="";
		int pos=0;
		int times=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='|'){
				times+=1;
				if(times==4){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<str.length();i++){
			if(str.charAt(i)=='|')
				break;
			new_str=new_str+str.charAt(i);
		}
		return new_str;
	}
	public String Comments_Number(String str){/*Function that return the Comments_Number from specific line of file*/
		String new_str="";
		int pos=0;
		int times=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='|'){
				times+=1;
				if(times==5){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<str.length();i++){
			if(str.charAt(i)=='|')
				break;
			new_str=new_str+str.charAt(i);
		}
		return new_str;
	}
	public String Shares_Number(String str){/*Function that return the Shares_Number from specific line of file*/
		String new_str="";
		int pos=0;
		int times=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='|'){
				times+=1;
				if(times==6){
					pos=i+1;
					break;
				}
			}
		}
		for(int i=pos;i<str.length();i++){
			if(str.charAt(i)=='|')
				break;
			new_str=new_str+str.charAt(i);
		}
		return new_str;
	}

}
