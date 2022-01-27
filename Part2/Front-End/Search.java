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
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
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
        String search_arg=args[0];
        int max_val = Integer.MAX_VALUE;
        String return_value="";
        IndexSearcher searcherYoutube = createSearcherYoutube();
        IndexSearcher searcherTikTok  = createSearcherTikTok();
        Analyzer analyzerYoutube = new StandardAnalyzer();
        Analyzer analyzerTikTok = new StandardAnalyzer();
        MultiFieldQueryParser queryParserTikTok = new MultiFieldQueryParser(new String[]{"name","text","Play_times"},analyzerTikTok);
        MultiFieldQueryParser queryParserYoutube = new MultiFieldQueryParser(new String[]{"name","creator","description","views","dateCreation"},analyzerYoutube);
        Query q = queryParserYoutube.parse(search_arg);
        TopDocs hits = searcherYoutube.search(q,max_val);
        ScoreDoc[] some1 = hits.scoreDocs;
        for (int e = 0; e < some1.length; e++) {
            int docId = some1[e].doc;
            Document d = searcherYoutube.doc(docId);
            return_value=return_value+d.get("name") +","+d.get("Source")+","+d.get("creator")+","+d.get("dateCreation")+"\n"+"~~";
        }
        Query q2 = queryParserTikTok.parse(search_arg);
        TopDocs hits2 = searcherTikTok.search(q2,max_val);
        ScoreDoc[] some2 = hits2.scoreDocs;
        for (int e = 0; e < some2.length; e++) {
            int docId = some2[e].doc;
            Document d = searcherTikTok.doc(docId);
            return_value=return_value+d.get("name")+","+d.get("text")+","+d.get("Play_times")+","+d.get("Source")+"~~";
        }
        System.out.println(return_value);
    }
    private static IndexSearcher createSearcherTikTok() throws Exception{	
		String INDEX_DIR="/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Back-End/index";
    	Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
    	IndexReader reader = DirectoryReader.open(dir);
    	IndexSearcher searcher = new IndexSearcher(reader);
    	return searcher;
	}
    private static IndexSearcher createSearcherYoutube() throws Exception{   
        String INDEX_DIR="/home/prokopis/Desktop/Undergraduate_Thesis/Part2/Back-End-Youtube/index";
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
}
