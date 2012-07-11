import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	private ArrayList<SentenceHIT> HITs;

	private int numAssignments = 5;

	private QualificationRequirement acceptanceRate = new QualificationRequirement("000000000000000000L0", Comparator.GreaterThanOrEqualTo, 95, null, false);
	private QualificationRequirement location = new QualificationRequirement("00000000000000000071", Comparator.EqualTo, null, new Locale("US"), false);
	private QualificationRequirement[] requirements = {acceptanceRate, location};

	// constructor
	public void SenctenceSurvey()
	{
		service = new RequesterService( new PropertiesClientConfig() );
	}

	public void readFile(File input)
	{
		
	}

	public void createSomeHIT(SentenceHIT hit) throws FileNotFoundException
	{
		try 
		{
			SentenceHIT inputHIT = hit;
			HIT amazonHIT = service.createHIT
			(
					null,
					"someHT",
					"someDescription",
					null,
					RatingHit(inputHIT.originalSentence, inputHIT.sentence1, inputHIT.sentence2, inputHIT.sentence3, inputHIT.sentence4, "grammar"),
					00.05,
					(long)300,
					(long)432000, 
					(long)172800, 
					numAssignments,
					"", 
					requirements, 
					null
			);

			// Print out the HITId and the URL to view the HIT.
			System.out.println("Created HIT: " + amazonHIT.getHITId());
			contextpr.println(amazonHIT.getHITId());
			System.out.println("HIT location: ");
			System.out.println(service.getWebsiteURL() + "/mturk/preview?groupId=" + amazonHIT.getHITTypeId());

		} catch (ServiceException e) 
		{
			System.err.println(e.getLocalizedMessage());
		}
	}

	public static String ratingHIT(String originalSentence, String sentence1, String sentence2, String sentence3, String sentence4, String hitType) {
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
		q += "      Original Sentence: " + originalSentence + "</br>";
		q += "		<div id=\"Sentence1\"></div>" + sentence1 + "</br>";
		q += "		<div id=\"Sentence2\"></div>" + sentence2 + "</br>";
		q += "		<div id=\"Sentence3\"></div>" + sentence3 + "</br>";
		q += "		<div id=\"Sentence4\"></div>" + sentence4 + "</br>";
		q += "      <input type=\"submit\" value=\"Submit\" id=\"submit_button\"/>";
		q += "      </form>";
		q += "    <script language='Javascript'>turkSetAssignmentID();</script>";
		q += "    <script type=\'text/javascript\'>";
		q += "      if (document.getElementById(\"assignmentId\").value == \"ASSIGNMENT_ID_NOT_AVAILABLE\") {";
		q += "        document.getElementById(\"submit_button\").disabled = true;";
		q += "        document.getElementById(\"answer\").disabled = true; } ";
		q += "      else {document.getElementById(\"submit_button\").disabled = false;";
		q += " 				for(i=1; i<5; i++){";
		q += "					fillDropDown(\"Sentence\" + i);";
		q += "				}";
		q += "		}";
		q += "		function validateForm(){";
		q += "			for (i=1; i<5; i++){";
		q += "				if (document.getElementById(\"Sentence\" + i).value == \"null\"){";
		q += " 					alert(\"Please rate all sentences.\");";
		q += " 					return false;";
		q += "				}";
		q += " 			}";
		q += " 		}";
		q += "		function fillDropDown(inputID){";
		q += "			document.getElementById(inputID).innerHTML = \"<select name=\"rating\"/><option value=\"1\">1</option><option value=\"2\">2</option>" +
				"<option value=\"3\">3</option><option value=\"4\">4</option><option value=\"5\">5</option><option value=\"null\">Select Rating</option></select>\";";
		q += "    </script>";
		q += "  </body>";
		q += "</html>]]>";
		q += "  </HTMLContent>";
		q += "  <FrameHeight>500</FrameHeight>";
		q += "</HTMLQuestion>";
		return q;
	}

	public static void main(String[] args) throws IOException
	{
		String usageError = "Please provide a valid option. Such as: " +
		"\n -add FILENAME           *creates new HITs from the data provided in the given file(s)* ";

		if ( args.length >=1 )
		{
			SentenceSurvey app = new SentenceSurvey();
			File inputFile = null;

			try 
			{
				inputFile = new File(args[1]);
				app.readFile(inputFile);
				
				
			} catch (IOException e)
			{
				System.err.println("Could not find the file:");
			}

		} else 
		{
			System.out.println(usageError);
		}
	}
}

	















