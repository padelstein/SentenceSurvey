package Sentence;

import java.util.HashMap;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.PropertiesClientConfig;


public class SentenceHIT 
{
	// instance variables
	private RequesterService service = new RequesterService(new PropertiesClientConfig());
	
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
	public SentenceHIT(String original, String option1, String option2, String option3, String option4)
	{
		originalSentence = original;
		sentence1 = option1;
		sentence2 = option2;
		sentence3 = option3;
		sentence4 = option4;
		
		answers.put( sentence1, new FrequencyCounter<Integer>() );
		answers.put( sentence2, new FrequencyCounter<Integer>() );
		answers.put( sentence3, new FrequencyCounter<Integer>() );
		answers.put( sentence4, new FrequencyCounter<Integer>() );	
		
		calcAnswers();
	}

	// constructor for retrieval from mTurk
	public SentenceHIT(String hitID)
	{
		ID = hitID.trim();
		amazonHIT = service.getHIT(ID);
		typeID = amazonHIT.getHITTypeId();
		
		// we provide a requester annotation to the service when creating the HIT that stores the sentences
		String[] sentences = amazonHIT.getRequesterAnnotation().split("\t");
		
		sentence1 = sentences[0];
		sentence2 = sentences[1];
		sentence3 = sentences[2];
		sentence4 = sentences[3];
		
		answers.put( sentence1, new FrequencyCounter<Integer>() );
		answers.put( sentence2, new FrequencyCounter<Integer>() );
		answers.put( sentence3, new FrequencyCounter<Integer>() );
		answers.put( sentence4, new FrequencyCounter<Integer>() );
		
		calcAnswers();
	}
	
	public void calcAnswers()
	{
		// TO DO
		// need to know how assignments are going to come in formatted from mTurk
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