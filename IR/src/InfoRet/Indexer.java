package InfoRet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import org.apache.commons.io.FileUtils;


public class Indexer {

	public static void main(String[] args) throws IOException {
		
		HashMap<String, HashMap<String, Integer>> TermFreqHash =new HashMap<String, HashMap<String,Integer>>();
		HashMap<Integer, String> DocIdNameHash = new HashMap<Integer, String>();
		
		int DocId = 0;
		int count = 0;
		String DocName;
		
		//Taking Input of Document Directory from User.
		System.out.print("Enter Directory Path : " +" ");
		Scanner DirectoryName = new Scanner(System.in);
		String Directory = DirectoryName.next();
		File dir = new File(Directory);
		File[] list= dir.listFiles();
		
		System.out.print("Enter Stopwords file Path : " + " ");
		Scanner Stop = new Scanner(System.in);
		String StopFilePath = Stop.next();
		//Reading and Indexing Files.
		for (File file : list) {
			
			DocName = file.getName();
			DocIdNameHash.put(DocId, DocName);
			String readFile=FileUtils.readFileToString(file);
		  
			//Tokenizing terms from current file.
			TermPipeline getTerms=new TermPipeline(readFile, StopFilePath);
		    HashMap<String, Integer> words=getTerms.tokenize();
		 
		    //Creating inverted index.
		    System.out.println("Reading and Indexing File :" + count++);
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
	}
}
