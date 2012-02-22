/**
 * 
 */
package edu.stanford.arcspread.wordbrowser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.pigir.webbase.DistributorContact;
import edu.stanford.pigir.webbase.WbRecord;
import edu.stanford.pigir.webbase.wbpull.webStream.BufferedWebStreamIterator;


/**
 * @author paepcke
 *
 */
public class WordBrowserTester {

	private class UrlPagePair {
		String url = null;
		String page = null;
		
		public UrlPagePair(String theUrl, String thePage) {
			url = theUrl;
			page = thePage;
		}
	}
	/**
	 * @param crawlName
	 * @param numPages
	 * @throws IOException 
	 */
	
	public ArrayList<UrlPagePair> getURLPageList(String crawlName, int numPages) throws IOException {
		ArrayList<UrlPagePair> res = new ArrayList<UrlPagePair>(); 
		System.out.println("Getting distributor contact.");
		DistributorContact distrCont = DistributorContact.getCrawlDistributorContact(crawlName, numPages);
		System.out.println("Getting Web stream iterator with " +
							distrCont.getDistributorMachineName() +
							":" + distrCont.getDistributorPortAsStr());
		BufferedWebStreamIterator webIt = new BufferedWebStreamIterator(distrCont);
		
		while (webIt.hasNext()) {
			System.out.println("Request next Web page...");
			WbRecord rec = webIt.next();
			String url = rec.getMetadata().getURLAsString();
			res.add(new UrlPagePair(url, rec.getContentUTF8()));
		}
		return res;
	}
	
	/**
	 * @param args: Crawl name and number of pages. If empty, uses a default with 10 pages.
	 */
	public static void main(String[] args) {
		String usage = "Usage: WordBrowserTester <crawlName> <numPages>";
		if (args.length < 2) {
			System.out.println(usage);
			return;
		}
		WordBrowserTester tester = new WordBrowserTester();
		ArrayList<UrlPagePair> crawlContent = null;
		String crawlName = args[0];
		int numPages     = -1;
		try {
			numPages     = Integer.parseInt(args[1]);
			System.out.println("Testing with " + crawlName + " for " + args[1] + " pages...");
			crawlContent = tester.getURLPageList(crawlName, numPages); 
		} catch (NumberFormatException e) {
			System.out.println("Expected int in second argument. " + usage);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		File tmpFile = null;
		FileWriter tmpFileWriter = null;
		BufferedWriter tmpWriter = null;
		try {
			// Create temp file.
			tmpFile = File.createTempFile("wordBrowserTest", ".csv");
			tmpFileWriter = new FileWriter(tmpFile);
			tmpWriter = new BufferedWriter(tmpFileWriter);
		} catch (IOException e) {
			System.out.println("Could not create temp file.");
			return;
		}
		// Delete temp file when program exits.
		tmpFile.deleteOnExit();
		
		try {
			for (UrlPagePair pair : crawlContent) {
				//System.out.println(pair.page + "||" + pair.url + "\n");
				tmpWriter.write(pair.page + "||" + pair.url + "\n");
			}
			tmpWriter.close();
		} catch (IOException e) {
			System.out.println("Could not write to temp file " + tmpFile.getName());
			return;
		}
		
		try {
			ProcessInputFile pr = new ProcessInputFile(tmpFile);
			Homepage page = new Homepage();
			page.launchFrame();
		} catch (Exception e) {
			System.out.println("Could not run ProcessInputFile(): " + e.getMessage());
		}
		
	}
}
