import java.io.IOException;

public class test {
	public static void main(String[] args) {
		
	String s = "test";
		
	new Thread(new Runnable() {
		private String myString;
		public Runnable init(String p) {
			this.myString = p;
			return this;
		}
		
		@Override
	    public void run() {
	    	synchronized(myString){
	    	System.out.println("enter thread: " + myString);
	    	myString.concat("Hello");
	    	System.out.println("thread: " + myString);
	    	while(true)
	    	{}
	    	}
	    }
	}.init(s)).start();
	
	try
	{
	    Thread.sleep(2000);
	}
	catch(InterruptedException ex)
	{
	    Thread.currentThread().interrupt();
	}
	
	
	synchronized(s) {
	System.out.println(s);
	}
	
	}
	
}
