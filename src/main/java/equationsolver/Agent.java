package equationsolver;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/*
 * Class representing agent.
 * Each agent calculates solution values for one variable.
 */
public class Agent implements Callable<Double> {

	private List<BlockingQueue<QueueData>> queues;
	private int varNumber;
	private String name;
	private String varName;
	private double[][] coeff;
	private double[] intercept;
	private int maxIterations; 
	private double errorLimit;
	private LogOutput output;

	public Agent(List<BlockingQueue<QueueData>> queues, int varNumber, String varName, double[][] coeff,
			double[] intercept, int maxIterations, double errorLimit, LogOutput output) {
		super();
		this.queues = queues;
		this.varNumber = varNumber;
		this.coeff = coeff;
		this.intercept = intercept;
		this.name = "Agent" + (varNumber + 1);
		this.varName = varName;
		this.maxIterations = maxIterations;
		this.errorLimit = errorLimit;
		this.output = output;
	}

	public Double call() {
		int c = 0;
		double value = 0.0;
		// Initial solution. All values are 0
		for (BlockingQueue<QueueData> queue : queues) {
			if (c != varNumber) {
				try {
					queue.put(new QueueData(varNumber, 0.0));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			c++;
		}

		double[] prevVal = new double[coeff.length];
		double[] values = new double[coeff.length];
		for (int iteration = 0; iteration < maxIterations; iteration++) {			
			for (int i = 0; i < coeff.length - 1; i++) {
				QueueData data = null;
				try {
					data = queues.get(varNumber).take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				values[data.getVarIndex()] = data.getValue();
			}
			values[varNumber] = value;
			// Get the value for the next iteration
			value = getValue(values);					
			
			System.out.println(Thread.currentThread().getName() + " " + name + " - calculated value for " + varName
					+ " : " + value);
			output.append(Thread.currentThread().getName() + " " + name + " iteration " + (iteration + 1)
					+ " - calculated value for " + varName + " : " + value + "\n");
			
			// Calculate error
			double error = getError(values, prevVal);
			output.append(Thread.currentThread().getName() + " " + name + " iteration " + (iteration + 1)
					+ " - calculated max error " + error + "\n");
			if(error <= errorLimit) {
				output.append(Thread.currentThread().getName() + " " + name + " iteration " + (iteration + 1)
						+ " - Error is tolerable. Ending computation.\n");
				break;
			}
						
			c = 0;
			for (BlockingQueue<QueueData> queue : queues) {
				if (c != varNumber) {
					try {
						queue.put(new QueueData(varNumber, value));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				c++;
			}
			
			for (int i = 0; i < coeff.length; i++) {
				prevVal[i] = values[i];					
			}
		}

		return value;
	}
	
	private double getValue(double[] values) {
		double sum = 0;
		for (int i = 0; i < coeff.length; i++) {
			if (i != varNumber) {
				sum += -coeff[varNumber][i] * values[i];
			}
		}
		sum += intercept[varNumber];
		return sum / coeff[varNumber][varNumber];
	}
	
	private double getError(double[] values, double[] prevValues) {
		double maxError = Double.MIN_VALUE;
		for(int i = 0; i < values.length; i++) {
			double error = Math.abs((values[i] - prevValues[i]) / values[i]);
			maxError = Math.max(maxError, error);
		}
		return maxError;
	}
}
