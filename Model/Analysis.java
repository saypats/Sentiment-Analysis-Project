package Model;

import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;

public class Analysis {
  
  
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
      pos= new BigDecimal("1.0");
      neg= new BigDecimal("1.0");
    }
    System.out.println(correctReviewCount+" " +reviewCount);        
    double correctProb=(double)correctReviewCount/(double)reviewCount;
    return correctProb;
  }  
  
  
  
  public static void main(String[] args) throws IOException, URISyntaxException {
    
	  System.out.println("Enter TRAIN directories: POSITIVE...");
	  Scanner train = new Scanner(System.in);
	  String trainPos = train.next();
	  System.out.println("Enter TRAIN directories: NEGATIVE...");
	  String trainNeg = train.next();
	  
	  System.out.println("\nEnter TEST directories: POSITIVE...");
	  Scanner test = new Scanner(System.in);
	  String testPos = test.next();
	  System.out.println("Enter TEST directories: NEGATIVE...");
	  String testNeg = test.next();
	  
	  //String trainFile = "Data/Train.data", testFile = "Data/Test_1.data", vocabFile = "Data/Vocab.data", stopwordFile = "Data/stopwords.txt";
		
//		URL path1 = ClassLoader.getSystemResource(trainPos);
//		File trainP = new File(path1.toURI());
//		
//		URL path2 = ClassLoader.getSystemResource(trainNeg);
//		File trainN = new File(path2.toURI());
//		
//		URL path3 = ClassLoader.getSystemResource(testPos);
//		File testP = new File(path3.toURI());
//		
//		URL path4 = ClassLoader.getSystemResource(testNeg);
//		File testN = new File(path4.toURI());
//	  
		makeDictionaryNegation(testPos, testNeg);
		
		List<List<String>> trainingReviewsPos = readReviewDataNegation(trainPos);
	    List<List<String>> trainingReviewsNeg = readReviewDataNegation(trainNeg);   
	    
	    train(trainingReviewsPos,1);
	    train(trainingReviewsNeg,-1);
	    
	    List<List<String>> testReviewsPos = readReviewDataNegation(testPos);
	    List<List<String>> testReviewsNeg = readReviewDataNegation(testNeg);
	    
	    
//	  
//	  if(args.length != 4){
//      System.err.println("Usage: java <Positive_Train_Directory> <Negative_Train_Directory> <Positive_Test_Directory> <Negative_Test_Directory>");
//      System.exit(1);
//    }    
	    
// makeDictionaryNegation(args[2],args[3]);	
//    List<List<String>> trainingReviewsPos = readReviewDataNegation(args[0]);
//    List<List<String>> trainingReviewsNeg = readReviewDataNegation(args[1]);    
   // train(trainingReviewsPos,1);
    //train(trainingReviewsNeg,-1);
    /*for(String word: dictionary){
     System.out.println(word+"^+: "+wordProbs.get(word+"^+"));
     System.out.println(word+"^-: "+wordProbs.get(word+"^-"));
     }*/
//    List<List<String>> testReviewsPos = readReviewDataNegation(args[2]);
//    List<List<String>> testReviewsNeg = readReviewDataNegation(args[3]);
    
    /*for(List<String> review : testReviewsPos){
     for(String word : review){
     System.out.println(wordProbs.get(word.toLowerCase()+"^+"));
     }
     }
     System.out.println();
     for(List<String> review : testReviewsPos){
     for(String word : review){
     System.out.println(wordProbs.get(word.toLowerCase()+"^-"));
     }
     }*/
    System.out.println("Positive Reviews:");
    double posProb=test(testReviewsPos,1);
    System.out.println("Negative Reviews:");
    double negProb=test(testReviewsNeg,-1);
    System.out.println("Positive Review Accuracy: " + posProb);
    System.out.println("Negative Review Accuracy: " + negProb);
    System.out.println("Overall Accuracy: " + (double)Math.round(((posProb+negProb)/2.0)* 1000)/1000);                     
  }
}
