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
class YouApache{
	public static void main (String[] args) throws Exception{
		IndexWriter writer=writer_creation();
	}
	public static IndexWriter writer_creation() throws Exception{
		String INDEX_DIR="index";
		FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
	    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
	    IndexWriter writer = new IndexWriter(dir, config);
		Connection c=null;
		int id;
		String name;
	    String creator;
	    String description;
	    String views;
	    String dateCreation;
		try{
			Class.forName("org.postgresql.Driver");
		    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prokopis","prokopis","123");
		}
		catch(Exception e){
			System.out.println(e);
		}
		Statement stmt1=c.createStatement();
	    ResultSet rs=stmt1.executeQuery("select * from Youtube ");
	    while(rs.next()){
	    	Document doc = new Document();
	      	id = rs.getInt(1);
	      	name= rs.getString(2);
	      	creator = rs.getString(3);
	      	description=rs.getString(4);
	      	views=rs.getString(5);
	      	dateCreation=rs.getString(6);
	      	String s=String.valueOf(id);    
	      	doc.add(new StringField("id",s, Field.Store.YES));
          	doc.add(new TextField("name", name, Field.Store.YES));
          	doc.add(new TextField("creator",creator,Field.Store.YES));
          	doc.add(new TextField("description",description,Field.Store.YES));
          	doc.add(new TextField("views",views,Field.Store.YES));
          	doc.add(new TextField("dateCreation",dateCreation,Field.Store.YES));
          	doc.add(new TextField("Source","Youtube",Field.Store.YES));
          	writer.addDocument(doc);
	    }
	    writer.close();
	    stmt1.close();
	    c.close();
	    return writer;
	}
}