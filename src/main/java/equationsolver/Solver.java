package equationsolver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Solver {

	private LogOutput output;

	public Solver(LogOutput output) {
		this.output = output;
	}

	public void solve(String input) {
		int numVars = 0;
		String[] vars = null;
		int maxIterations = 0;
		double errorLimit = 0.0;
		double[][] coeff = null;
		double[] intercept = null;

		try {
			Scanner scanner = new Scanner(input);
			numVars = scanner.nextInt();
			vars = new String[numVars];
			for (int i = 0; i < numVars; i++) {
				vars[i] = scanner.next();
			}
			maxIterations = scanner.nextInt();
			errorLimit = scanner.nextDouble();
			coeff = new double[numVars][numVars];
			intercept = new double[numVars];
			for (int i = 0; i < numVars; i++) {
				for (int j = 0; j < numVars; j++) {
					coeff[i][j] = scanner.nextDouble();
				}
				intercept[i] = scanner.nextDouble();
				;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Show the variables in the log
		output.append(numVars + " variables found: ");
		String varNames = "";
		for (int i = 0; i < vars.length; i++) {
			if (i < vars.length - 1) {
				varNames += vars[i] + ", ";
			} else {
				varNames += vars[i];
			}
		}
		output.append(varNames + "\n");

		// Spin up n number of threads as agents if n number of variables found.
		ExecutorService executor = Executors.newFixedThreadPool(10);

		List<Callable<Double>> tasks = new ArrayList<Callable<Double>>();
		List<BlockingQueue<QueueData>> queues = new ArrayList<BlockingQueue<QueueData>>();

		// Add n number of blocking queues
		for (int i = 0; i < numVars; i++) {
			queues.add(new ArrayBlockingQueue<QueueData>(1024));
		}

		for (int i = 0; i < numVars; i++) {
			Agent agent = new Agent(queues, i, vars[i], coeff, intercept, maxIterations, errorLimit, output);
			tasks.add(agent);
		}
		output.append(numVars + " agents created.\n");
		output.append("Maximum iterations: " + maxIterations + "\n");
		output.append("Error limit: " + errorLimit + "\n\n");

		List<Future<Double>> futures = new ArrayList<Future<Double>>();
		for (Callable<Double> task : tasks) {
			Future<Double> future = executor.submit(task);
			futures.add(future);
		}

		int c = 0;
		for (Future<Double> future : futures) {
			try {
				Double d = future.get();
				System.out.println(vars[c] + " = " + d);
				output.append(vars[c] + " = " + d + "\n");
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			c++;
		}

		executor.shutdown();
	}

}
