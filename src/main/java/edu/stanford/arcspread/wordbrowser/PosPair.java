package edu.stanford.arcspread.wordbrowser;

/*
 * Structure to save the word and its POS tag - used in PartOfSpeechTagger.java 
 */ 

public class PosPair {
	 
   /*
	* Member variables 
	*/
	String word;
	String pos;
	
	 
   /*
	* Constructor 
	*/
	public PosPair(String word, String pos){
		this.word = word.trim();
		this.pos = pos;
	}
	
	
   /*
	* Getter function for the word 
	*/
	public String getWord(){
		return word;
	}
	
	
   /*
	* Getter function for the tag 
	*/
	public  String getPos(){
		return pos;
	}
	
	
   /*
	* Function to check if the words is a proper noun 
	*/
	public boolean isProperNoun(){
		if (pos.startsWith("NNP") || pos.startsWith("NNPS")){
			return true;
		}
		return false; 
	}
	
	
   
}
