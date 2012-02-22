package edu.stanford.arcspread.wordbrowser;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

 public class Homepage{
	 
	/*
	 * Member variables 
	 */
	 private static JFrame mainFrame;
	 private static JButton resultButton;
	 private static JTextArea docArea; 
	 private static JLabel filterLabel, docLabel;
	 private static JCheckBox filter1;
 	 private static JCheckBox filter2;
 	 private static JCheckBox filter3;
	 private static JScrollPane sbrText;
	 private static CheckBoxListener myListener = null;
	 private static JPanel finalPane;
	 private static Highlighter.HighlightPainter HPainter;  
 	
	 private static List<NERWordData>filter1List = new ArrayList<NERWordData>();
	 private static List<NERWordData>filter2List = new ArrayList<NERWordData>();
	 private static List<NERWordData>filter3List = new ArrayList<NERWordData>();
	 private static List<NERWordData>resList = new ArrayList<NERWordData>();
	 private static ProcessInputFile pr;
 	 
 	/*
 	 * Constructor
 	 */
      public Homepage(){ 
    	  mainFrame = new JFrame("WordBrowser"); 
      	  mainFrame.getContentPane().setLayout(new FlowLayout());
      	  docArea = new JTextArea("", 30, 40);
      	  docArea.setEditable(false);
      	
      	try {
			String textLine;
	        FileReader fr = new FileReader("input/crawl.csv");
	        BufferedReader reader = new BufferedReader(fr);
	        while((textLine=reader.readLine())!=null)
	             docArea.append(textLine);   

	        }
	        catch (IOException ioe) {
	        System.err.println(ioe);
	        System.exit(1);
	        }
          
      	finalPane = new JPanel(new GridBagLayout());
      	GridBagConstraints c = new GridBagConstraints();
          
      	docArea.setLineWrap(true);
        sbrText = new JScrollPane(docArea);
        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        finalPane.add(sbrText, c);
            
        resultButton = new JButton("Results");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 6;
        c.gridy = 6;
        c.gridwidth = 1;
        finalPane.add(resultButton, c);
        
  		filterLabel = new JLabel("FILTERS:");
  		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        finalPane.add(filterLabel, c);
        
  		docLabel = new JLabel("Input document");
  		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 4;
        c.gridwidth = 1;
        finalPane.add(docLabel, c);
        
  		filter1 = new JCheckBox("PERSON");
  		filter1.setOpaque(true);
  		filter1.setBackground(Color.yellow);
  		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        finalPane.add(filter1, c);
        
  		filter2 = new JCheckBox("LOCATION");
  		filter2.setOpaque(true);
  		filter2.setBackground(Color.green);
  		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        finalPane.add(filter2, c);
        
  		filter3 = new JCheckBox("ORGANIZATION");
  		filter3.setOpaque(true);
  		filter3.setBackground(Color.cyan);
  		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        finalPane.add(filter3, c);
        
  		myListener = new CheckBoxListener();
    	
    	filter1.addItemListener(myListener);
    	filter2.addItemListener(myListener);
    	filter3.addItemListener(myListener);
    		
    	
    	resultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
					new NewFrame(resList);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });
    	
      }
 
      
    /*
  	 * Set and display the frame
  	 */ 
     public void launchFrame(){ 
    	 mainFrame.add(finalPane);
         mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         mainFrame.pack(); 
         mainFrame.setVisible(true);
     }
    
     
    /*
   	 * Listener for filters
   	 */ 
     class CheckBoxListener implements ItemListener {
         public void itemStateChanged(ItemEvent e) {
             Object source = e.getSource();
             if (source == filter1) {
                 if (e.getStateChange() == ItemEvent.SELECTED){
                    filter1List = pr.generateResult("PERSON");
                 }
                 else {
                 	filter1List = null;
                 }
                 	
                 
             } else if (source == filter2) {
                 if (e.getStateChange() == ItemEvent.SELECTED){
                     filter2List = pr.generateResult("LOCATION");
                     }
                  else {
                  	filter2List = null;
                  	}
                 
             } else if (source == filter3) {
                 if (e.getStateChange() == ItemEvent.SELECTED){
                     filter3List = pr.generateResult("ORGANIZATION");
                     }
                  else {
                  	filter3List = null;
                  	}
                
             } 
            resList.clear();
         	if(filter1List!= null)
         	resList.addAll(filter1List);
         	if(filter2List!= null)
         	resList.addAll(filter2List);
         	if(filter3List!= null)
         	resList.addAll(filter3List);
         	refreshHighlight();
         }
     }
     
     
    /*
   	 * Display highlight based on filters selected
   	 */ 
     public void refreshHighlight(){
    	 removeHighlights(docArea);
    	 Iterator<NERWordData> it = resList.iterator();
 		 while(it.hasNext()){
 			NERWordData val = (NERWordData)it.next();	
 			if (val.nertag.compareTo("PERSON") == 0){
 				HPainter = new HighlightPainter(Color.yellow);
 					highlight(docArea, val.word);}
 			else if (val.nertag.compareTo("ORGANIZATION") == 0){
 				HPainter = new HighlightPainter(Color.cyan);
 					highlight(docArea, val.word);}
 			else if (val.nertag.compareTo("LOCATION") == 0){
 				HPainter = new HighlightPainter(Color.green);
 					highlight(docArea, val.word);}
 			
 		 }
     }
     
    
    /*
   	 * Highlighting function
   	 */ 
     public void highlight(JTextComponent textComp, String word) {
 	    
 	    try {
 	        Highlighter h = docArea.getHighlighter();
 	        Document doc = docArea.getDocument();
 	        String text = doc.getText(0, doc.getLength());
 	        int pos = 0;

 	        // Search for pattern
 	        while ((pos = text.indexOf(word, pos)) >= 0) {
 	            h.addHighlight(pos, pos+word.length(), HPainter);
 	            pos += word.length();
 	        }
 	    } catch (BadLocationException e) {
 	    }
 	}

    
    /*
   	 * Remove highlights
   	 */  
 	 public void removeHighlights(JTextComponent textComp) {
 	    Highlighter hilite = textComp.getHighlighter();
 	    Highlighter.Highlight[] hilites = hilite.getHighlights();

 	    for (int i=0; i<hilites.length; i++) {
 	        if (hilites[i].getPainter() instanceof HighlightPainter) {
 	            hilite.removeHighlight(hilites[i]);
 	        }
 	    }
 	}


 	/*
  	 * Highlight painter
  	 */ 
 	 class HighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
 	    public HighlightPainter(Color color) {
 	        super(color);
 	    }
 	}
	
     
 	/*
   	 * MAIN
   	 */ 
     public static void main(String args[]) throws Exception{
    	 if (args.length == 0) {
    		 System.out.println("Usage java Homepage <csv input file>");
    		 System.exit(-1);
    		 
    	 }
    	 pr = new ProcessInputFile(new File("input/crawl.csv"));
    	 Homepage page = new Homepage();
         page.launchFrame();
    }
 }