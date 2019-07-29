package Model;

//Class used by Analysis.java

import java.util.regex.*;
import java.util.*;

class NegationTokenizer {
  
  public List<String> tokenize(String rawInput){
    // Write a regular expression for each token type
    String ordinal = "(\\d+(?:st|nd|rd|th))";// ordinal number e.g 1st
    String decimal = "([\\+\\-]?\\d+\\.\\d+|[\\+\\-]?\\d+(?:,\\d+)+)"; // decimal numbers and negative numbers and numbers with comma (ignore 1,000.0 form here)
    String number = "(\\d+)"; // int numbers
    String dollar = "(\\$\\d+(?:\\.\\d\\d)?)";
    
    String title =  "(?:(Dr\\.|Mr\\.|Mrs\\.|Sr\\.|Ms\\.|Jr\\.|Mx\\.|Fr\\.|Prof\\.|St\\.|U\\.S\\.)\\s)";// titles list and Abbreviations (with period at the end) list. It's a finite list but not comprehensive in this case. e.g. U.S. Dr.
    String contractionsOrPossessives = "([a-zA-Z]+)(n't|'s|'ve|'d|'ll|'m|'re)"; // Contractions and Possessives e.g. mom's>> [mom, 's]    I've >> [I, 've]
    String word = "([a-zA-Z']+(?:-[a-zA-Z']+)*)"; // words
    
    String specialPunct= "(--)"; // special case when -- should be tokenized to one -- instead of two -
    String punctuation = "(\\p{Punct})"; // e.g. !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
    
    // Combine them into a single string
    String r = ordinal + "|" + dollar + "|" + decimal + "|" + number + "|" + title + "|" + contractionsOrPossessives + "|" + word + "|" + specialPunct + "|" + punctuation;
    // Compile it to a Pattern
    Pattern p = Pattern.compile(r);
    
    // Create a list for the tokens
    List<String> tokenList = new ArrayList<String>();
    
    // Use a Matcher to loop through the input
    Matcher m = p.matcher(rawInput);
    
    int groupCount =  m.groupCount();
    
    while(m.find()){     
      for(int k = 1; k <= groupCount; k++){
        if(m.group(k)!=null){ // modified sample code from m.group(1)!=null
          //System.out.println("Group " + k +": " + m.group(k)); // test purpose
          tokenList.add(m.group(k));
        }
      }
    }
    String [] tempWords = new String[tokenList.size()];
    tempWords=tokenList.toArray(tempWords);
    
    int negate=0;      
    for(int i = 0; i<tempWords.length;i++){
      if(tempWords[i].equals(".")|tempWords[i].equals(",") |tempWords[i].equals("!")|tempWords[i].equals("?")|tempWords[i].equals("(")|tempWords[i].equals(")")){
        negate=0;
      }
      if(negate==1){
        tempWords[i]="!"+tempWords[i];
      }
      if(tempWords[i].equals("not") | tempWords[i].equals("n't") |tempWords[i].equals("never")){
        negate=1;
      }
    }
    List<String> finalTokenList = Arrays.asList(tempWords);  
    return finalTokenList;
  }
}
