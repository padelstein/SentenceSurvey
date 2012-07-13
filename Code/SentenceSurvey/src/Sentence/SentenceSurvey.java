package src.Sentence;

import java.io.*;
import java.util.ArrayList;

import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.amazonaws.mturk.requester.Locale;

public class SentenceSurvey
{
	// instance variables
	private RequesterService service;

	private ArrayList<SentenceHIT> HITs = new ArrayList<SentenceHIT>();

	private int numAssignments = 5;

	private QualificationRequirement acceptanceRate = new QualificationRequirement("000000000000000000L0", Comparator.GreaterThanOrEqualTo, 95, null, false);
	private QualificationRequirement location = new QualificationRequirement("00000000000000000071", Comparator.EqualTo, null, new Locale("US"), false);
	private QualificationRequirement[] requirements = {acceptanceRate, location};

	private static String grammaticalDescription = "Rate each sentence below with a score from 1 to 5 based on how " +
	"grammatical the sentence is.  A 1 indicates completely ungrammatical " +
	"and 5 perfectly grammatical.";
	private static String contextDescription = "Each sentence below is a candidate simplification for the original " +
	"sentence listed below.  Rate each sentence with a score from 1 to 5 " +
	"based on how well the content of the original sentence is preserved in " +
	"the candidate sentence.  A 1 indicates that none of the key ideas were " +
	"preserved while a 5 indicates that all of the key ideas were " +
	"preserved.";
	private static String simplicityDescription = "Each sentence below is a candidate simplification for the original " +
	"sentence listed below.  Rate each sentence with a score from 1 to 5 " +
	"based on how simple the candidate sentence.  A good simplification " +
	"should preserve the main ideas, but make the content understandable to " +
	"a broader audience.";

	// constructor
	public SentenceSurvey()
	{
		service = new RequesterService( new PropertiesClientConfig() );
	}

