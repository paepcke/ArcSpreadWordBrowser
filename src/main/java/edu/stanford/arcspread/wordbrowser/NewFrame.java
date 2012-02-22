package edu.stanford.arcspread.wordbrowser;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

  public class NewFrame{
	   /*
	 	* Member variables
	 	*/
	  	private static JFrame newFrame;
	  	private static JButton alphaButton, lengthButton, exportButton;
	  	private static JTextArea resultArea;
	  	private static List<NERWordData> words;
	  	private static JScrollPane sbrText;
	  	private static JPanel newPanel;
      
		/*
	 	 * Constructor
	 	 */
		public NewFrame(List<NERWordData> wordList) throws Exception{
    	  words = wordList;
    	  newFrame = new JFrame("Results"); 
          newFrame.getContentPane().setLayout(new FlowLayout());
          resultArea = new JTextArea("", 30, 40);
          Iterator<NERWordData> i = words.iterator();
      		while(i.hasNext()){
      			NERWordData val = (NERWordData)i.next();
      			resultArea.append(val.word + "::" + val.nertag + "\n");
      		}	
         
      		newPanel = new JPanel(new GridBagLayout());
      		GridBagConstraints c = new GridBagConstraints();
          
      		resultArea.setLineWrap(true);
      		sbrText = new JScrollPane(resultArea);
      		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      		c.fill = GridBagConstraints.HORIZONTAL;
      		c.gridx = 3;
      		c.gridy = 3;
      		c.gridwidth = 2;
      		newPanel.add(sbrText, c);
        
      		alphaButton = new JButton("Sort Alphabetically");
	     	c.fill = GridBagConstraints.HORIZONTAL;
	        c.gridx = 1;
	        c.gridy = 0;
	        c.gridwidth = 1;
	        newPanel.add(alphaButton, c);
        
	        lengthButton = new JButton("Sort by Length");
	     	c.fill = GridBagConstraints.HORIZONTAL;
	        c.gridx = 2;
	        c.gridy = 0;
	        c.gridwidth = 1;
	        newPanel.add(lengthButton, c);
        
	     	exportButton = new JButton("Export filtered results");
	     	c.fill = GridBagConstraints.HORIZONTAL;
	        c.gridx = 6;
	        c.gridy = 6;
	        c.gridwidth = 1;
	        newPanel.add(exportButton, c);
        
	        //Write to CSV file
	        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {
            	Iterator<NERWordData> i = words.iterator();
            	FileWriter fstream;
    			try {
    				
    				fstream = new FileWriter("output/out.csv");
    				BufferedWriter out = new BufferedWriter(fstream);
    				while(i.hasNext()){
    	        		NERWordData val = (NERWordData)i.next();
    	        		out.append(val.word + "," + val.nertag + "," + val.link + "\n");
    	        	}
    				out.close();
    			} catch (IOException e1) {
    				e1.printStackTrace();
    			}
            	
            }
        });
     	
	        //Sort alphabetically
	        alphaButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	ArrayList<String> alphaWords = new ArrayList<String>();
	            	Iterator<NERWordData> i = words.iterator();
	              	while(i.hasNext()){
	              		NERWordData val = (NERWordData)i.next();
	              		alphaWords.add(val.word + " (" + val.nertag + ")");
	              	}
            	
	              	Collections.sort(alphaWords);
	            	resultArea.setText("");
	            	for(int j=0; j < alphaWords.size(); j++){
	            		resultArea.append(alphaWords.get(j)+"\n");
	            	}
	            	
	            }
	        });
        
	        //Sort by length
	        lengthButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	ArrayList<String> lengthWords = new ArrayList<String>();
	            	ArrayList<String> finalLengthWords = new ArrayList<String>();
	            	Iterator<NERWordData> i = words.iterator();
	              	while(i.hasNext()){
	              		NERWordData val = (NERWordData)i.next();
	              		lengthWords.add(val.word + " (" + val.nertag + ")");
	              	}
	            	
	            	StringLengthComparator comp = new StringLengthComparator();
	            	String [] arr = lengthWords.toArray(new String[lengthWords.size()]);
	            	Arrays.sort(arr, comp);
	            	for(int j=0; j < lengthWords.size(); j++){
	            		finalLengthWords.add(arr[j]);
	            	}
	            	resultArea.setText("");
	            	for(int k=0; k < finalLengthWords.size(); k++){
	            		resultArea.append(finalLengthWords.get(k)+"\n");
	            	}
	            	
	            }
	        });
	     	 
	     	 launchFrame();
	      }
 
		
	/*
	 *  Set and display frame
	 */ 
	public void launchFrame(){ 
        newFrame.add(newPanel);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.pack();
        newFrame.setVisible(true);
     }
     
     
    /*
	 * Comparator for word lengths
	 */
     public class StringLengthComparator implements Comparator<String> {
		  public int compare(String o1, String o2) {
		    if (o1.length() < o2.length()) {
		      return -1;
		    } else if (o1.length() > o2.length()) {
		      return 1;
		    } else {
		      return 0;
		    }
		  }
	}
    
 }