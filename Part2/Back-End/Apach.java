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
	    String Sound_Tag;
	    String text;
	    String date;
	    String LIKES_NUMBER;
	    String COMMENTS_NUMBER;
	    String SHARES_NUMBER;
		try{
			Class.forName("org.postgresql.Driver");
		    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
		}
		catch(Exception e){
			System.out.println(e);
		}
		Statement stmt1=c.createStatement();
	    ResultSet rs=stmt1.executeQuery("select * from TikTokVideoDataTable ");
	    while(rs.next()){
	      Document doc = new Document();
	      num = rs.getInt(1);
	      name= rs.getString(2);
	      text = rs.getString(3);
	      Sound_Tag=rs.getString(4);
		  LIKES_NUMBER=rs.getString(5);
		  COMMENTS_NUMBER=rs.getString(6);
		  SHARES_NUMBER=rs.getString(7);      
	      date=rs.getString(8);
	      String y=String.valueOf(num);  
	      doc.add(new StringField("id",y, Field.Store.YES));
          doc.add(new TextField("name", name, Field.Store.YES));
          doc.add(new TextField("text",text,Field.Store.YES));
          doc.add(new TextField("sound_Tag",Sound_Tag,Field.Store.YES));
          doc.add(new TextField("likes_number",LIKES_NUMBER,Field.Store.YES));
          doc.add(new TextField("comments_number",COMMENTS_NUMBER,Field.Store.YES));
          doc.add(new TextField("shares_number",SHARES_NUMBER,Field.Store.YES));
          doc.add(new TextField("date",date,Field.Store.YES));
          doc.add(new TextField("Source","TikTok",Field.Store.YES));
          writer.addDocument(doc);
	    }
	    writer.close();
	    stmt1.close();
	    c.close();
	    return writer;
	}
}
