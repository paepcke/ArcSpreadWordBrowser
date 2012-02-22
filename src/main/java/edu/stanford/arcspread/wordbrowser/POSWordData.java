package edu.stanford.arcspread.wordbrowser;

/*
 * Structure to save the word, it's POS tag and the URL associated with it.
 */


public class POSWordData {
	
   /*
	* Member variables 
	*/
	String word;
	String postag;
	String link;
	
   /*
	* Constructor
	*/
	public POSWordData(String str, String pos, String URL){
		word = str;
		postag = pos;
		link = URL;
	}
};
