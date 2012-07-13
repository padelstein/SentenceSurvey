package src.Sentence;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import com.amazonaws.mturk.requester.Assignment;
import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.Locale;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.service.axis.AWSService;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;


public class DataCollection {
	private RequesterService service;
	
	private QualificationRequirement acceptanceRate = new QualificationRequirement("000000000000000000L0", Comparator.GreaterThanOrEqualTo, 95, null, false);
	private QualificationRequirement location = new QualificationRequirement("00000000000000000071", Comparator.EqualTo, null, new Locale("US"), false);
	private QualificationRequirement[] requirements = {acceptanceRate, location};
	
	ArrayList<SentenceHIT> grammarHits = new ArrayList<SentenceHIT>();
	ArrayList<SentenceHIT> contentHits = new ArrayList<SentenceHIT>();
	ArrayList<SentenceHIT> simplicityHits = new ArrayList<SentenceHIT>();
	
	/**
	 * @param args
	 */
	
	public DataCollection(){
		service = new RequesterService(new PropertiesClientConfig());
	}
	
	
	public void getAssignmentsHIT(String hitId) throws IOException
	{
		try 
		{
			SentenceHIT currentHIT = new SentenceHIT(hitId, "","","","");

			ArrayList<String>  compareList = null;
			// Print out the HITId and the URL to view the HIT.
			System.out.println("Retrieved HIT: " + hitId + " " + currentHIT.typeID );

			//uses hittypeId to check what kind of hit we are looking at
			if ( currentHIT.typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") ){
				grammarHits.add(currentHIT);
			} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
				contentHits.add(currentHIT);
			} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
				simplicityHits.add(currentHIT);
			} else {
				return;
			}

			for (String answer: currentHIT.answers.keySet() ){
//				Worker currentWorker = null;
				boolean alreadyDocumentedWorker = false;

				// checks if we have already documented the worker
//				for ( Worker i: workers ){
//					if ( i.workerId.equals(workerID) ){
//						currentWorker = i;
//						alreadyDocumentedWorker = true;
//						break;
//					} 
//				}
				// adds the answer to the worker
//				if ( alreadyDocumentedWorker ){
//					currentWorker.addAnswer(answerText, currentHIT.typeID, HITindex);
//				} else {
//					currentWorker = new Worker(workerID);
//					workers.add(currentWorker);
//					currentWorker.addAnswer(answerText, currentHIT.typeID, HITindex);
//				}
			}

			/* 
			 * uses frequency counter to check for the word with the highest frequency and it prints out all words with frequencies >= three
			 * otherwise prints out a list of all submissions and their frequencies. Also takes the most frequent word and records the normal word
			 * and they frequent word for comparison.
			 */

		}
		catch (ServiceException e) 
		{
			System.err.println(e.getLocalizedMessage());
		}
	}

	// retrieves HITs from amazon and processes them
	public void getHITs(File hitIDs) throws IOException
	{
		try 
		{
			BufferedReader hitReader = new BufferedReader(new InputStreamReader(new FileInputStream(hitIDs)));
			String hitId = hitReader.readLine();
			ArrayList<HIT> HITs = new ArrayList<HIT>();
			while (hitId != null){
				HITs.add(service.getHIT(hitId));
				hitId = hitReader.readLine();
			}
			for ( HIT amazonHIT: HITs )
			{
				SentenceHIT currentHIT = new SentenceHIT(amazonHIT.getHITId());
				System.out.println("Retrieved HIT: " + currentHIT.ID + " " + currentHIT.typeID );

				//uses hittypeId to check what kind of hit we are looking at
				if ( currentHIT.typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") ){
					grammarHits.add(currentHIT);
				} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
					contentHits.add(currentHIT);
				} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
					simplicityHits.add(currentHIT);
				} else {
					continue;
				}

				
					// checks if we have already documented the worker
//					for ( Worker i: workers ){
//						if ( i.workerId.equals(workerID) ){
//							currentWorker = i;
//							alreadyDocumentedWorker = true;
//							break;
//						} 
//					}
					// adds the answer to the worker
//					if ( alreadyDocumentedWorker ){
//						currentWorker.addAnswer(answerText, currentHIT.typeID, HITindex);
//					} else {
//						currentWorker = new Worker(workerID);
//						workers.add(currentWorker);
//						currentWorker.addAnswer(answerText, currentHIT.typeID, HITindex);
//					}
				}
		}
		catch (ServiceException e) 
		{
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	public void fillHitList(File input, int numAssignments) throws IOException{
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
		if (fileReader != null){
			String inputLine = fileReader.readLine();
			while (inputLine != null){
				String[] words = inputLine.split("\t");
				ArrayList<String> answers = new ArrayList<String>(10);
				String typeID = "";
				for ( int i = 3; i < 3 + numAssignments ; i++)
				{
					if ( i < words.length )
					{
						answers.add(words[i].trim());
					}
				}

				typeID = words[1];

				SentenceHIT currentHIT = new SentenceHIT("","","","","");

				if ( typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") )
				{
					grammarHits.add(currentHIT);
				} else if ( typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") )
				{
					contentHits.add(currentHIT);
				} else if ( typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") )
				{
					simplicityHits.add(currentHIT);
				}

				inputLine = fileReader.readLine();
			}
		}
	}
	
	public static void main(String[] args) {
		if (args.length >=1){
			// Create an instance of this class.
			DataCollection app = new DataCollection();
			File inputFile = null;

			try {
				if (args.length>1)
					inputFile = new File(args[1]);
				
				app.getHITs(new File(args[0]));
				
				app.fillHitList(inputFile, 5);

			} catch (IOException e){
				
			}
		}
	}

}
