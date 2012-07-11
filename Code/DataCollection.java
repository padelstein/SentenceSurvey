import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import com.amazonaws.mturk.requester.Assignment;
import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.Locale;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.service.exception.ServiceException;


public class DataCollection {

	
	private QualificationRequirement acceptanceRate = new QualificationRequirement("000000000000000000L0", Comparator.GreaterThanOrEqualTo, 95, null, false);
	private QualificationRequirement location = new QualificationRequirement("00000000000000000071", Comparator.EqualTo, null, new Locale("US"), false);
	private QualificationRequirement[] requirements = {acceptanceRate, location};
	
	ArrayList<OurHIT> grammarHits = new ArrayList<OurHIT>();
	ArrayList<OurHIT> contentHits = new ArrayList<OurHIT>();
	ArrayList<OurHIT> simplicityHits = new ArrayList<OurHIT>();
	
	/**
	 * @param args
	 */
	
	public void getAssignmentsHIT(String hitId) throws IOException
	{
		try 
		{
			OurHIT currentHIT = new OurHIT(hitId);

			ArrayList<String>  compareList = null;
			// Print out the HITId and the URL to view the HIT.
			System.out.println("Retrieved HIT: " + hitId + " " + currentHIT.typeID );

			//uses hittypeId to check what kind of hit we are looking at
			if ( currentHIT.typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") ){
				grammarHits.add(currentHIT);
			} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
				contentHITs.add(currentHIT);
				HITindex++;
			} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
				simplicityHITs.add(currentHIT);
			} else {
				return;
			}

			for (Assignment answer: currentHIT.assignments ){
				String workerID = answer.getWorkerId();
//				Worker currentWorker = null;
				boolean alreadyDocumentedWorker = false;
				String answerText = answer.getAnswer();

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

			if (wordToSense != null){
				//				answerOutput.println("The original target word:" + word);
				//				answerOutput.println("The ideal substitution would be: " + wordToSense.get(word)[2]);
			}

			/* 
			 * uses frequency counter to check for the word with the highest frequency and it prints out all words with frequencies >= three
			 * otherwise prints out a list of all submissions and their frequencies. Also takes the most frequent word and records the normal word
			 * and they frequent word for comparison.
			 */
			for ( String text: currentHIT.frequencyCounter.keySet() ){
				int freq = currentHIT.frequencyCounter.get(text);
				if ( freq == currentHIT.highestFreq ){
					compareList.add(text.trim());
				}
			}

		}
		catch (ServiceException e) 
		{
			System.err.println(e.getLocalizedMessage());
		}
	}

	// retrieves HITs from amazon and processes them
	public void getHITs() throws IOException
	{
		try 
		{
			HIT[] HITs = service.getAllReviewableHITs(null);
			for ( HIT amazonHIT: HITs )
			{
				OurHIT currentHIT = new OurHIT(amazonHIT.getHITId());
				ArrayList<String>  compareList = null;
				System.out.println("Retrieved HIT: " + currentHIT.ID + " " + currentHIT.typeID );

				//uses hittypeId to check what kind of hit we are looking at
				if ( currentHIT.typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") ){
					grammarHITs.add(currentHIT);
				} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
					contentHITs.add(currentHIT);
					HITindex++;
				} else if ( currentHIT.typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") ){
					simplicityHITs.add(currentHIT);
				} else {
					continue;
				}

				for (Assignment answer: currentHIT.assignments ){
					String workerID = answer.getWorkerId();
					Worker currentWorker = null;
					boolean alreadyDocumentedWorker = false;
					String answerText = answer.getAnswer();

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

					for (String text: currentHIT.frequencyCounter.keySet())
					{
						int freq = currentHIT.frequencyCounter.get(text);
						if (freq == currentHIT.highestFreq)
						{
							compareList.add(text.trim());
						}
					}
				}
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

				OurHIT currentHIT = new OurHIT();

				if ( typeID.equals("20ASTLB3L0FBPWA8FU5JZEVE5SUJV7") )
				{
					grammarHITs.add(currentHIT);
				} else if ( typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") )
				{
					contentHITs.add(currentHIT);
				} else if ( typeID.equals("25D2JE1M7PKKF8JGAZQAK04LZYTXQE") )
				{
					simplicityHITs.add(currentHIT);
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
				
				app.getHITs();
				
				app.fillHitList(inputFile, 5);

			} catch (IOException e){
				
			}
		}
	}

}
