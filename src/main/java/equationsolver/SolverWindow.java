package equationsolver;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class SolverWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SolverWindow window = new SolverWindow();
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
	public SolverWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 913, 466);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);		
		
		JLabel inputLabel = new JLabel("Enter input here");
		inputLabel.setBounds(19, 16, 122, 16);
		frame.getContentPane().add(inputLabel);
		
		JLabel lblAgentLogs = new JLabel("Logs");
		lblAgentLogs.setBounds(287, 16, 122, 16);
		frame.getContentPane().add(lblAgentLogs);
		
		JScrollPane inpuScrollPane = new JScrollPane();
		inpuScrollPane.setBounds(19, 44, 249, 288);
		frame.getContentPane().add(inpuScrollPane);
		
		JTextArea inputArea = new JTextArea();
		inpuScrollPane.setViewportView(inputArea);
		
		JScrollPane logScrollPane = new JScrollPane();
		logScrollPane.setBounds(287, 44, 606, 380);
		frame.getContentPane().add(logScrollPane);
		
		JTextArea logArea = new JTextArea();
		logArea.setEditable(false);
		logScrollPane.setViewportView(logArea);	
		
		JButton calcButton = new JButton("Calculate");
		calcButton.addActionListener((e) -> {
			logArea.setText("");
			calcButton.setEnabled(false);
			Solver solver = new Solver(new LogOutput(logArea));
			solver.solve(inputArea.getText());
			calcButton.setEnabled(true);
		});
		calcButton.setBounds(94, 358, 117, 29);
		frame.getContentPane().add(calcButton);
	}
}
