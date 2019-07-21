import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import javax.swing.JFrame;
import javax.swing.JLabel;

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
	        frame.setBounds(100, 100, 850, 700);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().setLayout(null);
	        
	        //Greetings, labels, instructions
	        JLabel lblTitle_1 = new JLabel("Welcom to Sentiment Analysis of your Movie Review!");
	        lblTitle_1.setBounds(90, 100, 350, 20);
	        frame.getContentPane().add(lblTitle_1);

		JLabel lblTitle_2 = new JLabel("Please enter your Movie Review:");
	        lblTitle_2.setBounds(90, 140, 250, 20);
	        frame.getContentPane().add(lblTitle_2);
	        
	        JTextArea textArea = new JTextArea();
	        textArea.setBounds(90, 170, 500, 200);
	        frame.getContentPane().add(textArea);

		JButton reviewSubmit = new JButton("Submit");
	        reviewSubmit.setBackground(Color.RED);
		reviewSubmit.setForeground(Color.WHITE);
		reviewSubmit.setBounds(90, 380, 90, 25);
		frame.getContentPane().add(reviewSubmit);
	        
		// Clear button below text box for user Movie Review
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(190, 380, 90, 25);
		frame.getContentPane().add(btnClear);

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
}
