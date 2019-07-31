package View;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Review_Form extends JFrame{
	 
	    private JFrame frame;
	    private JTextArea textArea;
	    private JFileChooser fileChooser;
	    private JLabel dashs;
	    private JLabel dashs2;
	    private JLabel resultLabel;
	    private JLabel typeOfReviewLabel;
	    private JLabel accuracyLabel;
	    private JLabel polarityLabel;
	    private JTextField typeOfReviewTextField;
	    private JTextField accuracyTextField;
	    private JTextField polarityTextField;
	 
	    /**
	     * Launch the application.
	     */
	    public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                	Review_Form window = new Review_Form();
	                    window.frame.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    }
	 
	    /**
	     * Create the application.
	     */
	    public Review_Form() {
	        initialize();
	    }
	 
	    /**
	     * Initialize the contents of the frame.
	     */
	    private void initialize() {

		// Build the frame for the App
	        frame = new JFrame("Sentiment Analysis of Movie Reviews");
	        frame.setBounds(100, 100, 850, 700);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().setLayout(null);
	        
	        // Welcome Greeting
	        JLabel lblTitle_1 = new JLabel("Welcome to Sentiment Analysis of your Movie Review!");
		lblTitle_1.setBounds(50, 30, 350, 20);
	        frame.getContentPane().add(lblTitle_1);

		// Message for User to enter Movie Review
		JLabel lblTitle_2 = new JLabel("Please enter in your Movie Review:");
	        lblTitle_2.setBounds(50, 90, 250, 20);
	        frame.getContentPane().add(lblTitle_2);

		// Text area for User's movie review
	        JTextArea textArea = new JTextArea();
	        textArea.setBounds(50, 120, 500, 200);
	        frame.getContentPane().add(textArea);

		// Button for User to submit movie review of Sentiment Analysis
		JButton reviewSubmit = new JButton("Submit");
	        reviewSubmit.setBackground(Color.RED);
		reviewSubmit.setForeground(Color.WHITE);
		reviewSubmit.setBounds(50, 330, 90, 25);
		frame.getContentPane().add(reviewSubmit);

		// Clear button below text box for user Movie Review
		JButton btnClear = new JButton("Clear!");
		btnClear.setBounds(150, 330, 90, 25);
		frame.getContentPane().add(btnClear);

		JLabel dashs = new JLabel("---------------------------------------------------------"
		    + "--------------------------------------------------------------------");
		dashs.setBounds(50, 380, 900, 10);
		frame.getContentPane().add(dashs);

		// Label for User to select a movie dataset
		JLabel lblTitle_3 = new JLabel("Or, select a Movie Review Dataset.");
		lblTitle_3.setBounds(50, 400, 300, 20);
		frame.getContentPane().add(lblTitle_3);

		// Button for user to upload a dataset of movie reviews
		JButton uploadButton = new JButton("Upload");
		uploadButton.setBackground(Color.RED);
		uploadButton.setForeground(Color.WHITE);
		uploadButton.setBounds(50, 430, 90, 25);
		frame.getContentPane().add(uploadButton);

		JLabel dashs2 = new JLabel("---------------------------------------------------------"
		    + "--------------------------------------------------------------------");
		dashs2.setBounds(50, 480, 900, 10);
		frame.getContentPane().add(dashs2);

		// Label for the Results section
		resultLabel = new JLabel("Results: ");
		resultLabel.setBounds(50, 500, 100, 20);
		frame.getContentPane().add(resultLabel);

		// Label for the Type of Review of the Movie
		typeOfReviewLabel = new JLabel("Type of Review (Positive/Negative): ");
		typeOfReviewLabel.setBounds(50, 520, 300, 20);
		frame.getContentPane().add(typeOfReviewLabel);

		// TextField for the type of result of the Movie (Positive/Negative)
		typeOfReviewTextField = new JTextField(10);
		typeOfReviewTextField.setBounds(270, 520, 90, 20);
		frame.getContentPane().add(typeOfReviewTextField);

		// Label for the Accuracy of the Movie Review
		accuracyLabel = new JLabel("Accuracy: ");
		accuracyLabel.setBounds(50, 545, 300, 20);
		frame.getContentPane().add(accuracyLabel);

		// TextField for the accuracy result of the Movie Review
		accuracyTextField = new JTextField(10);
		accuracyTextField.setBounds(270, 545, 90, 20);
		frame.getContentPane().add(accuracyTextField);

		// Label for the Polarity of the Movie Review
		polarityLabel = new JLabel("Polarity (Strong/Weak): ");
		polarityLabel.setBounds(50, 570, 300, 20);
		frame.getContentPane().add(polarityLabel);

		// TextField for the polarity result of the Movie Review
		polarityTextField = new JTextField(10);
		polarityTextField.setBounds(270, 570, 90, 20);
		frame.getContentPane().add(polarityTextField);

		// Action listener for when user selects to upload a movie review dataset
		uploadButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
			JFileChooser fileChooser = new JFileChooser();
			int returnValue = fileChooser.showOpenDialog(null);
			if(returnValue == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    System.out.println(selectedFile.getName());
			}
		    }
		});

		// Action listener if the user selects submit button without writing a movie review
		reviewSubmit.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
			if(textArea.getText().isEmpty())
			    JOptionPane.showMessageDialog(null, "Please type a Movie Review");
			else	
			    JOptionPane.showMessageDialog(null, "Movie Review Submitted");
			// Action Listener for clear button
			btnClear.addActionListener(new ActionListener() {
		   	    public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
			    }
			});
		    }
		});

	        //Radio button: switch from typing to uploading review files
	}

	// Getter for the User's movie review
	public JTextArea getUserReview() {
	    return textArea;
	}
	
	// Getter for the file directory of the movie review dataset 
	public JFileChooser getFileChooser() {
	    return fileChooser;
	}

	// Meant to accept the verdict of the review.  Is it Positive or Negative...
	public void setTypeOfReview(String reviewVerdict) {
	    typeOfReviewTextField.setText(reviewVerdict);
	}

	// Meant to accept the accuracy of the Analysis
	public void setAccuracy(double accuracy) {
	    accuracyTextField.setText(Double.toString(accuracy));
	}

	// Meant to accept the polarity of the Analysis.  Strong or Weak..
	public void setPolarity(String polarity) {
	    polarityTextField.setText(polarity);
	}
}
