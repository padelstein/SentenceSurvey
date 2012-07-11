import java.io.FileNotFoundException;
import java.io.IOException;

public class SentenceSurvey
{
	
	// instance variables
//	private RequesterService service;

	private int numAssignments = 5;
	
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
//		service = new RequesterService( new PropertiesClientConfig() );
	}

	public void createSomeHIT() throws FileNotFoundException
	{
//		try 
//		{
//			HIT hit = service.createHIT
//			(
//					null,
//					"someHT",
//					"someDescription,
//					null,
//					HIT(firstSentence, word, secondSentence),
//					00.05,
//					(long)300,
//					(long)432000, 
//					(long)172800, 
//					numAssignments,
//					"", 
//					requirements, 
//					null
//			);

			// Print out the HITId and the URL to view the HIT.
//			System.out.println("Created HIT: " + hit.getHITId());
//			contextpr.println(hit.getHITId());
//			System.out.println("HIT location: ");
//			System.out.println(service.getWebsiteURL() + "/mturk/preview?groupId=" + hit.getHITTypeId());
//
//		} catch (ServiceException e) 
//		{
//			System.err.println(e.getLocalizedMessage());
//		}
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
//			File inputFile = null;
		} else 
		{
			System.out.println(usageError);
		}
	}
}
