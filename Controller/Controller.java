package Controller;

import Model.Analysis;
import View.Review_Form;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
public class Controller {
      
       private Review_Form theView;
       private Analysis theModel;
      
       // Constructor for the Controller.  TesterListener will need to be removed
       //	once the Model is up and running
       public Controller(Review_Form theView, Analysis theModel) {
    	   this.theView = theView;
    	   this.theModel = theModel;
    	   this.theView.addTesterListener(new addTesterListener());
    	   this.theView.addSubmitListener(new addSubmitListener());
    	   this.theView.addRunListener(new addRunButton());
       }
      
       // This Listener is only temporary, will need to be removed when adding the Model
     public class addTesterListener implements ActionListener{
              public void actionPerformed(ActionEvent e) {
                     int rating = theView.getRating();
					theView.setTypeOfReview(theModel.getReviewDecision(rating));
                     theView.setAccuracy(theModel.getAccuracy());
                     //theView.setPolarity(theModel.getPolarity());
                     //theView.setMovieRating(theModel.getMovieRating());
              }
       }
      
       // This listener will need to add commands to send results to the UI, 
       //	similar to tester up above
       class addSubmitListener implements ActionListener{
              public void actionPerformed(ActionEvent e) {
                     String uR = theView.getUserReview();
                     int rating = theView.getRating();
                     theModel.testSingle(uR, rating);
              }
       }
       
       // This listener will also need to add commands to send results to the UI,
       //	similar to tester up above
       class addRunButton implements ActionListener{
    	   public void actionPerformed(ActionEvent e) {
    		   String path = theView.getUploadPath();
    		   theModel.printPath(path);
    	   }
       }
 
}
