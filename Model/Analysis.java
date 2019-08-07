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
	public static String reviewDecision = "";
	public static double sensitive = 0.0;
	//public double precision = 0.0;
			
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
  
  /************* DATASET REVIEW FUNCTIONS *************/
   
  /*** FOR DATASET: Makes Dictionary with Negation ***/
  static private void makeDictionaryNegationDatasets(String testPos, String testNeg)throws IOException, URISyntaxException{	 
	  //Read "stop-words" file and save it as a SET.
	  Set<String> blacklist = readStopWords("training/stopwords.txt");
     
	    //Get words from positive reviews' folder
	    List<List<String>> posReviews = readReviewDataNegationDataset(testPos);
	    for(List<String> review : posReviews){      
	      for(String word : review){
	        dictionary.add(word.toLowerCase());  
	      }
	    }
	  //Get words from negative reviews' folder
	    List<List<String>> negReviews = readReviewDataNegationDataset(testNeg);
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

   
  
  /** FUNTION TO READ STOPWORDS FROM FILE **/
  static private Set<String> readStopWords(String stopWords) throws URISyntaxException, IOException{
	  //Read "stop-words" file and save it as a SET.
	  URL stopPath = ClassLoader.getSystemResource(stopWords);
	    File stop = new File(stopPath.toURI());
	    BufferedReader br = new BufferedReader(new FileReader(stop));
	    String st; 
	    Set<String> blacklist = new HashSet<String>();
	    while ((st = br.readLine()) != null) {
	    	blacklist.add(st);
	    }	    
	    return blacklist;
  }
   
   
  
  /** FUNCTION TO TURN REVIEWS FROM DATASET INTO LIST OF "BAG OF WORDS" **/
  static private List<List<String>> readReviewDataNegationDataset(String testPath)throws IOException, URISyntaxException{
    List<List<String>> reviews = new LinkedList<List<String>>();  
    
    URL path = ClassLoader.getSystemResource(testPath);
    File dir = new File(path.toURI());
    System.out.println(path);
    
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
  

  /*** FUNCTION TO READ A FILE; USED BY readReviewNegationDataset() ***/
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
   
  
  /** DATASET: FUNCTION TO CLASSIFY THE REVIEW - NAIVE BAYES CLASSIFER ALGORITHM **/
   static private void trainDataset(List<List<String>> reviews, int movieRating){
    Map<String,Integer> wordCounts = new HashMap<String,Integer>();		//new map to keep track of words
    for(String word: dictionary)
    {
      wordCounts.put(word,1);
    }
    int tokenCount=0;
    for(List<String> review : reviews){	//looks in list of reviews
      for(String word : review){		//looks in each review
        tokenCount++;					//get the total number of words in that review
        Integer wcount = wordCounts.get(word.toLowerCase()); //get number of words from dictionary
        if(wcount!=null)            
          wordCounts.put(word.toLowerCase(),wcount+1);   //put all the words from the review into dictionary  
      }
    }
   
    /* Compute observation probabilities */
    //Classifying based on the rating first
    if(movieRating > 5)
		sentiment = 1;
	else if (movieRating < 5) 
		sentiment = -1;
	else
		sentiment = 0;
    
     
    for(String word : dictionary){
    	//
    	double prob = (double)wordCounts.get(word)/(tokenCount+dictionary.size());	
      if(sentiment==1){
        wordProbs.put(word+"^+",prob);
      }
      else if(sentiment==-1){
        wordProbs.put(word+"^-",prob);
      }
    }
    
    System.out.print(wordProbs); 
    
  }
 
    
  /** FUNTION TO TEST DATASETS **/
  static private double testDataset(List<List<String>> reviews, int movieRating){
    int reviewCount = 0;
    int correctReviewCount = 0;
    BigDecimal pos= new BigDecimal("1.0");
    BigDecimal neg= new BigDecimal("1.0");
    
    for(List<String> review : reviews){
      reviewCount++;		//total number of reviews
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
        correctReviewCount++;		//sensitivity of positive is more
      }
      if(pos.compareTo(neg)<0 && sentiment==-1){
        correctReviewCount++;		//sensitivity of negative is more
      }
      //Set the sensitivity to 1 for positive and negative again
      pos= new BigDecimal("1.0");
      neg= new BigDecimal("1.0");
    }
    System.out.println("Sensitivity:"+correctReviewCount+"   " +"Total number of Reviews:"+reviewCount);        
    double correctProb=(double)correctReviewCount/(double)reviewCount;	//Calculating accuracy of the sensitivity
    sensitive = correctReviewCount;
    return correctProb;
  }    
  
  
  /************* SINGLE REVIEW SPECIFIC FUNCTIONS *************/

  /*** FOR SINGLE REVIEW: Makes Dictionary with Negation ***/
  static private void makeDictionaryNegation(String user_review, int rating) throws URISyntaxException, IOException {
	  //Read blacklist from the file
	  Set<String> blacklist = readStopWords("training/stopwords.txt");
	 
	  //Get words from user review (bag of words) and add to dictionary
	  List<String> uReview = readReviewDataNegation(user_review); 
	      for(String word : uReview){
	        dictionary.add(word.toLowerCase());  
	      }
	    
	    //remove stop-words from dictionary (Blacklisted words)
	    for(String word : blacklist){
		      dictionary.remove(word); 
	    }	    
	  }  
  
  
  /*** FUNCTION TO READ SINGLE REVIEW; RETURNS "Bag Of Words" ***/
  static private List<String> readReviewDataNegation(String user_review)throws IOException, URISyntaxException{
	    List<String> reviewWords = new ArrayList<String>(); 
	    //Send the raw input to tokenizer 
	        NegationTokenizer tokenizer = new NegationTokenizer();
	        reviewWords = tokenizer.tokenize(user_review);	    
	    System.out.print("List of 'Bag of words': " + reviewWords);
	    return reviewWords;
	  }
  
  /** FUNCTION TO TEST SINGLE REVIEW **/
  static private double testSingle(List<String> bagOfWords, int movieRating){
	    int wordCount = 0;
	    int correctReviewCount = 0;
	  
	    BigDecimal pos= new BigDecimal("1.0");
	    BigDecimal neg= new BigDecimal("1.0");
	    
	      for(String word : bagOfWords){
	    	  wordCount++;
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
	        correctReviewCount++;		//sensitivity of positive is more
	      }
	      if(pos.compareTo(neg)<0 && sentiment==-1){
	        correctReviewCount++;		//sensitivity of negative is more
	      }
	      //Set the sensitivity to 1 for positive and negative again
	      pos= new BigDecimal("1.0");
	      neg= new BigDecimal("1.0");    
  
	    System.out.println("Sensitivity:"+correctReviewCount+"   " +"Total number of Reviews:"+wordCount);        
	    double correctProb=(double)correctReviewCount/(double)wordCount;	//Calculating accuracy of the sensitivity
	    sensitive = correctReviewCount;
	    return correctProb;
	  }  
 

public void dataset(String testPos, String testNeg, double rating) throws IOException, URISyntaxException {
	
	//Training data.....
	String pathPos = "D:/Adv Software Engg Project/Project - Sentiment Analysis for Movie Reviews/Datasets/aclImdb/train/pos";
	String pathNeg = "D:/Adv Software Engg Project/Project - Sentiment Analysis for Movie Reviews/Datasets/aclImdb/train/pos";
    
		List<List<String>> trainingReviewsPos = readReviewDataNegationDataset(pathPos);
		List<List<String>> trainingReviewsNeg = readReviewDataNegationDataset(pathNeg);   
		trainDataset(trainingReviewsPos,1);
		trainDataset(trainingReviewsNeg,-1);
		
		//Now Testing the actual data
		makeDictionaryNegationDatasets(testPos, testNeg);
		  List<List<String>> testReviewsPos = readReviewDataNegationDataset(testPos);
		  List<List<String>> testReviewsNeg = readReviewDataNegationDataset(testNeg);

		  double posProb=testDataset(testReviewsPos,1);
		  double negProb=testDataset(testReviewsNeg,1);
		  
		  accuracy = (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000;
		  
		  //
		  
		  
}


public void single(String uR, int rating) throws IOException, URISyntaxException {
	//Training data.....
		String pathPos = "D:\\Adv Software Engg Project\\Project - Sentiment Analysis for Movie Reviews\\Datasets\\aclImdb\\train\\pos";
		String pathNeg = "D:\\Adv Software Engg Project\\Project - Sentiment Analysis for Movie Reviews\\Datasets\\aclImdb\\train\\neg";
		
			List<List<String>> trainingReviewsPos = readReviewDataNegationDataset(pathPos);
			List<List<String>> trainingReviewsNeg = readReviewDataNegationDataset(pathNeg);   
			trainDataset(trainingReviewsPos,1);
			trainDataset(trainingReviewsNeg,-1);
				
			//Testing a single review
			makeDictionaryNegation(uR, rating);
			  List<String> testReview = readReviewDataNegation(uR);
					  
			  double probab =testSingle(testReview, rating);
			  accuracy = probab;
			  //accuracy = (double)Math.round(((porbab+negProb)/2.0)* 1000)/1000; //overall accuracy	
}



  
  
  /* *******  ADDED MODEL GETTER SETTER FUNCTIONS ******* */

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
  
  
  public double getSensitivity() {
	  return sensitive;
  }
  
      
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
