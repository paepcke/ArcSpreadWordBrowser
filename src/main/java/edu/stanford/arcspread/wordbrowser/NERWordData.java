package edu.stanford.arcspread.wordbrowser;

/*
 * Structure to save the word, it's NER tag and the URL associated with it.
 */

public class NERWordData {
	
   /* 
	* Member variables
	*/
	String word;
	String nertag;
	String link;
	
   /*
	* Constructor
	*/
	public NERWordData(String str, String ner, String URL){
		word = str;
		nertag = ner;
		link = URL;
	}
};


