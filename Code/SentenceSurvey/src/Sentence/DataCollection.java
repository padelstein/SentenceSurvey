package Sentence;

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

	ArrayList<SentenceHIT> grammarHITs = new ArrayList<SentenceHIT>();
	ArrayList<SentenceHIT> contentHITs = new ArrayList<SentenceHIT>();
	ArrayList<SentenceHIT> simplicityHITs = new ArrayList<SentenceHIT>();

	public void fillHitList(File dataFile, File grammarIDFile, File contentIDFile, File simplicityIDFile) throws IOException
	{
		BufferedReader dataReader = new BufferedReader(new InputStreamReader(new FileInputStream( dataFile ) ) );
		BufferedReader grammarIDReader = new BufferedReader(new InputStreamReader(new FileInputStream( grammarIDFile ) ) );
		BufferedReader contentIDReader = new BufferedReader(new InputStreamReader(new FileInputStream( contentIDFile ) ) );
		BufferedReader simplicityIDReader = new BufferedReader(new InputStreamReader(new FileInputStream( simplicityIDFile ) ) );

		String data = dataReader.readLine();
		String grammarID = grammarIDReader.readLine();
		String contentID = contentIDReader.readLine();
		String simplicityID = simplicityIDReader.readLine();

		while ( data != null )
		{
			String number = dataReader.readLine(); // the number in the data file corresponding to the HIT
			data = dataReader.readLine();
			String original = dataReader.readLine();
			String option1 = dataReader.readLine();
			String option2 = dataReader.readLine();
			String option3 = dataReader.readLine();
			String option4 = dataReader.readLine();

			SentenceHIT newHIT = new SentenceHIT(original, option1, option2, option3, option4, number);

			grammarHITs.add(newHIT);
			contentHITs.add(newHIT);
			simplicityHITs.add(newHIT);

			data = dataReader.readLine();
			grammarID = grammarIDReader.readLine();
			contentID = contentIDReader.readLine();
			simplicityID = simplicityIDReader.readLine();
		}
	}

	public static void main(String[] args) {
		if (args.length >=1){
			// Create an instance of this class.
			DataCollection app = new DataCollection();
			File dataFile = null;
			File grammarIDFile = null;
			File contentIDFile = null;
			File simplicityIDFile = null;

			try {
				if (args.length>1)
				{
					dataFile = new File(args[0]);
					grammarIDFile = new File(args[1]);
					contentIDFile = new File(args[2]);
					simplicityIDFile = new File(args[3]);
				}
				app.fillHitList(dataFile, grammarIDFile, contentIDFile, simplicityIDFile);

			} catch (IOException e){

			}
		}
	}

}
