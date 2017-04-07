package InfoRet;

import java.io.File;
import java.util.HashMap;

class CalcTFIDF{
	
	HashMap<Integer, Integer> rank = new HashMap<Integer, Integer>(); 
	
	public void TotalTFIDF(String[] terms, File[] list, HashMap<String, HashMap<String, Integer>> TermFreqHash, HashMap<Integer, String> DocIdNameHash ){
	
		HashMap<String, HashMap<String, Double>>TFIDFhash = new HashMap<String, HashMap<String, Double>>();
	
		for(int j=0; j<terms.length; j++){
			
			int x=0;
			HashMap<String, Double> hh = new HashMap<String, Double>();
			double IDF = Math.log10(list.length/TermFreqHash.get(terms[j]).size());
			
			System.out.println("===========================================================");
		
			System.out.println("IDF for"+" "+terms[j]+":"+" "+IDF);
			
			for(String key : TermFreqHash.get(terms[j]).keySet()){
			
				System.out.println("---------------------------------------------------------");

				int TF = TermFreqHash.get(terms[j]).get(key);
				double TFIDF = TF*IDF;
				System.out.println("TF of"+" "+ terms[j]+" "+"in"+" "+key+":"+" "+TermFreqHash.get(terms[j]).get(key));
                System.out.println("TFIDF of"+" "+key+" "+"for"+" "+terms[j]+":"+" "+TFIDF);

                hh.put(key, TFIDF);	
			}
		
			for(int m=0; m<DocIdNameHash.size(); m++){
				if(!TermFreqHash.get(terms[j]).keySet().contains(DocIdNameHash.get(m))){
					hh.put(DocIdNameHash.get(m), 0.0);
				}
			}
	
			TFIDFhash.put(terms[j],hh);
			System.out.println(" ");
		}
		
		System.out.println(" ");
	
		double[] TFIDFfinal = new double[list.length]; 
		for(int i=0; i<DocIdNameHash.size(); i++ ){
			double ff=0;
			for(int j=0; j<terms.length; j++){
				ff = ff+ (TFIDFhash.get(terms[j]).get(DocIdNameHash.get(i)));
			}
			TFIDFfinal[i] = ff;
		}
		
		 int rankedDocuments[] = new int[list.length];
			for (int i = 0; i < rankedDocuments.length; i++) {
				rankedDocuments[i]=i;
			}
			
		int temp;
		double temp2;
		int num=rankedDocuments.length;
			for (int i = 0; i < ( num - 1 ); i++) {
				for (int j = 0; j < num - i - 1; j++) {
					if (TFIDFfinal[j] < TFIDFfinal[j+1]){
						temp = rankedDocuments[j];
						rankedDocuments[j] = rankedDocuments[j+1];
						rankedDocuments[j+1] = temp;
						temp2 = TFIDFfinal[j];
						TFIDFfinal[j] = TFIDFfinal[j+1];
						TFIDFfinal[j+1] = temp2;
					}
				}
		    }
		
		for(int i=0; i<list.length; i++){
			rank.put(i+1, (rankedDocuments[i]));
		}
		  
		for(int i=0; i<10; i++){
			System.out.println("RANK: "+ (i+1) + " " + "SCORE: "+ TFIDFfinal[i]+" "+"DOCUMENT: " + DocIdNameHash.get((rankedDocuments[i]))+" "+"Document ID :"+" "+rankedDocuments[i]);
		}
	}
	
	public HashMap<Integer, Integer> returnRankID(){
		return rank;
	}

}