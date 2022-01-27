import java.io.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriter;
import java.nio.file.Paths;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.FSDirectory;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class Apach{
	public static void main (String[] args) throws Exception{
		IndexWriter writer=writer_creation();
	}
	public static IndexWriter writer_creation() throws Exception{
		String INDEX_DIR="index";
		FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
	    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
	    IndexWriter writer = new IndexWriter(dir, config);
		Connection c=null;
		int num;
		String name;
	    String text;
	    String Play_times;
	    String Link_str;
		try{
			Class.forName("org.postgresql.Driver");
		    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
		}
		catch(Exception e){
			System.out.println(e);
		}
		Statement stmt1=c.createStatement();
	    ResultSet rs=stmt1.executeQuery("select * from TikTokScrapedVideoTable ");
	    while(rs.next()){
	      Document doc = new Document();
	      num = rs.getInt(1);
	      name= rs.getString(2);
	      text = rs.getString(3);
	      Play_times = rs.getString(4);
	      Link_str=rs.getString(5);
	      String num_str=String.valueOf(num);  
	      doc.add(new StringField("id",num_str, Field.Store.YES));
          doc.add(new TextField("name", name, Field.Store.YES));
          doc.add(new TextField("text",text,Field.Store.YES));
          doc.add(new TextField("play_times",Play_times,Field.Store.YES));
          doc.add(new TextField("link_str",Link_str,Field.Store.YES));
          doc.add(new TextField("Source","TikTok",Field.Store.YES));
          writer.addDocument(doc);
	    }
	    writer.close();
	    stmt1.close();
	    c.close();
	    return writer;
	}
}
