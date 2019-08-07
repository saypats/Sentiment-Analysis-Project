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
    private JLabel movieRatingLabel;
    
    private JTextField typeOfReviewTextField = new JTextField(10);
    private JTextField accuracyTextField = new JTextField(10);
    private JTextField movieRatingTextField = new JTextField(10);
    private JTextField posPathDirectory = new JTextField(10);
    private JTextField negPathDirectory = new JTextField(10); 
   
    //*** TestButton will need to be removed once the Model is up and running
    //		The submit and Run Buttons will need to send the results to the UI
    //		once the analysis has been run...
    //private JButton testButton = new JButton("Tester");
    //*****************************************************
    
    private JButton submitButton = new JButton("Submit"); 
    private JButton uploadButton_pos = new JButton("Positive");
    private JButton uploadButton_neg = new JButton("Negative");
    private JButton runButton = new JButton("Run");
    
    private String dataSetPath_pos;
    private String dataSetPath_neg;
    private String uReview;
    private String rating;
   
    Review_Form(){
      
       JPanel panel = new JPanel();
      
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       panel.setLayout(null);
       this.setSize(750, 900);
      
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
       
        JButton reviewSubmit = new JButton("Submit");
        submitButton.setBackground(Color.RED);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(50, 390, 90, 25);
        panel.add(submitButton);
 
        JButton btnClear = new JButton("Clear!");
        btnClear.setBounds(150, 390, 90, 25);
        panel.add(btnClear);
             
        JLabel dashs = new JLabel("---------------------------------------------------------"
               + "--------------------------------------------------------------------");
        dashs.setBounds(50, 420, 900, 10);
        panel.add(dashs);
 
        JLabel lblTitle_3 = new JLabel("Or, select Movie Review Datasets.");
        lblTitle_3.setBounds(50, 450, 300, 20);
        panel.add(lblTitle_3);
 
        //Text field for Positive Directory Path
        posPathDirectory = new JTextField(10);
        posPathDirectory.setBounds(50, 480, 90, 20);
        panel.add(posPathDirectory);              
      //JButton uploadButton = new JButton("Positive"); Browse option
        uploadButton_pos.setBackground(Color.RED);
        uploadButton_pos.setForeground(Color.WHITE);
        uploadButton_pos.setBounds(150, 480, 90, 25);
        panel.add(uploadButton_pos);
        
        //Text field for Negative Directory Path
        negPathDirectory = new JTextField(10);
        negPathDirectory.setBounds(50, 520, 90, 20);
        panel.add(negPathDirectory);   
      //JButton uploadButton = new JButton("Negative");
        uploadButton_neg.setBackground(Color.RED);
        uploadButton_neg.setForeground(Color.WHITE);
        uploadButton_neg.setBounds(150, 520, 90, 25);
        panel.add(uploadButton_neg);
          
        //Run button for uploaded directory paths
        runButton.setBackground(Color.BLUE);
        runButton.setForeground(Color.WHITE);
        runButton.setBounds(50, 550, 90, 25);
        panel.add(runButton);
             
        JLabel dashs2 = new JLabel("---------------------------------------------------------"
               + "--------------------------------------------------------------------");
        dashs2.setBounds(50, 600, 900, 10);
        panel.add(dashs2);
 
        resultLabel = new JLabel("Results: ");
        resultLabel.setBounds(50, 650, 100, 20);
        panel.add(resultLabel);
        
        // TextField for the type of result of the Movie (Positive/Negative)
        typeOfReviewLabel = new JLabel("Type of Review (Positive/Negative): ");
        typeOfReviewLabel.setBounds(50, 730, 300, 20);
        panel.add(typeOfReviewLabel);
        //TextField for sentiment type
        typeOfReviewTextField = new JTextField(10);
        typeOfReviewTextField.setBounds(280, 730, 90, 20);
        panel.add(typeOfReviewTextField);
 
        // Label for the Accuracy of the Movie Review
        accuracyLabel = new JLabel("Accuracy: ");
        accuracyLabel.setBounds(50, 690, 300, 20);
        panel.add(accuracyLabel);
        // TextField for the accuracy result of the Movie Review
        accuracyTextField = new JTextField(10);
        accuracyTextField.setBounds(280, 690, 90, 20);
        panel.add(accuracyTextField);
  
        
        //Upload Test files: Positive and Negative: Added code to select directory path
        uploadButton_pos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {          	
            	JFileChooser fileChooser = new JFileChooser();
            	fileChooser.setCurrentDirectory(new java.io.File("."));
                int returnValue = fileChooser.showOpenDialog(null);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                if(returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getCurrentDirectory();
                    dataSetPath_pos = selectedFile.getPath();
                    posPathDirectory.setText(dataSetPath_pos);
                }
            }
        });
        
        uploadButton_neg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	JFileChooser fileChooser = new JFileChooser();
            	fileChooser.setCurrentDirectory(new java.io.File("."));
                int returnValue = fileChooser.showOpenDialog(null);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                if(returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getCurrentDirectory();
                    dataSetPath_neg = selectedFile.getPath();
                    negPathDirectory.setText(dataSetPath_pos);
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
                
             // Added this little bit to make sure the User enters a rating between 1-10
        		int uMovieRating = Integer.parseInt(movieRatingTextField.getText());
                        if(uMovieRating > 10 || uMovieRating < 1) {
                        	JOptionPane.showMessageDialog(null, "Please enter a rating between 1-10");
                        	movieRatingTextField.setText(null);
                        }                  
            }
        });
        
        //Run button action listener 
        runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(movieRatingTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter movie rating");
				}
				else if(posPathDirectory.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please provide path for positive reviews");
				} 
				else if(negPathDirectory.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please provide path for negative reviews");
				}				
			}
		});
             
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textArea.setText(null);
            	movieRatingTextField.setText(null);
            }
        });
      
        this.add(panel);
    }
   
    
    //*** Getters to send User Review & dataset path to the Controller
       public String getUserReview() {
    	   uReview = textArea.getText();
       return uReview;
       }   
       
       public String getRating() {
    	   rating = movieRatingTextField.getText();
       return rating;
       }
       
      
       public String getPath_pos() {
    	   return dataSetPath_pos;
       }
       
       public String getPath_neg() {
    	   return dataSetPath_neg;
       }
       
       
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


	
}
