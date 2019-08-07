package Controller;

import Model.Analysis;
import View.Review_Form;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
    	   //this.theView.addTesterListener(new addTesterListener());
    	   this.theView.addSubmitListener(new addSubmitListener());
    	   this.theView.addRunListener(new addRunButton());
       }
      
       // This Listener is only temporary, will need to be removed when adding the Model
     public class addTesterListener implements ActionListener{
              public void actionPerformed(ActionEvent e) {
                     int rating = Integer.parseInt(theView.getRating());
					theView.setTypeOfReview(theModel.getReviewDecision(rating));
                    theView.setAccuracy(theModel.getAccuracy());
                    
              }
       }
      
      //Listener for Submit button
       class addSubmitListener implements ActionListener{
              public void actionPerformed(ActionEvent e) {
                     String uR = theView.getUserReview();
                     int rating = Integer.parseInt(theView.getRating());
                     try {
						theModel.single(uR, rating);
					} catch (IOException e1) {
						System.out.print("Error occurred");
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						System.out.print("Error occurred");
						e1.printStackTrace();
					}
              }
       }
       
     //Listener for Run Button
       class addRunButton implements ActionListener{
    	   public void actionPerformed(ActionEvent e) {
    		   String pathPos = theView.getPath_pos();
    		   String pathNeg = theView.getPath_neg();
    		   double rating = Double.parseDouble(theView.getRating());
    		   try {
				theModel.dataset(pathPos, pathNeg, rating);
			} catch (IOException e1) {
				System.out.print("Error occurred");
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				System.out.print("Error occurred");
				e1.printStackTrace();
			}
    	   }
       }
 
}
