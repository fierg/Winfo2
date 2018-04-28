package puzzleSolver;


import java.util.StringTokenizer;

public class puzzleSolver {

	private static Puzzle puzzle;

	public static void main(String[] args) {
		//args[0]: Hier soll das Puzzle, mit Komma als Trennzeichen oder direkt als int[][]-Array, übergeben werden können. 
		//			( z.B.: 1,2,3,4,5,6,7,8,0 )
		//args[1]: Dieser Parameter soll den Suchalgorithmus variieren. 
		//			(1: greedy, 2: A* mit h(n) = falsch platzierten Kacheln, 3: A*  mit h(n) = Manhattan-Distanzen)
		//args[2]: Über einen Debugging-Parameter soll gewählt werden können, ob eine vollständige Ausgabe der Suchschritte stattfindet. 
		//			(0: nein, 1: ja)

		//chheck args:
		if(args.length != 3){
			printUsage();
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

	private static void solve(SolvingStrategy strategy, int printSteps){
		strategy.solve(puzzle, printSteps);
	}

	private static void printUsage() {
		System.out.println("Usage: \"Das Puzzle durch Komma getrennt (1,2,3,4,...)\" \"Suchalgorithmus (1: greedy, 2: A* wrong tiles, 3: A* manhattan\" \"Ausgabe der Suchschritte 0:nein, 1:ja\"");
	}

}