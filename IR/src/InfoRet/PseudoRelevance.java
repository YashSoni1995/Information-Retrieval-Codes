package InfoRet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;


public class PseudoRelevance {

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
		
		System.out.println(" ");
		System.out.print("ENTER YOUR QUERY :"+" ");
		Scanner input = new Scanner(System.in);
		String term = input.nextLine();
		System.out.println(" ");
		String[] terms = term.split("\\s+");
		
		//Calculating TFIDF of each document for the query and Printing top 10 ranked Documents.
		CalcTFIDF TFIDFfinal = new CalcTFIDF();
		TFIDFfinal.TotalTFIDF(terms, list, TermFreqHash, DocIdNameHash);
		
		//Taking input of relevant ranks from User.
		System.out.println(" ");
		
		//Taking first 5 documents as relevant. 
		int[] array = new int[]{1, 2, 3, 4, 5};
		int[] RelDocArray = new int[array.length];
		
			for(int i=0; i<array.length; i++){
				System.out.println(" ");
				RelDocArray[i]=TFIDFfinal.returnRankID().get(array[i]);
			}
		
		//Calculating VectorSum for relevant documents.
		RelVectSum Sum = new RelVectSum();
		double[] RelSum = Sum.RelDocSum(TermFreqHash, DocIdNameHash, RelDocArray);
		
	    ArrayList<Integer> Array = new ArrayList<Integer>();
	    for(int i=0; i<10; i++){
	    	Array.add(i, TFIDFfinal.returnRankID().get(i+1));
	    }
	    
	    //Taking NonRelevant documents leaving the selected relevant documents from top 10 ranked one.
	    for(int i=0; i<RelDocArray.length; i++){
            for(int j=0; j<Array.size(); j++){
	    	  if(RelDocArray[i]==Array.get(j)){
	    		Array.remove(j);
	    	  }
            }
	    }	    
	    
	    int[] NonRelDocArray = new int[(int) (10 - RelDocArray.length)];    
	    for(int i=0; i<NonRelDocArray.length; i++){
	    	NonRelDocArray[i] =  Array.get(i);
	    	}
	    
	    
	    ArrayList<String> lst = new ArrayList<String>();
	    for(int i=0; i<terms.length; i++){
	    	lst.add(terms[i]);
	    }
	
	    //Generating Initial query Vector.
	    int c=0;
	    double[] InitialQueryVector = new double[TermFreqHash.size()]; 
			for(String word : TermFreqHash.keySet()){
				if(lst.contains(word)){
			      InitialQueryVector[c] = 1;
		         }	
				else{
			      InitialQueryVector[c] = 0;
		         }
			c++;
			}	
		
	//Calculating NonRelevantDocuments Vector Sum.
		NonRelVectSum Ssum = new NonRelVectSum();
		double[] nonRelSum = Ssum.NonRelDocSum(TermFreqHash, DocIdNameHash, NonRelDocArray);

	   //Generating NewQuery Vector. 
	   double[] NewQueryVector = new double[TermFreqHash.size()]; 
	   for(int i=0; i<TermFreqHash.size(); i++){
		   NewQueryVector[i] = InitialQueryVector[i] + RelSum[i] - nonRelSum[i];
		 }
	  
	   //Sorting the NewQueryVector.
	  int newQuerySort[] = new int[TermFreqHash.size()];
		for (int i = 0; i < newQuerySort.length; i++) {
			newQuerySort[i]=i;
			}
		
		int temp1;
		double temp2=0;
		int num=newQuerySort.length;
		for (int i = 0; i < ( num - 1 ); i++) {
	      for (int j = 0; j < num - i - 1; j++) {
	    	  if (NewQueryVector[j] < NewQueryVector[j+1]){
	          temp1 = newQuerySort[j];
	          newQuerySort[j] = newQuerySort[j+1];
	          newQuerySort[j+1] = temp1;
	          temp2 = NewQueryVector[j];
	          NewQueryVector[j] = NewQueryVector[j+1];
	          NewQueryVector[j+1] = temp2;
	        }
	      }
	    }
		
		//Taking top 20 terms for newQuery.
		ArrayList<String> TermList = new ArrayList<String>();
		for(String key : TermFreqHash.keySet()){
			TermList.add(key);
		}
	  
		String[] newQuery = new String[20];
		for(int i=0; i<20; i++){
			newQuery[i] = TermList.get(newQuerySort[i]);
			System.out.println(newQuery[i]);
		}
	 
		System.out.println("New Query : ");
		for(int i=0; i<20; i++){
			System.out.print(newQuery[i]+" ");
		}	
		
		//Calculating TFIDF Score and Ranking the documents again with new Query.
		CalcTFIDF newQueryTFIDF = new CalcTFIDF();
		newQueryTFIDF.TotalTFIDF(newQuery, list, TermFreqHash, DocIdNameHash);
		
	}
}
