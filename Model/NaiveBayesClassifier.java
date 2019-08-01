import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.StringTokenizer;

public class NaiveBayesClassifier {
	
	
	public static void NBClassifier(boolean binaryNB, File trainFile, File testFile, File vocabFile, File stopwordFile, boolean removeStopwords) throws IOException{
		long time = System.currentTimeMillis();
		String s;
		BufferedReader br;
		HashSet<Integer> stopwords = new HashSet<>();
		int distinctWords = 0;
		
		HashSet<String> stopwordsStr = new HashSet<>();
		if(removeStopwords) {
			br = new BufferedReader(new FileReader(stopwordFile));
			while((s = br.readLine())!=null)	stopwordsStr.add(s);
			br.close();
		}
		
		br = new BufferedReader(new FileReader(vocabFile));
		while((s = br.readLine())!=null) {
			if(stopwordsStr.contains(s))	stopwords.add(distinctWords);	
			distinctWords++;
		}
		br.close();
		int[] countPos = new int[distinctWords];//countPos[0] = Count(word=vocab[0] && Review=positive)
		int[] countNeg = new int[distinctWords];
		int posReviews = 0, negReviews = 0, totalWordsInPosReviews = 0, totalWordsInNegReviews = 0;
		
		br = new BufferedReader(new FileReader(trainFile));
		while((s = br.readLine())!=null) {
			StringTokenizer st = new StringTokenizer(s," :");
			if(st.countTokens()==0)	continue;
			int rating = Integer.parseInt(st.nextToken());
			if(rating > 5) { // Positive review
				posReviews++;
				while(st.hasMoreTokens()) {
					int word = Integer.parseInt(st.nextToken());
					int freq = Integer.parseInt(st.nextToken());
					freq = binaryNB ? 1 : freq;
					if(stopwords.contains(word))	continue;
					countPos[word]+=freq;
					totalWordsInPosReviews+=freq;
				}
			}else { // Negative Review
				negReviews++;
				while(st.hasMoreTokens()) {
					int word = Integer.parseInt(st.nextToken());
					int freq = Integer.parseInt(st.nextToken());
					freq = binaryNB ? 1 : freq;
					if(stopwords.contains(word))	continue;
					countNeg[word]+=freq;
					totalWordsInNegReviews+=freq;
				}
			}
		}
		br.close();
		
		br = new BufferedReader(new FileReader(testFile));
		int truePositive = 0,
			falsePositive = 0, 
			falseNegative = 0, 
			correctClassification = 0, 
			incorrectClassification = 0;
		
		while((s = br.readLine())!=null) {
			StringTokenizer st = new StringTokenizer(s, " :");
			int rating = Integer.parseInt(st.nextToken());
			int actual = rating>5 ? 1 : 0;//1-->yes, 0-->no
			double probOfPos = Math.log(posReviews/(posReviews+negReviews+0.0));
			double probOfNeg = Math.log(negReviews/(posReviews+negReviews+0.0));
			
			
			//Added code
			System.out.println("Value: " +actual);
			
			if(actual == 1){
				System.out.print("The preidcted classification of the review is POSITIVE\n");}
			else{
				System.out.print("The preidcted classification of the review is NEGATIVE\n");}
			
			while(st.hasMoreTokens()) {	
				int word = Integer.parseInt(st.nextToken());
				int freq = Integer.parseInt(st.nextToken());
				freq = binaryNB ? 1 : freq;
				if(stopwords.contains(word))	continue;
				probOfPos+=freq*Math.log((countPos[word]+1)/(totalWordsInPosReviews+distinctWords+0.0));
				probOfNeg+=freq*Math.log((countNeg[word]+1)/(totalWordsInNegReviews+distinctWords+0.0));
			}
			
			int predicted = (probOfPos>probOfNeg ? 1 : 0);
			if(predicted  == actual )	correctClassification++;
			else 						incorrectClassification++;
		
			if(predicted==1 && actual==1)			truePositive++;
			else if(predicted==1 && actual==0)		falseNegative++;
			else if(predicted==0 && actual==1)		falsePositive++;
		}
		br.close();
		double accuracy = correctClassification/(correctClassification + incorrectClassification + 0.0);
		double precision = truePositive/(truePositive+falsePositive+0.0);
		double recall = truePositive/(truePositive+falseNegative+0.0);
		double fscore = 2*precision*recall/(precision+recall);
		
		//Accuracy and Precision of the prediction: 
		System.out.println("\n Accuracy="+accuracy+"\nPrecision="+precision+" Recall="+recall+" F-Score="+fscore);
		time = System.currentTimeMillis()-time;
		System.out.println("Time:"+time/1000d+"s");
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		String trainFile = "Data/Train.data", testFile = "Data/Test.data", vocabFile = "Data/Vocab.data", stopwordFile = "Data/stopwords.txt";
		
		URL path1 = ClassLoader.getSystemResource(trainFile);
		File train = new File(path1.toURI());
		
		URL path2 = ClassLoader.getSystemResource(testFile);
		File test = new File(path2.toURI());
		
		URL path3 = ClassLoader.getSystemResource(vocabFile);
		File vocab = new File(path3.toURI());
		
		URL path4 = ClassLoader.getSystemResource(stopwordFile);
		File stopword = new File(path4.toURI());
		
		/*		
		System.out.println("Without removing stopwords");
		NBClassifier(false, train, test, vocab, stopword, false);
		System.out.println("After removing stopwords");
		NBClassifier(false, train, test, vocab, stopword, true);
		*/
		
		System.out.println("\nNaive Bayes Classification:");
		//System.out.println("Without removing stopwords --> \n");
		//NBClassifier(true, train, test, vocab, stopword, false);
		System.out.println("\nAfter removing stopwords -->\n");
		NBClassifier(true, train, test, vocab, stopword, true);
	}
}