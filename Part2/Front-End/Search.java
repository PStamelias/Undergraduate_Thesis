import java.io.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
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
            int hits=0;
            int i=Integer.parseInt(search_arg);
            TopDocs foundDocs2 = searchById(i, searcher);
            System.out.println("Total Results :: " + foundDocs2.totalHits);
        }
        else if(type_search.equals("name")){
            int hits=0;
            TopDocs foundDocs2 = searchByName(search_arg, searcher);
            System.out.println("Total Results :: " + foundDocs2.totalHits);
        }
        else if(type_search.equals("Sound_Tag")){
            int hits=0;
            TopDocs foundDocs2 = searchBySoundTag(search_arg, searcher);
            System.out.println("Total Results :: " + foundDocs2.totalHits);
        }
        else if(type_search.equals("date")){
            int hits=0;
            TopDocs foundDocs2 = searchBydate(search_arg, searcher);
            System.out.println("Total Results :: " + foundDocs2.totalHits);
        }
    }

    private static IndexSearcher createSearcher() throws Exception{	
		String INDEX_DIR="/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Back-End/index";
    	Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
    	IndexReader reader = DirectoryReader.open(dir);
    	IndexSearcher searcher = new IndexSearcher(reader);
    	return searcher;
	}

	private static TopDocs searchByName(String Name, IndexSearcher searcher) throws Exception
    {
        System.out.println("mpika edw");
        QueryParser qp = new QueryParser("name",new StandardAnalyzer());
        Query NameQuery = qp.parse(Name);
        TopDocs hits = searcher.search(NameQuery, 100);
        return hits;
    }
 
    private static TopDocs searchById(Integer id, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser("id", new StandardAnalyzer());
        Query idQuery = qp.parse(id.toString());
        TopDocs hits = searcher.search(idQuery, 10);
        return hits;
    }

 	private static TopDocs searchBySoundTag(String Sound_Tag, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser("Sound_Tag", new StandardAnalyzer());
        Query Sound_TagQuery = qp.parse(Sound_Tag);
        TopDocs hits = searcher.search(Sound_TagQuery, 10);
        return hits;
    }

    private static TopDocs searchBydate(String Date, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser("Date", new StandardAnalyzer());
        Query DateQuery = qp.parse(Date);
        TopDocs hits = searcher.search(DateQuery, 10);
        return hits;
    }
}