	// reads data file and primes HITs for submission
	public void readFile(File inputFile) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream( inputFile ) ) );
		String input = "";

		input = in.readLine();
		input = in.readLine();
		input = in.readLine();
		input = in.readLine();

		int k = 0;
		
		while ( input != null && k < 5 )
		{
			String original = input;
			original = original.toUpperCase().charAt(0) + original.substring(1);
			String option1 = in.readLine();
			option1 = option1.toUpperCase().charAt(0) + option1.substring(1);
			String option2 = in.readLine();
			option2 = option2.toUpperCase().charAt(0) + option2.substring(1);
			String option3 = in.readLine();
			option3 = option3.toUpperCase().charAt(0) + option3.substring(1);
			String option4 = in.readLine();
			option4 = option4.toUpperCase().charAt(0) + option4.substring(1);

			SentenceHIT newHIT = new SentenceHIT(original, option1, option2, option3, option4);

			HITs.add(newHIT);

			input = in.readLine();
			input = in.readLine();
			input = in.readLine();
			input = in.readLine();
			
			k++;
		}

	}

	// creates an individual HIT
	public void createHIT(SentenceHIT hit, String title, String description, String type) throws FileNotFoundException
	{
		try 
		{
			SentenceHIT inputHIT = hit;
			HIT amazonHIT = service.createHIT
			(
					null,
					title,
					description,
					null,
					ratingHIT(inputHIT.originalSentence, inputHIT.sentence1, inputHIT.sentence2, inputHIT.sentence3, inputHIT.sentence4, type),
					00.05,
					(long)300,
					(long)432000, 
					(long)172800, 
					numAssignments,
					inputHIT.originalSentence, 
					requirements, 
					null
			);

			// Print out the HITId and the URL to view the HIT.
			System.out.println("Created HIT: " + amazonHIT.getHITId());
			//			contextpr.println(amazonHIT.getHITId());
			System.out.println("HIT location: ");
			System.out.println(service.getWebsiteURL() + "/mturk/preview?groupId=" + amazonHIT.getHITTypeId());

		} catch (ServiceException e) 
		{
			System.err.println(e.getLocalizedMessage());
		}
	}

	// the HTML describing a particular HIT, usually called within createHIT()
	public String ratingHIT(String originalSentence, String sentence1, String sentence2, String sentence3, String sentence4, String hitType) 
	{
		String description = "";
		if (hitType.equals("Grammatically"))
			description = grammaticalDescription;
		else if (hitType.equals("Content"))
			description = contextDescription;
		else if (hitType.equals("Simplicity"))
			description = simplicityDescription;
		String q = "";
		q += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		q += "<HTMLQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2011-11-11/HTMLQuestion.xsd\">";
		q += "	<HTMLContent><![CDATA[";
		q += "<!DOCTYPE html>";
		q += "<html>";
		q += "  <head>";
		q += "    <script type=\'text/javascript\' src=\'https://s3.amazonaws.com/mturk-public/externalHIT_v1.js\'></script>";
		q += "  </head>";
		q += "	<body>";
		q += "		<u><b><span style=\"font-size:25px;\">Instructions:</span></b></u><br /><br />";
		q += description;		
		q += "		<hr />";
		q += "		<br /><u><b><span style=\"font-size:25px;\">Task:</span></b></u><br />";
		q += "      <br/><form name='mturk_form' method='post' id='mturk_form' onsubmit=\"return validateForm()\" action='https://www.mturk.com/mturk/externalSubmit' style=\"padding-top:10px\">";
		q += "      <input type=\'hidden\' value=\'\' name =\'assignmentId\' id=\'assignmentId\'/>";
		q += "      <b>Original Sentence:</b> " + originalSentence + "<br/><br/>";
		q += "		<b>Simplification 1:</b> <select id=\"Sentence1\" name=\"rating\"></select>  " + sentence1 + "<br/>";
		q += "		<b>Simplification 2:</b> <select id=\"Sentence2\" name=\"rating\"></select>  " + sentence2 + "<br/>";
		q += "		<b>Simplification 3:</b> <select id=\"Sentence3\" name=\"rating\"></select>  " + sentence3 + "<br/>";
		q += "		<b>Simplification 4:</b> <select id=\"Sentence4\" name=\"rating\"></select>  " + sentence4 + "<br/>";
		q += "      <input type=\"submit\" value=\"Submit\" id=\"submit_button\"/>";
		q += "      </form>";
		q += "    <script language='Javascript'>turkSetAssignmentID();</script>";
		q += "    <script type=\'text/javascript\'>";
		q += "		function fillDropDown(inputID){";
		q += "			document.getElementById(inputID).innerHTML = \"<option value=\\\"1\\\">1</option><option value=\\\"2\\\">2</option>" +
		"<option value=\\\"3\\\">3</option><option value=\\\"4\\\">4</option><option value=\\\"5\\\">5</option><option value=\\\"null\\\" selected=\\\"true\\\">Select Rating</option>\";";
		q += "		}";

		q += " 		for(var j=1; j<5; j++){";
		q += "			fillDropDown(\"Sentence\" + j);";
		q += "		}";
		q += "      if (document.getElementById(\"assignmentId\").value == \"ASSIGNMENT_ID_NOT_AVAILABLE\") {";
		q += "        	document.getElementById(\"submit_button\").disabled = true;";
		q += "			for(var k=1; k<5; k++){";
		q += " 				document.getElementById(\"Sentence\" + k).disabled = true;}";
		q += "			}";
		q += "      else {document.getElementById(\"submit_button\").disabled = false;";
		q += "		}";
		
		q += "		function validateForm(){";
		q += "			for (var i=1; i<5; i++){";
		q += "				if (document.getElementById(\"Sentence\" + i).value == \"null\"){";
		q += " 					alert(\"Please rate all sentences.\");";
		q += " 					return false;";
		q += "				}";
		q += " 			}";
		q += " 		}";
		q += "    </script>";
		q += "  </body>";
		q += "</html>]]>";
		q += "  </HTMLContent>";
		q += "  <FrameHeight>500</FrameHeight>";
		q += "</HTMLQuestion>";
		return q;
	}

	// main method
	public static void main(String[] args) throws IOException
	{
		String usageError = "Please provide a valid option. Such as: " +
		"\n -add FILENAME           *creates new HITs from the data provided in the given file(s)* ";

		if ( args.length >= 1 )
		{
			// instantiates the class
			SentenceSurvey app = new SentenceSurvey();
			File inputFile = null;

			try 
			{
				inputFile = new File(args[0]);
				// reads the data file and primes HITs
				app.readFile(inputFile);
				for (SentenceHIT hit : app.HITs)
				{
					app.createHIT(hit, "Rate These Sentences on Grammar", "Rate the following sentences based on grammar", "Grammatically");
				}

			} catch (IOException e)
			{
				System.err.println("Could not find the file");
			}

		} else 
		{
			System.out.println(usageError);
		}
	}
}


















