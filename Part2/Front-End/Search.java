import java.io.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriter;
import java.nio.file.Paths;
import java.lang.Object;
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
class Search{
    public static void main(String[] args) throws Exception{
        String type_search=args[0];
        String search_arg=args[1];
        IndexSearcher searcher = createSearcher();
        if(type_search.equals("id")){
            QueryParser qp = new QueryParser("id", new StandardAnalyzer());
            Query idQuery = qp.parse(search_arg);
            TopDocs hits = searcher.search(idQuery, 10);
            ScoreDoc[] some1 = hits.scoreDocs;
            String return_value=" ";
            for (int e = 0; e < some1.length; e++) {
                int docId = some1[e].doc;
                Document d = searcher.doc(docId);
                return_value=return_value+d.get("id")+" "+ d.get("name")+ " " + d.get("text") + "----";
            }
            System.out.println(return_value);
        }
        else if(type_search.equals("name")){
            QueryParser qp = new QueryParser("name", new StandardAnalyzer());
            Query idQuery = qp.parse(search_arg);
            TopDocs hits = searcher.search(idQuery, 10);
            ScoreDoc[] some1 = hits.scoreDocs;
            String return_value="";
            for (int e = 0; e < some1.length; e++) {
                int docId = some1[e].doc;
                Document d = searcher.doc(docId);
                return_value=return_value+d.get("id")+" "+ d.get("name")+ " " + d.get("text") + "----";
            }
            return_value = return_value.substring(0, return_value.length() - 1);
            System.out.println(return_value);
        }
        else if(type_search.equals("text")){
            QueryParser qp = new QueryParser("text", new StandardAnalyzer());
            Query idQuery = qp.parse(search_arg);
            TopDocs hits = searcher.search(idQuery, 10);
            ScoreDoc[] some1 = hits.scoreDocs;
            String return_value="";
            for (int e = 0; e < some1.length; e++) {
                int docId = some1[e].doc;
                Document d = searcher.doc(docId);
                return_value=return_value+d.get("id")+" "+ d.get("name")+ " " + d.get("text") + "----";
            }
            System.out.println(return_value);
        }
    }
    private static IndexSearcher createSearcher() throws Exception{	
		String INDEX_DIR="/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Back-End/index";
    	Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
    	IndexReader reader = DirectoryReader.open(dir);
    	IndexSearcher searcher = new IndexSearcher(reader);
    	return searcher;
	}

}
