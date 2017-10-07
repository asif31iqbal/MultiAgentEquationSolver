package equationsolver;

import javax.swing.JTextArea;

public class LogOutput {
	private JTextArea area;
	public LogOutput(JTextArea area) {
		this.area = area;
	}
	
	public synchronized void append(String s) {
		area.append(s);
	}
}
