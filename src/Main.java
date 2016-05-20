import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class
 * Load the .csv file
 * Convert data to matrix
 * 
 * @author Henrard & Outters
 */
public class Main {

	private static double[][] matrix;
	private static double[][] degrees;

	/**
	 * Command line management Call to PageRank constructor
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		double probability = 1;
		File fin = null;
		// getOpt for command line
		switch (args.length) {
		case 0:
			// In the current directory
			load("matrix.csv");
			break;
		case 1:
			if (args[0].matches("^\\d+(\\.\\d+)?")) {
				try {
					probability = Double.parseDouble(args[1]);
				} catch (Exception e) {
					System.err.println("Unexpected character for the teleportation value : " + args[0]);
					System.exit(-1);
				}
			} else {
				fin = new File(args[0]);
				if (fin.exists() && !fin.isDirectory())
					load(args[0]);
			}
			break;
		case 2:
			try {
				probability = Double.parseDouble(args[0]);
			} catch (Exception e) {
				System.err.println("Unexpected character for the teleportation value : " + args[0]);
				System.exit(-1);
			}
			fin = new File(args[1]);
			if (fin.exists() && !fin.isDirectory())
				load(args[1]);
			break;
		default:
			System.err.println("Usage: main [teleportation] [fin]");
			System.exit(-1);
			break;
		}

		// Call to PageRank constructor
		new PageRank(matrix, degrees, probability);
	}

	/**
	 * Load file into memory Transform file into a list of strings as a list
	 * does not have a precise length
	 * 
	 * @param filename name of the csv file.
	 */
	private static void load(String filename) {
		// We use a list of strings because we don't know the number of lines in
		// the file
		List<String> lines = new ArrayList<String>();
		String newline = null;
		BufferedReader reader;
		try {
			// Open with buffer
			reader = new BufferedReader(new FileReader(new File(filename)));
			// Read file line after line until reaching EOF
			while ((newline = reader.readLine()) != null) {
				lines.add(newline);
				newline = null;
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("I/O error with : " + filename);
			System.exit(-1);
		}

		// Call to method that will convert the list of strings into a matrix
		convertToMatrix(lines);
	}

	/**
	 * Convert the list of string to a matrix Convert all strings to double as
	 * requested by the library
	 * 
	 * @param lines string extracted from the file
	 */
	private static void convertToMatrix(List<String> lines) {
		// Instanciation of static variables
		matrix = new double[lines.size()][];
		// Degrees is a matrix containing the sum of the value of the links for
		// every row
		degrees = new double[lines.size()][];

		for (int i = 0; i < lines.size(); i++) {
			String tabLine[] = lines.get(i).split(",");
			double matrixLine[] = new double[tabLine.length];
			double sumOfLine = 0;
			try {
				for (int j = 0; j < tabLine.length; j++) {
					double n = Double.parseDouble(tabLine[j]);
					matrixLine[j] = n;
					sumOfLine += n;
				}
			} catch (NumberFormatException e) {
				System.err.println("The matrix has a wrong element, at line :\n" + lines.get(i));
				System.exit(-1);
			}

			matrix[i] = matrixLine;
			double t[] = { sumOfLine };
			degrees[i] = t;

		}
	}

}
