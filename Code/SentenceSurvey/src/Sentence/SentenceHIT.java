package Sentence;

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
	
	public FrequencyCounter answers1 = new FrequencyCounter();
	public FrequencyCounter answers2 = new FrequencyCounter();
	public FrequencyCounter answers3 = new FrequencyCounter();
	public FrequencyCounter answers4 = new FrequencyCounter();
	
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
			String[] answerText = ass.getAnswer().substring(textStart + 10, textEnd).toLowerCase().split("\\|");
			
			addAnswer(1, Integer.parseInt(answerText[0]) );
			addAnswer(2, Integer.parseInt(answerText[1]) );
			addAnswer(3, Integer.parseInt(answerText[2]) );
			addAnswer(4, Integer.parseInt(answerText[3]) );
		}
	}
	
	public void addAnswer(Integer option, Integer score)
	{
		if (option == 1)
			answers1.add((double)score);
		if (option == 2)
			answers2.add((double)score);
		if (option == 3)
			answers3.add((double)score);
		if (option == 4)
			answers4.add((double)score);
	}
	
	public double getAverageAnswer(Integer option)
	{
		if (option == 1) {
			answers1.calcStats();
			return answers1.mean;
		} else if (option == 2) {
			answers2.calcStats();
			return answers2.mean;
		} else if (option == 3) {
			answers3.calcStats();
			return answers3.mean;
		} else if (option == 4) {
			answers4.calcStats();
			return answers4.mean;
		} else {
			return 0.0;
		}
	}
	
	public String toString()
	{
		return "Original : " + originalSentence
				+ "\n" + sentence1 + getAverageAnswer(1)
				+ "\n" + sentence2 + getAverageAnswer(2)
				+ "\n" + sentence3 + getAverageAnswer(3)
				+" \n" + sentence4 + getAverageAnswer(4);
	}
}
