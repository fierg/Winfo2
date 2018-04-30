package puzzleSolver;


import java.util.StringTokenizer;

/**
 * The Class puzzleSolver.
 */
public class puzzleSolver {

	/** The puzzle. */
	private static Puzzle puzzle;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		//args[0]: Hier soll das Puzzle, mit Komma als Trennzeichen oder direkt als int[][]-Array, �bergeben werden k�nnen. 
		//			( z.B.: 1,2,3,4,5,6,7,8,0 )
		//args[1]: Dieser Parameter soll den Suchalgorithmus variieren. 
		//			(1: greedy, 2: A* mit h(n) = falsch platzierten Kacheln, 3: A*  mit h(n) = Manhattan-Distanzen)
		//args[2]: �ber einen Debugging-Parameter soll gew�hlt werden k�nnen, ob eine vollst�ndige Ausgabe der Suchschritte stattfindet. 
		//			(0: nein, 1: ja)

		//chheck args:
		if(args.length != 3){
			printUsage();
			return;
		}
		//Tokenize the Puzzle and generate it
		StringTokenizer toki = new StringTokenizer(args[0], ",");
		int[][] tiles = new int[3][3];
		try {
			for (int i = 0; i < 9; i++) {
				tiles[i / 3][i % 3] = Integer.parseInt(toki.nextToken());
			}
		} catch (NumberFormatException e){
			//check args
			printUsage();
		}
		puzzle = new Puzzle(tiles);
		puzzle.printPuzzle(); //print once for the start state
		try {
			switch(Integer.parseInt(args[1])){
				//Switch the Algorithm argument and solve accordingly
				//Handle the step-printing inside the solve() Function of the Strategy
				case 1: solve(new GreedyStrategy(), Integer.parseInt(args[2])); break;
				case 2: solve(new AStar1Strategy(), Integer.parseInt(args[2]));break;
				case 3: solve(new AStar2Strategy(), Integer.parseInt(args[2]));break;
				default: printUsage();
			}
		} catch (NumberFormatException e){
			//check args
			printUsage();
		}

	}

	/**
	 * Solve.
	 *
	 * @param strategy the strategy
	 * @param printSteps the print steps
	 */
	private static void solve(SolvingStrategy strategy, int printSteps){
		strategy.solve(puzzle, printSteps);
	}

	/**
	 * Prints the usage.
	 */
	private static void printUsage() {
		System.out.println("Usage: \"Das Puzzle durch Komma getrennt (1,2,3,4,...)\" \"Suchalgorithmus (1: greedy, 2: A* wrong tiles, 3: A* manhattan\" \"Ausgabe der Suchschritte 0:nein, 1:ja\"");
	}

}