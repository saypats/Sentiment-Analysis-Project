package Model;

import java.util.*;
import View.Review_Form;
import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;

public class Analysis {
	
	//Global result-variables to be displayed to User
	public double accuracy = 0.0;
	public static int sentiment = 0;	 //1 is Positive, -1 is negative 
	public String reviewDecision = "";
	//public double polarity = 0.0;
	public double precision = 0.0;
	//public double movieRating = 0.0;
				
  //probability tables
  static private Map<String,Double> wordProbs = new HashMap<String,Double>();
  static private Set<String> dictionary = new HashSet<String>();
  
  //static getter methods to get: WordProbability and Dictionary
  static public Map<String,Double> getWordProbs(){
    return wordProbs;
  }
  static public Set<String> getDictionary(){
    return dictionary;
  }
  
  /**
   * Training the Algorithm: Creating dictionary from all possible reviews.
   */
  //Makes Dictionary. Negation.
  static private void makeDictionaryNegationDatasets(String testPos, String testNeg, String stopwords)throws IOException, URISyntaxException{
	  
	 
	  //Read "stop-words" file and save it as a SET.
	  URL stopPath = ClassLoader.getSystemResource(stopwords);
	    File dir = new File(stopPath.toURI());
	    
	    Set<String> blacklist = new HashSet<String>(Arrays.asList(new String[] {"games","!games","katniss","!Jennifer",
	    		"Jennifer","Lawrence","mocking","!mocking","watching","!watching","got","whole","!whole","having",".","to",
	    		"!to","n't","can","!can","then","there","this","!this","!then","be","!be","else","!else","so",
	    		"!so","i","!i","movie","!movie","they","!they","is","!is","was","!was","were","!were"}));
	    
	    //Get words from positive reviews' folder
	    List<List<String>> allReviewsWords = readReviewDataNegation(testPos);
	    for(List<String> review : allReviewsWords){      
	      for(String word : review){
	        dictionary.add(word.toLowerCase());  
	      }
	    }
	  //Get words from negative reviews' folder
	    List<List<String>> negReviews = readReviewDataNegation(testNeg);
	    for(List<String> review : negReviews){     
	      for(String word : review){
	        dictionary.add(word.toLowerCase());  
	      }
	    }    
	    //remove stop-words from dictionary (Blacklisted words)
	    for(String word : blacklist){
		      dictionary.remove(word); 
	    }
	  }

  //Turns all the reviews inside a directory into a list of "lists of words". Includes Negation.
  static private List<List<String>> readReviewDataNegation(String testPath)throws IOException, URISyntaxException{
    List<List<String>> reviews = new LinkedList<List<String>>();  
    
    URL path = ClassLoader.getSystemResource(testPath);
    File dir = new File(path.toURI());
    
    File[] directoryListing = dir.listFiles();
    if (directoryListing != null) {
      for (File child : directoryListing) {
        String stringReview = fileToString(child);
        NegationTokenizer tokenizer = new NegationTokenizer();
        reviews.add(tokenizer.tokenize(stringReview));
      }
    }
    System.out.print("List of 'List of words': " + reviews);
    return reviews;
  }
  

  //Reads a file. Used by readReviewDataNegation
  static private String fileToString(File file) throws IOException{
    StringBuilder fileContents = new StringBuilder((int)file.length());
    Scanner scanner = new Scanner(file);
    String lineSeparator = System.getProperty("line.separator");
    try {
      while(scanner.hasNextLine()) {        
        fileContents.append(scanner.nextLine() + lineSeparator);
      }
      return fileContents.toString();
    } finally {
      scanner.close();
    }
  }
  
