import java.io.PrintWriter;
import java.util.HashMap;

public class FrequencyCounter<K>
	{
		public HashMap<K, Double> frequency = new HashMap<K, Double>();
		public double samples = 0;
		public double mean = 0;
		public double sd = 0;
		
		public void add(K key)
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
			for (K key : frequency.keySet() )
			{
				frequency.put(key, frequency.get(key)/samples);
				mean += (Double)key * frequency.get(key);
			}
			// calculating standard deviations
			for (K key : frequency.keySet() )
			{
				sd += frequency.get(key) * Math.pow((Double)key - mean, 2);
			}
			sd = Math.sqrt(sd);
		}
	
		public void printCSV(PrintWriter out)
		{
			for ( K key : frequency.keySet() )
			{
				out.printf(key + ", %.8f", frequency.get(key) );
				out.println();
			}
		}
	}
