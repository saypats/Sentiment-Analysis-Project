import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;

public class MRSentiment {
	public static double sensitivity = 0;
    
  //probability tables
  static private Map<String,Double> wordProbs = new HashMap<String,Double>();
  static private Set<String> dictionary = new HashSet<String>();
  
  //static getter methods
  static public Map<String,Double> getWordProbs(){
    return wordProbs;
  }
  static public Set<String> getDictionary(){
    return dictionary;
  }
  
  //Makes Dictionary. No Negation.
  static private void makeDictionary(String myDirectoryPathPos, String myDirectoryPathNeg)throws IOException, URISyntaxException{
    Set<String> blacklist = new HashSet<String>(Arrays.asList(new String[] {"you","josh","!josh","games","!games","katniss","!jennifer","jennifer","lawrence","mocking","!mocking","watching","!watching","got","whole","!whole","having",".","to","!to","n't","can","!can","then","there","this","!this","!then","be","!be","else","!else","so","!so","i","!i","movie","!movie","they","!they","is","!is","was","!was","were","!were"}));
    List<List<String>> posReviews=readReviewData(myDirectoryPathPos);
    for(List<String> review : posReviews){      
      for(String word : review){
        dictionary.add(word.toLowerCase());  
      }
    }
    List<List<String>> negReviews=readReviewData(myDirectoryPathNeg);
    for(List<String> review : negReviews){     
      for(String word : review){
        dictionary.add(word.toLowerCase());  
      }
    }
    for(String word : blacklist){
      dictionary.remove(word);
    }
  }
  
  //Makes Dictionary. Negation.
  static private void makeDictionaryNegation(String testPos, String testNeg)throws IOException, URISyntaxException{
    Set<String> blacklist = new HashSet<String>(Arrays.asList(new String[] {"games","!games","katniss","!Jennifer","Jennifer","Lawrence","mocking","!mocking","watching","!watching","got","whole","!whole","having",".","to","!to","n't","can","!can","then","there","this","!this","!then","be","!be","else","!else","so","!so","i","!i","movie","!movie","they","!they","is","!is","was","!was","were","!were"}));
    List<List<String>> posReviews=readReviewDataNegation(testPos);
    for(List<String> review : posReviews){      
      for(String word : review){
        dictionary.add(word.toLowerCase());  
      }
    }
    List<List<String>> negReviews=readReviewDataNegation(testNeg);
    for(List<String> review : negReviews){     
      for(String word : review){
        dictionary.add(word.toLowerCase());  
      }
    }
    for(String word : blacklist){
      dictionary.remove(word);
    }
  }
  
  //Turns all the reviews inside a directory into a list of lists of words. No Negation.
  static private List<List<String>> readReviewData(String myDirectoryPathPos)throws IOException, URISyntaxException{
    List<List<String>> reviews = new LinkedList<List<String>>();
    
    URL path = ClassLoader.getSystemResource(myDirectoryPathPos);
    File dir = new File(path.toURI());
    
    File[] directoryListing = dir.listFiles();
    if (directoryListing != null) {
      for (File child : directoryListing) {
        String stringReview = fileToString(child);
        //Tokenizer tokenizer = new Tokenizer();
        //reviews.add(tokenizer.tokenize(stringReview));
        StringTokenizer tokenizer = new StringTokenizer(stringReview);
        reviews.add((List<String>) tokenizer.nextElement());
      }
    }
    return reviews;
  }
  
  //Turns all the reviews inside a directory into a list of lists of words. Negation.
  static private List<List<String>> readReviewDataNegation(String testPos)throws IOException, URISyntaxException{
    List<List<String>> reviews = new LinkedList<List<String>>();  
    
    URL path = ClassLoader.getSystemResource(testPos);
    File dir = new File(path.toURI());
    
    File[] directoryListing = dir.listFiles();
    if (directoryListing != null) {
      for (File child : directoryListing) {
        String stringReview = fileToString(child);
        NegationTokenizer tokenizer = new NegationTokenizer();
        reviews.add(tokenizer.tokenize(stringReview));
      }
    }
    return reviews;
  }
  
