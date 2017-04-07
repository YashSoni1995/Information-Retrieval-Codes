package InfoRet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class TermPipeline {

	static String StringFile;
	int InitialFreq = 1;
	static String PathName;
		public TermPipeline(String FileToRead, String StopFilePath){
		StringFile = FileToRead;
	    PathName = StopFilePath;
		}
	
		public HashMap<String, Integer> tokenize() throws IOException{
			
		HashMap<String, Integer> StopWords =new HashMap<String, Integer>();
		HashMap<String, Integer> tokenizedWords =new HashMap<String, Integer>();
		
		//Spliting words of the file.
		String[] words = StringFile.split("\\s+");
		System.out.println("");
		
		//Taking input of Stopwords file path. 
		String Path = PathName;
		File StopWordsFilePath = new File(Path);
	    String StopWordsString = FileUtils.readFileToString(StopWordsFilePath);
	    String[] Stop = StopWordsString.split("\\s+");
			
	    //Excluding Stopwords to enter into the HashMap and Storing distinct terms with their frequency.
	    	for(String StopWord : Stop){
	    		StopWords.put(StopWord, 1);
			}
			   for(String word : words){
				  
				   if(!StopWords.containsKey(word)){
					  
					   if(tokenizedWords.containsKey(word)){
						  int frequency = tokenizedWords.get(word)+1;
						  tokenizedWords.put(word, frequency);
					   }
					   else{
						  tokenizedWords.put(word, InitialFreq);
				  	  }
				  }
			   }
			 return tokenizedWords;
	}
}
