package View;

import Controller.Controller;
import Model.Analysis;

public class MovieReview {
	 public static void main(String[] args) {
         Review_Form theView = new Review_Form();
         Analysis theModel = new Analysis();
        
         Controller theController = new Controller(theView, theModel);
        
         theView.setVisible(true);
  }
}
