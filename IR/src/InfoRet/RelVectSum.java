package InfoRet;

import java.util.ArrayList;
import java.util.HashMap;

public class RelVectSum{
	
public double[] RelDocSum(HashMap<String, HashMap<String, Integer>> TermFreqHash, HashMap<Integer, String> DocIdNameHash, int[] RelDocArray){	

	HashMap<Integer, ArrayList<String>> hash = new HashMap<Integer, ArrayList<String>>();
	ArrayList<String> words = new ArrayList<String>();
	
	//Storing Words associated with each relevant document.
	for(int i=0; i<RelDocArray.length; i++){
			for(String word : TermFreqHash.keySet()){
				if(TermFreqHash.get(word).keySet().contains(DocIdNameHash.get(RelDocArray[i]))){
					words.add(word);
				}
			}
		hash.put(RelDocArray[i], words);
		}
		
	int q = 0;
	HashMap<Integer, Integer> Vecc = new HashMap<Integer, Integer>();
	HashMap<Integer, HashMap<Integer, Integer>> Vector = new HashMap<Integer, HashMap<Integer, Integer>>();
	//Extracting direct index for each relevant document.
	for(int i=0; i<RelDocArray.length; i++){
		for(String word : TermFreqHash.keySet()){
			if(TermFreqHash.get(word).get(DocIdNameHash.get(RelDocArray[i]))!=null){
				Vecc.put(q, TermFreqHash.get(word).get(DocIdNameHash.get(RelDocArray[i])));
			}
			else{
				Vecc.put(q,0);
			}
		q++;
		}
	Vector.put(RelDocArray[i], Vecc);
	}
	
	//Calculating final Vector for relevant Documents.
	double[] Sum = new double[TermFreqHash.size()];
	for(int i=0; i<TermFreqHash.size(); i++){
		for(int j=0; j<RelDocArray.length; j++)
                  Sum[i] = Sum[i] + Vector.get(RelDocArray[j]).get(i);                
	}
	
	for(int i=0; i<TermFreqHash.size(); i++){
		Sum[i] = (Sum[i]/RelDocArray.length)*0.75;
		}
	return Sum;
	}
}
	