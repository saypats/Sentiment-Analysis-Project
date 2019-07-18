import java.awt.EventQueue;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Review_Form {
	 
	    private JFrame frame;
	    private JTextField textField;
	    private JTextField textField_1;
	    private JTextField textField_2;
	 
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
	        frame = new JFrame();
	        frame.setBounds(100, 100, 730, 489);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().setLayout(null);
	        
	        //Greetings, labels, instructions
	        
	        //Text-area to write reviews
	        JLabel lbReview = new JLabel("Enter Review:");
	        lbReview.setBounds(65, 162, 46, 14);
	        frame.getContentPane().add(lbReview);
	        JTextArea textArea_1 = new JTextArea();
	        textArea_1.setBounds(126, 157, 212, 40);
	        frame.getContentPane().add(textArea_1);
	        
	        //Radio button: switch from typing to uploading review files
	      
	 
	       }
	 
	}	


	
