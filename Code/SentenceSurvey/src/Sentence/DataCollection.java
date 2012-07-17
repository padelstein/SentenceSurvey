package Sentence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataCollection {

	public ArrayList<SentenceHIT> grammarHITs;
	public ArrayList<SentenceHIT> contentHITs;
	public ArrayList<SentenceHIT> simplicityHITs;

	public DataCollection()
	{
		grammarHITs = new ArrayList<SentenceHIT>();
		contentHITs = new ArrayList<SentenceHIT>();
		simplicityHITs = new ArrayList<SentenceHIT>();
	}
	
	// primes the HITs with original data then updates them with answers from mTurk
	public void fillHitList(File dataFile, File grammarIDFile, File contentIDFile, File simplicityIDFile) throws IOException
	{
		// initialize file readers
		BufferedReader dataReader = new BufferedReader(new InputStreamReader(new FileInputStream( dataFile ) ) );
		BufferedReader grammarIDReader = new BufferedReader(new InputStreamReader(new FileInputStream( grammarIDFile ) ) );
		BufferedReader contentIDReader = new BufferedReader(new InputStreamReader(new FileInputStream( contentIDFile ) ) );
		BufferedReader simplicityIDReader = new BufferedReader(new InputStreamReader(new FileInputStream( simplicityIDFile ) ) );

		// prime the readers
		dataReader.readLine(); // reads the line of hypens
		String grammarID = grammarIDReader.readLine();
		String contentID = contentIDReader.readLine();
		String simplicityID = simplicityIDReader.readLine();

//		while ( data != null )
		for ( int i = 0 ; i < 20 ; i++ )
		{
			String number = dataReader.readLine(); // the number in the data file corresponding to the HIT
			dataReader.readLine(); // reads the word "original"
			String original = dataReader.readLine();
			String option1 = dataReader.readLine();
			String option2 = dataReader.readLine();
			String option3 = dataReader.readLine();
			String option4 = dataReader.readLine();

			// instantiate the HITs
			SentenceHIT newGrammarHIT = new SentenceHIT(original, option1, option2, option3, option4, number);
			SentenceHIT newContentHIT = new SentenceHIT(original, option1, option2, option3, option4, number);
			SentenceHIT newSimplicityHIT = new SentenceHIT(original, option1, option2, option3, option4, number);
			System.out.println("created HITs");
			
			// update them with answers from amazon
			newGrammarHIT.updateWithAmazon(grammarID);
			newContentHIT.updateWithAmazon(contentID);
			newSimplicityHIT.updateWithAmazon(simplicityID);
			
//			newGrammarHIT.approveAllAssignments();
//			newContentHIT.approveAllAssignments();
//			newSimplicityHIT.approveAllAssignments();
			

			grammarHITs.add( newGrammarHIT );
			contentHITs.add( newContentHIT );
			simplicityHITs.add( newSimplicityHIT );

			dataReader.readLine();
			grammarID = grammarIDReader.readLine();
			contentID = contentIDReader.readLine();
			simplicityID = simplicityIDReader.readLine();
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length >=1){
			// Create an instance of this class.
			DataCollection app = new DataCollection();
			File dataFile = null;
			File grammarIDFile = null;
			File contentIDFile = null;
			File simplicityIDFile = null;
			
			
			PrintWriter grammarOUT = new PrintWriter(new FileOutputStream(new File( "grammarOUT.diff.data.txt" )));
			PrintWriter contentOUT = new PrintWriter(new FileOutputStream(new File( "contentOUT.diff.data.txt" )));
			PrintWriter simplicityOUT = new PrintWriter(new FileOutputStream(new File( "simplicityOUT.diff.data.txt" )));
			
			try {
				if (args.length == 4)
				{
					dataFile = new File(args[0]);
					grammarIDFile = new File(args[1]);
					contentIDFile = new File(args[2]);
					simplicityIDFile = new File(args[3]);
				} 
				
				app.fillHitList(dataFile, grammarIDFile, contentIDFile, simplicityIDFile);
				
				grammarOUT.println("----------Grammar Diff Output-------------");
				for (SentenceHIT hit : app.grammarHITs)
				{
					grammarOUT.println( hit.toString() );
				}
				contentOUT.println("----------Content Diff Output-------------");
				for (SentenceHIT hit : app.contentHITs)
				{
					contentOUT.println( hit.toString() );
				}
				simplicityOUT.println("----------Simplicity Diff Output-------------");
				for (SentenceHIT hit : app.simplicityHITs)
				{
					simplicityOUT.println( hit.toString() );
				}
				
				grammarOUT.close();
				contentOUT.close();
				simplicityOUT.close();

			} catch (IOException e){
				System.err.println(e.getLocalizedMessage());
			}
		}
	}

}
