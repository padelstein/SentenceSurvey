package src.Sentence;

import java.io.PrintWriter;
import java.util.HashMap;

public class FrequencyCounter
	{
		public HashMap<Double, Double> frequency = new HashMap<Double, Double>();
		public double samples = 0;
		public double mean = 0;
		public double sd = 0;
		
		public void add(Double key)
		{
			if (frequency.containsKey(key)){
				frequency.put(key, frequency.get(key) + 1);
			}else {
				frequency.put(key, (double) 1);
			} 
			samples++;
		}
		
		public void calcStats()
		{
			mean = 0;
			sd = 0;
			
			for (Double key : frequency.keySet() )
			{
				frequency.put(key, frequency.get(key)/samples);
				mean += key * frequency.get(key);
			}
			// calculating standard deviations
			for (Double key : frequency.keySet() )
			{
				sd += frequency.get(key) * Math.pow((Double)key - mean, 2);
			}
			sd = Math.sqrt(sd);
		}
	
		public void printCSV(PrintWriter out)
		{
			for ( Double key : frequency.keySet() )
			{
				out.printf(key + ", %.8f", frequency.get(key) );
				out.println();
			}
		}
	
		public String toString()
		{
			String out = "\t";
			
			for ( Double key : frequency.keySet() )
			{
				for ( double i = 0 ; i < frequency.get(key) ; i++)
				{
					out += key + "\t";
				}
			}
			return out;
		}
	}