  //Naive-Bayes-Classifier: Algorithm that classifies the review. Used for training purpose
  static private void train(List<List<String>> reviews, int movieRating){
    Map<String,Integer> wordCounts = new HashMap<String,Integer>();
    for(String word: dictionary)
    {
      wordCounts.put(word,1);
    }
    int tokenCount=0;
    for(List<String> review : reviews){
      for(String word : review){
        tokenCount++;
        Integer wcount = wordCounts.get(word.toLowerCase());
        if(wcount!=null)            
          wordCounts.put(word.toLowerCase(),wcount+1);     
      }
    }
    // Compute observation probabilities  
    if(movieRating > 5)
		sentiment = 1;
	else if (movieRating < 5) 
		sentiment = -1;
    
    for(String word : dictionary){
    	double prob = (double)wordCounts.get(word)/(tokenCount+dictionary.size());
      if(sentiment==1){
        wordProbs.put(word+"^+",prob);
      }
      else if(sentiment==-1){
        wordProbs.put(word+"^-",prob);
      }
    }
  }
  
  
  //Tests reviews. Sentiment is 1 for positive, -1 for negative.
  static private double test(List<List<String>> reviews, int movieRating){
    int reviewCount = 0;
    int correctReviewCount = 0;
    BigDecimal pos= new BigDecimal("1.0");
    BigDecimal neg= new BigDecimal("1.0");
    for(List<String> review : reviews){
      reviewCount++;
      for(String word : review){
        if(dictionary.contains(word.toLowerCase())){
          BigDecimal probpos= new BigDecimal(wordProbs.get(word.toLowerCase()+"^+"));
          pos=pos.multiply(probpos);          
          BigDecimal probneg= new BigDecimal(wordProbs.get(word.toLowerCase()+"^-"));
          neg=neg.multiply(probneg);
        }
      }
      
     //Setting sentiment according to movie ratings
     if(movieRating > 5)
  		sentiment = 1;
     else if (movieRating < 5) 
  		sentiment = -1;
      
      if(pos.compareTo(neg)>0 && sentiment==1){
        correctReviewCount++;
      }
      if(pos.compareTo(neg)<0 && sentiment==-1){
        correctReviewCount++;
      }
      pos= new BigDecimal("1.0");
      neg= new BigDecimal("1.0");
    }
    System.out.println("CorrectReviewCount:"+correctReviewCount+"   " +"Review Count:"+reviewCount);        
    double correctProb=(double)correctReviewCount/(double)reviewCount;
    return correctProb;
  }  
  
  
  /* *******  ADDED MODEL GETTER SETTER FUNCTIONS ******* */
  // dummy data to make sure it is getting sent from Model to the View, via Controller
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
  
  // dummy data to make sure it is getting sent from Model to the View, via Controller      
  public double getAccuracy() {
         return accuracy;
  }
  
  //*** These functions are just to make sure the info is being passed to the Model
  // function just to make sure that the user's review is making it to the Model...
  public void printReview(String uReview) {
         System.out.println("This is your Review: " + uReview);
  }
  
  public void printPath(String p) {
	   System.out.println("This is the path for the file selected: " + p);
  }
  
public void setMovieRating(int rating) {
	
}


public void testSingle(String uR, int rating) {
	if(rating > 5) {
		
	}
	
	else if(rating <5) {
		
		
	}
	
}
  
  
  
  /*
  public static void main(String[] args) throws IOException, URISyntaxException {
    
	  System.out.println("Enter TRAIN directories: POSITIVE...");
	  Scanner train = new Scanner(System.in);
	  String trainPos = train.next();
	  System.out.println("Enter TRAIN directories: NEGATIVE...");
	  String trainNeg = train.next();
	  System.out.println("Enter path to Stopwords list...");
	  String stopPath = train.next();
	  
	  System.out.println("\nEnter TEST directories: POSITIVE...");
	  Scanner test = new Scanner(System.in);
	  String testPos = test.next();
	  System.out.println("Enter TEST directories: NEGATIVE...");
	  String testNeg = test.next();
  
		makeDictionaryNegationDatasets(testPos, testNeg, stopPath);
		
		List<List<String>> trainingReviewsPos = readReviewDataNegation(trainPos);
	    List<List<String>> trainingReviewsNeg = readReviewDataNegation(trainNeg);   
	    
	    train(trainingReviewsPos,1);
	    train(trainingReviewsNeg,-1);
	    
	    List<List<String>> testReviewsPos = readReviewDataNegation(testPos);
	    List<List<String>> testReviewsNeg = readReviewDataNegation(testNeg);
	        
	    
    System.out.println("Positive Reviews:");
    double posProb=test(testReviewsPos,1);
    System.out.println("Negative Reviews:");
    double negProb=test(testReviewsNeg,-1);
    System.out.println("Positive Review Accuracy: " + posProb);
    System.out.println("Negative Review Accuracy: " + negProb);
    System.out.println("Overall Accuracy: " + (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000);                     
  }
  */
}
