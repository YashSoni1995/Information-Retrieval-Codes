package InfoRet;

import java.util.ArrayList;
import java.util.HashMap;

public class NonRelVectSum{
	
public double[] NonRelDocSum(HashMap<String, HashMap<String, Integer>> TermFreqHash, HashMap<Integer, String> DocIdNameHash, int[] NonRelDocArray){	


	HashMap<Integer, ArrayList<String>> hash = new HashMap<Integer, ArrayList<String>>();
	ArrayList<String> words = new ArrayList<String>();
		
	//Storing Words associated with each non-relevant documents.
	for(int i=0; i<NonRelDocArray.length; i++){
		for(String word : TermFreqHash.keySet()){
		    if(TermFreqHash.get(word).keySet().contains(DocIdNameHash.get(NonRelDocArray[i]))){
			words.add(word);
		    }
		}
		hash.put(NonRelDocArray[i], words);
	}
		
	int q = 0;
	HashMap<Integer, Integer> Vecc = new HashMap<Integer, Integer>();
	HashMap<Integer, HashMap<Integer, Integer>> Vector = new HashMap<Integer, HashMap<Integer, Integer>>();
	
	//Extracting direct index for each non-relevant document.
	for(int i=0; i<NonRelDocArray.length; i++){
		for(String word : TermFreqHash.keySet()){
			if(TermFreqHash.get(word).get(DocIdNameHash.get(NonRelDocArray[i]))!=null){
				Vecc.put(q, TermFreqHash.get(word).get(DocIdNameHash.get(NonRelDocArray[i])));
			}
			else{
				Vecc.put(q,0);
			}
		q++;
		}
	Vector.put(NonRelDocArray[i], Vecc);
	}
	
	//Calculating final Vector for non-relevant Documents.
	double[] Sum = new double[TermFreqHash.size()];
	for(int i=0; i<TermFreqHash.size(); i++){
		for(int j=0; j<NonRelDocArray.length; j++)
                  Sum[i] = Sum[i] + Vector.get(NonRelDocArray[j]).get(i);                
		}
	
	for(int i=0; i<TermFreqHash.size(); i++){
		Sum[i] = (Sum[i]/NonRelDocArray.length)*0.25;
		}
	return Sum;
	}
}
	