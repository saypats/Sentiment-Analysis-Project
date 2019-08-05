package View;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.*;

 public class Review_Form extends JFrame{
    
    private JFrame frame;
    private JTextArea textArea = new JTextArea();
    private JFileChooser fileChooser;
    
    private JLabel dashs;
    private JLabel dashs2;
    private JLabel resultLabel;
    private JLabel typeOfReviewLabel;
    private JLabel accuracyLabel;
    //private JLabel polarityLabel;
    private JLabel movieRatingLabel;
    
    private JTextField typeOfReviewTextField = new JTextField(10);
    private JTextField accuracyTextField = new JTextField(10);
    private JTextField polarityTextField = new JTextField(10);
    private JTextField movieRatingTextField = new JTextField(10);
   
    //*** TestButton will need to be removed once the Model is up and running
    //		The submit and Run Buttons will need to send the results to the UI
    //		once the analysis has been run...
    private JButton testButton = new JButton("Tester");
    //*****************************************************
    
    private JButton submitButton = new JButton("Submit"); 
    private JButton uploadButton = new JButton("Uplaod");
    private JButton runButton = new JButton("Run");
    
    private String dataSetPath;
    private String uReview;
   
    Review_Form(){
      
       JPanel panel = new JPanel();
      
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       panel.setLayout(null);
       this.setSize(850, 700);
      
       JLabel lblTitle_1 = new JLabel("Welcome to Sentiment Analysis of your Movie Review!");
       lblTitle_1.setBounds(50, 50, 350, 20);
       panel.add(lblTitle_1);
      
       //Movie Rating:
       // Label for the Movie Rating of the Movie Review 
       movieRatingLabel = new JLabel("Give a rating between 1 to 10 (1-lowest, 10-highest): ");
       movieRatingLabel.setBounds(50, 100, 350, 20);
       panel.add(movieRatingLabel);

       // TextField for the Movie Rating of the Movie Review
       movieRatingTextField = new JTextField(10);
       movieRatingTextField.setBounds(355, 100, 50, 20);
       panel.add(movieRatingTextField);
       
       //Movie Review TextArea
       JLabel lblTitle_2 = new JLabel("Please enter in your Movie Review:");
       lblTitle_2.setBounds(50, 130, 250, 20);
       panel.add(lblTitle_2); 
              
        //JTextArea textArea = new JTextArea();
        textArea.setBounds(50, 155, 500, 200);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane pane = new JScrollPane(textArea);
        pane.setPreferredSize(new Dimension(500, 200));
        pane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(textArea);
       
        //JButton reviewSubmit = new JButton("Submit");
        submitButton.setBackground(Color.RED);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(50, 330, 90, 25);
        panel.add(submitButton);
 
        JButton btnClear = new JButton("Clear!");
        btnClear.setBounds(150, 330, 90, 25);
        panel.add(btnClear);
             
        JLabel dashs = new JLabel("---------------------------------------------------------"
               + "--------------------------------------------------------------------");
        dashs.setBounds(50, 380, 900, 10);
        panel.add(dashs);
 
        JLabel lblTitle_3 = new JLabel("Or, select a Movie Review Dataset.");
        lblTitle_3.setBounds(50, 400, 300, 20);
        panel.add(lblTitle_3);
 
        //JButton uploadButton = new JButton("Upload");
        uploadButton.setBackground(Color.RED);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setBounds(50, 430, 90, 25);
        panel.add(uploadButton);
              
        runButton.setBackground(Color.BLUE);
        runButton.setForeground(Color.WHITE);
        runButton.setBounds(150, 430, 90, 25);
        panel.add(runButton);
             
        JLabel dashs2 = new JLabel("---------------------------------------------------------"
               + "--------------------------------------------------------------------");
        dashs2.setBounds(50, 480, 900, 10);
        panel.add(dashs2);
 
        resultLabel = new JLabel("Results: ");
        resultLabel.setBounds(50, 500, 100, 20);
        panel.add(resultLabel);
             
        typeOfReviewLabel = new JLabel("Type of Review (Positive/Negative): ");
        typeOfReviewLabel.setBounds(50, 520, 300, 20);
        panel.add(typeOfReviewLabel);
 
        // TextField for the type of result of the Movie (Positive/Negative)
        typeOfReviewTextField = new JTextField(10);
        typeOfReviewTextField.setBounds(270, 520, 90, 20);
        panel.add(typeOfReviewTextField);
 
        // Label for the Accuracy of the Movie Review
        accuracyLabel = new JLabel("Accuracy: ");
        accuracyLabel.setBounds(50, 545, 300, 20);
        panel.add(accuracyLabel);
 
        // TextField for the accuracy result of the Movie Review
        accuracyTextField = new JTextField(10);
        accuracyTextField.setBounds(270, 545, 90, 20);
        panel.add(accuracyTextField);
 
//        // Label for the Polarity of the Movie Review
//        polarityLabel = new JLabel("Polarity (Strong/Weak): ");
//        polarityLabel.setBounds(50, 570, 300, 20);
//        panel.add(polarityLabel);
 
//        // TextField for the polarity result of the Movie Review
//        polarityTextField = new JTextField(10);
//        polarityTextField.setBounds(270, 570, 90, 20);
//        panel.add(polarityTextField);
              
       
             
        // Just a test button to try and get data from Model... Remove later
        //JButton testButton = new JButton("TempTest");
        testButton.setBackground(Color.BLACK);
        testButton.setForeground(Color.WHITE);
        testButton.setBounds(400, 520, 120, 25);
        panel.add(testButton);
             
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if(returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    dataSetPath = selectedFile.getPath();
                }
            }
        });
 
        // Action listener if the user selects submit button without writing a movie review
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(textArea.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please type a Movie Review");
                else  
                    JOptionPane.showMessageDialog(null, "Movie Review Submitted");
                    
            }
        });
             
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textArea.setText(null);
            }
        });
      
        this.add(panel);
    }
   
    
    //*** Getters to send User Review & dataset path to the Controller
       public String getUserReview() {
    	   uReview = textArea.getText();
       return uReview;
       }
   
       
       public int getRating() {
    	   uReview = movieRatingTextField.getText();
       return Integer.parseInt(uReview);
       }
       /*
       public String getUploadPath() {
    	   return dataSetPath;
       }
       */
       
       // Getter for the file directory of the movie review dataset... 
       public JFileChooser getFileChooser() {
           return fileChooser;
       }
       
       //*** Setters for the results of the UI
       // Meant to accept the verdict of the review.  Is it Positive or Negative...
       public void setTypeOfReview(String reviewType) {
           typeOfReviewTextField.setText(reviewType);
       }
 
       // Meant to accept the accuracy of the Analysis
       public void setAccuracy(double accuracy) {
           accuracyTextField.setText(Double.toString(accuracy));
       }
 
       // Meant to accept the polarity of the Analysis.  Strong or Weak..
       //public void setPolarity(double d) {
       //    polarityTextField.setText(d);
       //}
       
       public void setMovieRating(double movieRating) {
    	   movieRatingTextField.setText(Double.toString(movieRating));
       }
      
       
       //*** Listeners for the 3 buttons which communicate with the Controller
       public void addTesterListener(ActionListener listenForTestButton) {
              testButton.addActionListener(listenForTestButton);
           }
      
      public void addSubmitListener(ActionListener listenForSubmitButton) {
              submitButton.addActionListener(listenForSubmitButton);
       }
       
       public void addRunListener(ActionListener listenForUploadButton) {
    	   runButton.addActionListener(listenForUploadButton);// changed from uploadButton to runButton
       }
       
       
       //*** For the error message to be displayed
       void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
       }


	public String getUploadPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
