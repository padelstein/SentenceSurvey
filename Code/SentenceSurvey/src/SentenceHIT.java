import java.util.ArrayList;
import java.util.HashMap;
import com.amazonaws.mturk.requester.HIT;


public class SentenceHIT 
{
	// instance variables
	public String originalSentence;
	public String sentence1;
	public String sentence2;
	public String sentence3;
	public String sentence4;
	
	public HashMap< String, ArrayList<Integer> > answers;
	
	public HIT amazonHIT = null;
	public String ID = null;
	public String typeID = null;
	
	public SentenceHIT(String original, String option1, String option2, String option3, String option4)
	{
		originalSentence = original;
		answers.put( option1, new ArrayList<Integer>() );
		answers.put( option2, new ArrayList<Integer>() );
		answers.put( option3, new ArrayList<Integer>() );
		answers.put( option4, new ArrayList<Integer>() );
		
		sentence1 = option1;
		sentence2 = option2;
		sentence3 = option3;
		sentence4 = option4;
	}

	public void addAnswer(String sentence, Integer score)
	{
		answers.get(sentence).add(score);
	}
	
	public double getAverageAnswer(String sentence)
	{
		double numerator = 0;
		double divisor = 0;
		
		for ( Integer score : answers.get(sentence) )
		{
			numerator += (double) score;
			divisor++;
		}
		return numerator/divisor;
	}
}