package edu.stanford.arcspread.wordbrowser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;


public class ProcessInputFile {
	
   /*
	* Member variables
	*/
	private static String currPara = "", currURL="";
	private static PartOfSpeechTagger postagger = new PartOfSpeechTagger();
	private static List<POSWordData>properList = new ArrayList<POSWordData>();//proper nouns
	private static List<POSWordData>commonList = new ArrayList<POSWordData>();//all words other than proper nouns eg: nouns, adjectives etc
	private static List<NERWordData>nerList = new ArrayList<NERWordData>();//all words in the input file with their NER tags	
	private static Map<String, List<NERWordData>> nerMap = new HashMap<String, List<NERWordData>>();
	
	
   /*
	* Constructor
	*/
	public ProcessInputFile(String csvString) throws Exception{
		parseInputFile(ProcessInputFile.getScanner(csvString));
		createMaps();	
	}
	
	public ProcessInputFile(File csvFile) throws Exception{
		parseInputFile(ProcessInputFile.getScanner(csvFile));
		createMaps();	
	}
	
	public ProcessInputFile(InputStream csvStream) throws Exception{
		parseInputFile(ProcessInputFile.getScanner(csvStream));
		createMaps();	
	}
	
	
   /*
	* Function to perform NER Tagging. Call's the Stanford NLP Group's tagger with a paragraph at a time.
	*/
	static void doNERTagging(String paragraph, String currURL){
		String serializedClassifier = "classifiers/all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier classifier = 
				CRFClassifier.getClassifierNoExceptions(serializedClassifier);

		List<List<CoreLabel>> out = classifier.classify(paragraph);
		for (List<CoreLabel> sentence : out) {
			for (CoreLabel word : sentence) {
				NERWordData nd = new NERWordData(word.word(), word.get(AnswerAnnotation.class), currURL);
				nerList.add(nd);	
			}			
		}
	}
	
	
   /*
	* Function to perform POS Tagging. Call's the Stanford NLP Group's POS tagger.
	*/
	static void doPOSTagging(String paragraph, String currURL) throws IOException{
		List<PosPair> posPairs = postagger.getPosTags(currPara);
		Iterator<PosPair> it = posPairs.iterator();
		while(it.hasNext()){
			PosPair val = (PosPair)it.next();
			POSWordData wd = new POSWordData(val.word, val.pos, currURL);
			if(val.isProperNoun())
				properList.add(wd);
			else
				commonList.add(wd);
	}	
}
	
	
   /*
	* Create's a map of the NER tags to the list of words classified as those tags.
	*/
	static void createMaps(){
		//Map from NER tag to word data
		List<NERWordData>temp;
		Iterator<NERWordData> it = nerList.iterator();
		while(it.hasNext()){
			NERWordData val = (NERWordData)it.next();	
			temp = nerMap.get(val.nertag);
			if (temp == null){
			temp = new ArrayList<NERWordData>();
			temp.add(val);
			nerMap.put(val.nertag, temp);
			}	
			else{
				temp.add(val);
				nerMap.put(val.nertag,temp);
			}
			
		}
		
	}
	
	
   /*
	* Query the map 
	*/
	List<NERWordData> generateResult(String tag){
		List<NERWordData> value = nerMap.get(tag); 
		return value;	   
	}  

	static Scanner getScanner(String csvContent) {
		return new Scanner(csvContent);
	}

	static Scanner getScanner(File csvFile) throws FileNotFoundException {
		return new Scanner(csvFile);
	}
	
	static Scanner getScanner(InputStream csvStream) throws FileNotFoundException {
		return new Scanner(csvStream);
	}
	
   /*
	* Parses each row of the input csv file and calls the POS/NER tagger. 
	*/
	static void parseInputFile(Scanner input) throws Exception{
		String delim = "||";
		
		while(input.hasNext()){
			String word = input.next();
			currPara = currPara + " " + word;
			
			if(word.compareTo(delim) == 0)
			{	currURL = input.next();
				//doPOSTagging(currPara, currURL); 
				doNERTagging(currPara, currURL);
				currPara = "";
				currURL = "";

			}		
		}
	}

};