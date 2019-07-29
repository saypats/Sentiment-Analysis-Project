import java.io.File;

import Model.Analysis;

public class Controller {
	 private Analysis model;
	   private Review_Form view;
	   
	   public String getReview() {
		   String review = "";	//***** temporary statement NOT NEEDED *******
		return review ;
	   }
	   
	   public File getReviewFromDataset() {
		   File dataset = new File("File");	//***** temporary statement NOT NEEDED *******
		return dataset ;
	   }
	   
	   public void setReview(String review) {
		   //Add code to create new file and add the text to file for processingS
		   File user_review = new File("test/pos/Review.txt");
		   //this.review = review;
	   }
}
