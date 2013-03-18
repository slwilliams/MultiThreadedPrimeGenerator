public class MultiThreadedSeive implements Runnable
{
	protected boolean[] primes;
	protected int j = 2;
	protected int sqrt = 0;
	
	protected Thread[] threads;
	protected long startTime = 0;
	protected boolean[] finished;	
	
	public MultiThreadedSeive(int input, int cores)
	{
		primes = new boolean[input];	
		threads = new Thread[cores];
		finished = new boolean[cores];
		
		java.util.Arrays.fill(primes, true);
		java.util.Arrays.fill(finished, true);		
		sqrt = (int)Math.sqrt(primes.length);
		
		startTime = System.nanoTime();
		
		for(int i = 0; i < threads.length; i ++)
		{
			threads[i] = new Thread(this, Integer.toString(i));
			threads[i].start();
		}
	}	
	
	public void run()
	{
		while(true)
		{
			if(j >= sqrt)
			{
				while(true)
				{
					boolean allFinished = true;
					for(int i = 0; i < finished.length; i ++)
					{
						if(finished[i] == false)
						{
							allFinished = false;
							break;
						}
					}
					if(allFinished == true)
					{
						finished();
					}				
				}				
			}	
			finished[getName()] = false;
			
			int myNum = j;	 	
			
			for(int i = j+1; i < primes.length; i ++)
			{
				if(primes[i] == true)
				{
					j = i;
					break;
				}
			}
			
			for(int i = myNum*2; i < primes.length; i += myNum)
			{	
				primes[i] = false;			
			}
			
			finished[getName()] = true;						
		}	
	}
	
	public void finished()
	{
		//stop time
		long timeTaken = System.nanoTime() - startTime;
		System.out.println("Time taken: " + timeTaken/Math.pow(10,9) + " seconds");
		//clean up			
		for(int i = 0; i < threads.length; i ++)
		{
			if(getName() == i)
				continue;
			try
			{
				threads[i].stop();	
			}catch(Exception e){}					
		}
		printArray();
		System.exit(0);
	}		
	
	public int getName()
	{
		Thread t = Thread.currentThread();
		return Integer.parseInt(t.getName());
	}
	
	public void printArray()
	{
		int cnt = 0;
		for(int i = 0; i < primes.length; i ++)
		{
			if(primes[i] == true)
			{
				cnt ++;
			}				
		}
		System.out.println("Primes under " + primes.length + ": " + (cnt-2));
	}
	
	public static void main(String[] args)
	{
		Runtime runtime = Runtime.getRuntime();        
    
    int cores = runtime.availableProcessors();
		System.out.println("cores: " + cores);
		
		new MultiThreadedSeive(1000, cores);
	}
}