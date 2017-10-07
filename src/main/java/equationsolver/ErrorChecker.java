package equationsolver;

import java.util.Queue;

public class ErrorChecker implements Runnable{

	private Queue<Double> queue;
	private double errorLimit;
	private int numVars;
	private LogOutput output;
	private boolean errorLowEnough = false;
	
	public boolean isErrorLowEnough() {
		return errorLowEnough;
	}

	public ErrorChecker(Queue<Double> queue, double errorLimit, int numVars, LogOutput output) {
		this.queue = queue;
		this.errorLimit = errorLimit;
		this.numVars = numVars;
		this.output = output;
	}

	public void run() {
		double error = 10000.0;
		int iteration = 1;
		while(error >= errorLimit) {
			double maxError = Double.MIN_VALUE;
			synchronized(queue) {	
				if(queue.size() < numVars) {
					continue;
				}
				for(int i = 0; i < numVars; i++) {
					maxError = Math.max(maxError, queue.poll());
				}	
				output.append(Thread.currentThread().getName() +  " Errorchecker - iteration " + (iteration )
						+ " - calculated max error " + maxError + "\n");
				if(maxError <= errorLimit) {
					errorLowEnough = true;
					break;
				}
				iteration++;
				queue.notifyAll();
			}
		}
	}
}
