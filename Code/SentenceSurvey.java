public class SentenceSurvey
{
	
	// instance variables
	private RequesterService service;

	private int numAssignments = 5;
	
	// constructor
	public SenctenceSurvey()
	{
		service = new RequesterService( new PropertiesClientConfig() );
	}

	public void createSomeHIT() throws FileNotFoundException
	{
		try 
		{
			HIT hit = service.createHIT
			(
					null,
					"someHT",
					"someDescription,
					null,
					HIT(firstSentence, word, secondSentence),
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
			System.out.println("Created HIT: " + hit.getHITId());
			contextpr.println(hit.getHITId());
			System.out.println("HIT location: ");
			System.out.println(service.getWebsiteURL() + "/mturk/preview?groupId=" + hit.getHITTypeId());

		} catch (ServiceException e) 
		{
			System.err.println(e.getLocalizedMessage());
		}
	}

	public static String someHIThtml(SentenceHIT hit)
	{
		String q = "";
		return q
	}

	public static void main(String[] args) throws IOException
	{
		String usageError = "Please provide a valid option. Such as: " +
		"\n -add FILENAME           *creates new HITs from the data provided in the given file(s)* "

		if ( args.length >=1 )
		{
			SentenceSurvey app = new SentenceSurvey();
			File inputFile = null;
		} else 
		{
			System.out.println(usageError);
		}
	}
}
	

















}