  //Reads files. Used by readReviewData & readReviewDataNegation
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
  
  //Naiive Bayes Classifier
  static private void train(List<List<String>> reviews, int sentiment){
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
  
  //Tests reviews. Sentiment is 1 for postive, -1 for negative.
  static private double test(List<List<String>> reviews, int sentiment){
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
      if(pos.compareTo(neg)>0 && sentiment==1){
        correctReviewCount++;
      }
      if(pos.compareTo(neg)<0 && sentiment==-1){
        correctReviewCount++;
      }
      
     // System.out.print(correctReviewCount);
      pos= new BigDecimal("1.0");
      neg= new BigDecimal("1.0");
    }
    
    System.out.println("Correct Review Count: "+correctReviewCount+"\tReviewCount: "  +reviewCount);        
    double correctProb=(double)correctReviewCount/(double)reviewCount;
    sensitivity = correctProb;
    System.out.print("Sensitivity: " +sensitivity);
    return correctProb;
  }  
  
  
  
  public static void main(String[] args) throws IOException, URISyntaxException {
    
	  /**TRAINING **/
	  String trainPos = "trainset/pos";
	  String trainNeg = "trainset/neg";	  	  
	  System.out.println("Training...\nPOSITIVE Directory: " + trainPos);
	  System.out.println("NEGATIVE Directory: " + trainNeg);
	  
	  
	  /** Test Single Review: POSITIVE **/
	  /*
	  String testSingle_pos = "test/single/single_pos/pos";
	  String testSingle_neg = "test/single/single_pos/neg";
	  makeDictionaryNegation(testSingle_pos, testSingle_neg);
	  
	  List<List<String>> trainingReviewsPos = readReviewDataNegation(trainPos);
	  List<List<String>> trainingReviewsNeg = readReviewDataNegation(trainNeg);    
	  train(trainingReviewsPos,1);
	  train(trainingReviewsNeg,-1);
	  System.out.print("\nTraining Complete......");
	  
	  List<List<String>> testReviewsPos = readReviewDataNegation(testSingle_pos);
	  List<List<String>> testReviewsNeg = readReviewDataNegation(testSingle_neg); 
	  
	  System.out.println("\nPositive Reviews:");
	  double posProb=test(testReviewsPos,1);
	  System.out.println("\nNegative Reviews:");
	  double negProb=test(testReviewsNeg,-1);
	  System.out.println("\nPositive Review Accuracy: " + posProb);
	  System.out.println("Negative Review Accuracy: " + negProb);
	  System.out.println("Overall Accuracy: " + (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000); 
	  */
	  
	  /** Test Single Review: NEGATIVE **/
	  /*
	  String testSingle_pos1 = "test/single/single_neg/positive";
	  String testSingle_neg1 = "test/single/single_neg/negative";
	  makeDictionaryNegation(testSingle_pos1, testSingle_neg1);
	  
	  List<List<String>> trainingReviewsPos = readReviewDataNegation(trainPos);
	  List<List<String>> trainingReviewsNeg = readReviewDataNegation(trainNeg);    
	  train(trainingReviewsPos,1);
	  train(trainingReviewsNeg,-1);
	  System.out.print("\nTraining Complete......");
	  
	  List<List<String>> testReviewsPos = readReviewDataNegation(testSingle_pos1);
	  List<List<String>> testReviewsNeg = readReviewDataNegation(testSingle_neg1); 
	  
	  System.out.println("\nPositive Reviews:");
	  double posProb=test(testReviewsPos,1);
	  System.out.println("\nNegative Reviews:");
	  double negProb=test(testReviewsNeg,-1);
	  System.out.println("\nPositive Review Accuracy: " + posProb);
	  System.out.println("Negative Review Accuracy: " + negProb);
	  System.out.println("Overall Accuracy: " + (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000);
	  */
	  
	  /** Test Datasets of Review: POSITIVE and NEGATIVE **/
	  /** Test 10 Positive and 10 Negative Reviews **/
	  /*
	  String test_pos10 = "test/test_10/pos";
	  String test_neg10 = "test/test_10/neg";
	  makeDictionaryNegation(test_pos10, test_neg10);
	  
	  List<List<String>> trainingReviewsPos = readReviewDataNegation(trainPos);
	  List<List<String>> trainingReviewsNeg = readReviewDataNegation(trainNeg);    
	  train(trainingReviewsPos,1);
	  train(trainingReviewsNeg,-1);
	  System.out.print("\nTraining Complete......");
	  
	  List<List<String>> testReviewsPos = readReviewDataNegation(test_pos10);
	  List<List<String>> testReviewsNeg = readReviewDataNegation(test_neg10); 
	  
	  System.out.println("\nPositive Reviews:");
	  double posProb=test(testReviewsPos,1);
	  System.out.println("\nNegative Reviews:");
	  double negProb=test(testReviewsNeg,-1);
	  System.out.println("\nPositive Review Accuracy: " + posProb);
	  System.out.println("Negative Review Accuracy: " + negProb);
	  System.out.println("Overall Accuracy: " + (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000);
	  */
	  
	  
	  /**  TEST 700 Positive and 700 Negative Reviews   **/
	  /*
	  String test_pos700 = "test/test_700/pos_700";
	  String test_neg700 = "test/test_700/neg_700";
	  makeDictionaryNegation(test_pos700, test_neg700);
	  
	  List<List<String>> trainingReviewsPos = readReviewDataNegation(trainPos);
	  List<List<String>> trainingReviewsNeg = readReviewDataNegation(trainNeg);    
	  train(trainingReviewsPos,1);
	  train(trainingReviewsNeg,-1);
	  System.out.print("\nTraining Complete......");
	  
	  List<List<String>> testReviewsPos = readReviewDataNegation(test_pos700);
	  List<List<String>> testReviewsNeg = readReviewDataNegation(test_neg700); 
	  
	  System.out.println("\nPositive Reviews:");
	  double posProb=test(testReviewsPos,1);
	  System.out.println("\nNegative Reviews:");
	  double negProb=test(testReviewsNeg,-1);
	  System.out.println("\nPositive Review Accuracy: " + posProb);
	  System.out.println("Negative Review Accuracy: " + negProb);
	  System.out.println("Overall Accuracy: " + (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000);
	  */
	  
	  
	  /**  TEST 2000 Positive and 2000 Negative Reviews   **/
	  /*
	  String pos_2000 = "test/test_2000/pos_2000";
	  String neg_2000 = "test/test_2000/neg_2000";
	  makeDictionaryNegation(pos_2000, neg_2000);
	  
	  List<List<String>> trainingReviewsPos = readReviewDataNegation(trainPos);
	  List<List<String>> trainingReviewsNeg = readReviewDataNegation(trainNeg);    
	  train(trainingReviewsPos,1);
	  train(trainingReviewsNeg,-1);
	  System.out.print("\nTraining Complete......");
	  
	  List<List<String>> testReviewsPos = readReviewDataNegation(pos_2000);
	  List<List<String>> testReviewsNeg = readReviewDataNegation(neg_2000); 
	  
	  System.out.println("\nPositive Reviews:");
	  double posProb=test(testReviewsPos,1);
	  System.out.println("\nNegative Reviews:");
	  double negProb=test(testReviewsNeg,-1);
	  System.out.println("\nPositive Review Accuracy: " + posProb);
	  System.out.println("Negative Review Accuracy: " + negProb);
	  System.out.println("Overall Accuracy: " + (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000); 
	  */
	  
	  
//	 //Testing purpose   
//    for(List<String> review : testReviewsPos){
//     for(String word : review){
//     System.out.println(wordProbs.get(word.toLowerCase()+"^+"));
//     }
//    }
//     System.out.println();
//     for(List<String> review : testReviewsPos){
//     for(String word : review){
//     System.out.println(wordProbs.get(word.toLowerCase()+"^-"));
//     }
//    }
                     
  }
}
