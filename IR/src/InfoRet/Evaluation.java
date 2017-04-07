package InfoRet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import org.apache.commons.io.FileUtils;


public class Evaluation {
	
	public static void main(String[] args) throws IOException {
		
		HashMap<String, HashMap<String, Integer>> TermFreqHash =new HashMap<String, HashMap<String,Integer>>();
		HashMap<Integer, String> DocIdNameHash = new HashMap<Integer, String>();
		
		int DocId = 0;
		int count = 0;
		String DocName;
		
		//Taking Input of Document Directory from User.
		System.out.print("Enter Directory Path : ");
		Scanner DirectoryName = new Scanner(System.in);
		String Directory = DirectoryName.next();
		File dir = new File(Directory);
		File[] list= dir.listFiles();
		System.out.println(" ");
		System.out.print("Enter Stopwords file Path : " + " ");
		Scanner Stop = new Scanner(System.in);
		String StopFilePath = Stop.next();
		
		//Reading and Indexing Files.
		for (File file : list) {
			System.out.println("Reading and Indexing File :" + count++);
			DocName = file.getName();
			DocIdNameHash.put(DocId, DocName);
			String readFile=FileUtils.readFileToString(file);
			
		  //Tokenizing terms from current file.
			TermPipeline getTerms=new TermPipeline(readFile, StopFilePath);
		    HashMap<String, Integer> words=getTerms.tokenize();
		 
		    //Creating inverted index.
		    for(String term : words.keySet()){
		    	HashMap<String, Integer> DocFreqHash=new HashMap<String,Integer>();
		    	if(TermFreqHash.containsKey(term)){
		    		int freq = words.get(term);
		    		TermFreqHash.get(term).put(DocName, freq);
		    	}
		    	else{
		    		DocFreqHash.put(DocName, words.get(term));
		            TermFreqHash.put(term, DocFreqHash);
		        }
		     }
		   DocId = DocId+1;
		}
	
		System.out.println("Unique Terms appearing throughout all documents : ");
		
		//Printing all the Unique Terms.
		for(String key : TermFreqHash.keySet()){
			System.out.println(key+" ");
		}
        
		System.out.println(" ");
		System.out.println("*****INDEXING DONE*****");
		System.out.println(" ");
		System.out.println( "Total terms in inverted index : " + TermFreqHash.size());
		
		System.out.println(" ");
		System.out.print("ENTER YOUR QUERY :"+" ");
		Scanner input = new Scanner(System.in);
		String term = input.nextLine();
		System.out.println(" ");
		String[] terms = term.split("\\s+");
		
		//Calculating TFIDF of each document for the query and Printing top 10 ranked Documents.
		CalcTFIDF TFIDFfinal = new CalcTFIDF();
		TFIDFfinal.TotalTFIDF(terms, list, TermFreqHash, DocIdNameHash);
	
		//Taking Query File
		File QFile = new File("/home/yashraj/Desktop/query_HINDI.txt");
		String QueryFile = FileUtils.readFileToString(QFile);
	    String[] SplitIdQuery = QueryFile.split("-");
	    HashMap<Integer, String> QueryHashMap = new HashMap<Integer, String>();
	    
	    int QueryNo = 25;
	    for(int i=0; i<SplitIdQuery.length; i++){
	    	if(i%2!=0){
	    		QueryHashMap.put(QueryNo, SplitIdQuery[i]);
	    		QueryNo= QueryNo-1;
	    	} 
	    }
	    
	    //Taking qrel file.
	    System.out.println(" ");
	    System.out.print("Enter Path of Relevance Judgement File:"+" ");
	    Scanner in = new Scanner(System.in);
	    String RelJudgementFile = in.next();
	    File file = new File(RelJudgementFile);
	    String File2 = FileUtils.readFileToString(file);
	    String[] SplitQrel = File2.split("\\s+");
	    int l = SplitQrel.length/4;
 
	    HashMap<Integer, String> QueryRel = new HashMap<Integer, String>();
	    for(int i=0; i<25; i++){
	    	if(i<9){
	    		QueryRel.put(i+1, "english-document-0000"+(i+1)+".txt");
	    	} 
	    	else{
	    		QueryRel.put(i+1, "english-document-000"+(i+1)+".txt");
	    	}
    	}
    
	    HashMap<String, HashMap<String, String>> QrelHash = new HashMap<String, HashMap<String, String>>();
	    for(int i=0; i<25; i++){
	    	HashMap<String, String> RelJud = new  HashMap<String, String>();
	    	int j=0;
	    		while(j<(SplitQrel.length-4)){
	    			if(QueryRel.get(i+1).equals(SplitQrel[j])){
	    				RelJud.put(SplitQrel[j+2], SplitQrel[j+3]);
	    			}
	    			j = j+4;
	    		}
	    		QrelHash.put(QueryRel.get(i+1), RelJud);
	    }
	    
	    for(int i=0; i<25; i++){
	    	if(i<9){
	    		QrelHash.get("english-document-0000"+(i+1)+".txt");
	    	} 
	    	else{
	    		QrelHash.get("english-document-000"+(i+1)+".txt");
	    	}
     	}
     
	    int QueryId = 0;
	    for(Integer key : QueryHashMap.keySet()){
	    	if(term.equals(QueryHashMap.get(key))){  //'term' is the actual query entered by user.
	    		QueryId = key  ;
	    	}
	    }
        
        //Taking Relevant Documents from Judgement file into an ArrayList for the input query. 
     	ArrayList<String> RelDocs = new ArrayList<String>();
     	HashMap<String, String> New = QrelHash.get(QueryRel.get(QueryId));
     	for(String key : New.keySet()){
    		 int w = Integer.parseInt(New.get(key));
    		 if(w>0){
    			  RelDocs.add(key);
    	    }
    	 }
     
     	/* for(int i=0; i<RelDocs.size(); i++){
    	 System.out.println(RelDocs.get(i));
       } */
     	
     	//Calling CalcTFIDF class and its method returnRankID.
     	HashMap<Integer, Integer> Ranking = TFIDFfinal.returnRankID();
     	
     	//Storing Ranked Docs. Name and ID.
     	ArrayList<String> RankDocs = new ArrayList<String>();
     	for(int i=0; i<20; i++){
     		RankDocs.add(DocIdNameHash.get(Ranking.get(i+1)));
     	}
     	
     	//Checking whether retrieved Rank Documents are Relevant or not.
     	double cnt = 0;
     	for(int i=0; i<RelDocs.size(); i++){
     		for(int j=0; j<RankDocs.size(); j++){
     			if(RelDocs.get(i).equals(RankDocs.get(j))){
     				cnt++;
     			}
     		}
     	}
     
     	//Calculating and Printing Precision and Recall.
     	System.out.println(" ");
     	System.out.println("**********EVALUATION of IR System********");
     	System.out.println(" ");
     	System.out.println("For the query entered on this IR System: ");
     	System.out.println(" ");
     	System.out.println("Total Documents Retrieved:"+20);
     	System.out.println("Actual Total Relevant Documents:"+RelDocs.size());
     	System.out.println("Total Documents Retrieved which are Relevant:"+cnt);
     	double pre = (cnt/20);
     	double rec = (cnt/RelDocs.size());
     	System.out.println("Precision"+":"+pre);
     	System.out.println("Recall"+":"+rec);
     }
}
// /home/yashraj/Desktop/hindi1
// /home/yashraj/Stop/Stopwords_hindi.txt

