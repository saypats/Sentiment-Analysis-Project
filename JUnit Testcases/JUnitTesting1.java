package jUnitTesting1;

public class JUnitTesting1 {
	public static int sentiment = 0;	 //1 is Positive, -1 is negative
	public String reviewDecision = "";
	
	public String getReviewDecision(int rating) {
		  if(rating > 5)
		  		sentiment = 1;
		     else if (rating < 5) 
		  		sentiment = -1;
		  
		  if(sentiment == 1)	
			  reviewDecision = "Positive";
		  else
			  reviewDecision = "Negative";  
		  return reviewDecision;
	  }

}
