import Jama.Matrix; // Enables creation of matrix
/** PageRank class to calculate pageRank values.
 * Use JAMA Library :
 * http://math.nist.gov/javanumerics/jama/
 * @author Henrard & Outters
 */
public class PageRank {

	public static final int PRECISION = 100;// gives a .00 precision

	private Matrix matrix;
	private int size;

	public PageRank(double[][] m, double[][] d, double prob) {
		this.matrix = new Matrix(m);
		this.size = matrix.getRowDimension(); // we only need the row dimension
												// as the number of columns will
												// be the same

		// Compute
		Matrix p = transitionMatrix(new Matrix(d), prob);
		Matrix result = powerMethod(p);
		// will print the matrix to stdout
		result.print(0, 5); // 0 is the column's size and 5 the number of
							// decimals
	}

	/**
	 * Will sum up the leap probabilities and link probabilities matrices
	 * 
	 * @param degrees degree of each line
	 * 
	 * @param probability alapha
	 */
	private Matrix transitionMatrix(Matrix degrees, double probability) {
		// creation of leapProbabilities and initializing all of its elements
		Matrix leapProbabilities = new Matrix(size, size, (1 - probability) / size);
		// creation of linkProbabilities. Initialization of element will be done
		// in the loop below
		Matrix linkProbabilities = new Matrix(size, size);
		// loop to compute the linkProbabilities matrix
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// If degree = 0, set value to 1
				double degree = (degrees.get(i, 0) == 0) ? 1 : degrees.get(i, 0);

				double linkValue = matrix.get(i, j) * (probability / degree);
				linkProbabilities.set(i, j, linkValue);
			}
		}
		// sums up the two matrices resulting in the transition matrix
		return leapProbabilities.plus(linkProbabilities);
	}

	/**
	 * Will create the matrix that contains the results
	 * 
	 * @param transition Matrix of transition calculed previosly 
	 */
	private Matrix powerMethod(Matrix transition) {
		// creation of a new matrix row where the first element is equal to 1
		Matrix rank = new Matrix(1, size, 0);
		rank.set(0, 0, 1);

		// saving of the previous rank
		double previousRank[][] = new double[1][size];
		
		int nbIteration = 0;
		boolean accurate = false;
		while (!accurate) {
			nbIteration++;

			// multiply the rank matrix by the transition matrix
			rank = rank.times(transition);

			int n = 0;
			double result[][] = rank.getArray();
			// iterate over the result array until the end of it
			for (int i = 0; i < result[0].length; i++) {
				if ((int) (result[0][i] * PRECISION) == (int) (previousRank[0][i] * PRECISION)) {
					n++;
				}
			}
			previousRank = rank.getArray();
			if (n == result[0].length)
				accurate = true; // = break

		}
		System.out.println("Nombre d'iteration pour la power-method: " + nbIteration);
		return rank;
	}

}
