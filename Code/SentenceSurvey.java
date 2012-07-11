public static String contextGivenSub(String originalSentence, String sentence1, String sentence2, String sentence3, String sentence4) {
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
		q += "		Rate each sentence below with a score from 1 to 5 based on how ";
		q += "		grammatical the sentence is.  A 1 indicates completely ungrammatical ";
		q += "		and 5 perfectly grammatical."
		q += "		<hr />";
		q += "		<br /><u><b><span style=\"font-size:25px;\">Task:</span></b></u><br />";
		q += "      <br/><form name='mturk_form' method='post' id='mturk_form' onsubmit=\"return validateForm()\" action='https://www.mturk.com/mturk/externalSubmit' style=\"padding-top:10px\">";
		q += "      <input type=\'hidden\' value=\'\' name =\'assignmentId\' id=\'assignmentId\'/>";
		q += "      Original Sentence: " + originalSentence + "</br>";
		q += "		<div id=\"Sentence1\"></div></br>";
		q += "		<div id=\"Sentence2\"></div></br>";
		q += "		<div id=\"Sentence3\"></div></br>";
		q += "		<div id=\"Sentence4\"></div></br>";
		q += "      <input type=\"submit\" value=\"Submit\" id=\"submit_button\"/>";
		q += "      </form>";
		q += "    <script language='Javascript'>turkSetAssignmentID();</script>";
		q += "    <script type=\'text/javascript\'>";
		q += "      if (document.getElementById(\"assignmentId\").value == \"ASSIGNMENT_ID_NOT_AVAILABLE\") {";
		q += "        document.getElementById(\"submit_button\").disabled = true;";
		q += "        document.getElementById(\"answer\").disabled = true; } ";
		q += "      else {document.getElementById(\"submit_button\").disabled = false;";
		q += " 				for(i=1; i<5; i++){";
		q += "					fillDropDown(\"Sentence\" + i);
		q += "				}";
		q += "		}";
		q += "		function validateForm(){";
		q += "			for (i=1; i<5; i++){
		q += "				if (document.getElementById(\"Sentence\" + i).value == \"null\"){";
		q += " 					alert(\"Please rate all sentences.\");";
		q += " 					return false;";
		q += "				}"
		q += " 			}";
		q += " 		}";
		q += "		function fillDropDown(inputID){";
		q += "			document.getElementById(inputID).innerHTML = \"<select name=\"rating\"/><option value=\"1\">1</option><option value=\"2\">2</option>" +
				"<option value=\"3\">3</option><option value=\"4\">4</option><option value=\"5\">5</option><option value=\"null\">Select Rating</option></select>\";"
		q += "    </script>";
		q += "  </body>";
		q += "</html>]]>";
		q += "  </HTMLContent>";
		q += "  <FrameHeight>500</FrameHeight>";
		q += "</HTMLQuestion>";
		return q;
	}