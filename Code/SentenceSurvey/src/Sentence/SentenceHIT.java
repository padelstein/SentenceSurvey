package src.Sentence;

import java.util.HashMap;

import com.amazonaws.mturk.requester.Assignment;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.PropertiesClientConfig;


public class SentenceHIT 
{
	// instance variables
	private RequesterService service = new RequesterService(new PropertiesClientConfig());
	
	public String dataID;
	
	public String originalSentence;
	public String sentence1;
	public String sentence2;
	public String sentence3;
	public String sentence4;
	
	public HashMap< String, FrequencyCounter<Integer> > answers = new HashMap<String, FrequencyCounter<Integer> >();
	
	public HIT amazonHIT = null;
	public String ID = null;
	public String typeID = null;
	
	// constructor for submission to mTurk
	public SentenceHIT(String original, String option1, String option2, String option3, String option4, String number)
	{
		dataID = number;
		originalSentence = original;
		sentence1 = option1;
		sentence2 = option2;
		sentence3 = option3;
		sentence4 = option4;
		
		answers.put( sentence1, new FrequencyCounter<Integer>() );
		answers.put( sentence2, new FrequencyCounter<Integer>() );
		answers.put( sentence3, new FrequencyCounter<Integer>() );
		answers.put( sentence4, new FrequencyCounter<Integer>() );	
		
//		calcAnswers();
	}

	// constructor for retrieval from mTurk
	public void updateWithAmazon(String HITid)
	{
		
		amazonHIT = service.getHIT(HITid);
		typeID = amazonHIT.getHITTypeId();
		ID = amazonHIT.getHITId();
		
		calcAnswers();
	}
	
	public void calcAnswers()
	{
		Assignment[] assignments = service.getAllAssignmentsForHIT(ID);
		
		for ( Assignment ass : assignments)
		{
			int textStart = ass.getAnswer().indexOf("<FreeText>");
			int textEnd = ass.getAnswer().indexOf("</FreeText>");
			String[] answerText = ass.getAnswer().substring(textStart + 10, textEnd).toLowerCase().split("|");
			
			addAnswer(sentence1, Integer.parseInt(answerText[0]) );
			addAnswer(sentence2, Integer.parseInt(answerText[1]) );
			addAnswer(sentence3, Integer.parseInt(answerText[2]) );
			addAnswer(sentence4, Integer.parseInt(answerText[3]) );
		}
	}
	
	public void addAnswer(String sentence, Integer score)
	{
		answers.get(sentence).add(score);
	}
	
	public double getAverageAnswer(String sentence)
	{
		answers.get(sentence).calcStats();
		
		return answers.get(sentence).mean;
	}
	
	public String toString()
	{
		return "Original : " + originalSentence
				+ "\n" + sentence1
				+ "\n" + sentence2
				+ "\n" + sentence3
				+" \n" + sentence4;
	}
}
