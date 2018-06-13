package puzzleSolver;

import java.util.*;

/**
 * The Class puzzleSolver.
 */
public class puzzleSolver {

	/** The puzzle. */
	private static Puzzle puzzle;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// args[0]: Hier soll das Puzzle, mit Komma als Trennzeichen oder direkt
		// als int[][]-Array, �bergeben werden k�nnen.
		// ( z.B.: 1,2,3,4,5,6,7,8,0 )
		// args[1]: Dieser Parameter soll den Suchalgorithmus variieren.
		// (1: greedy, 2: A* mit h(n) = falsch platzierten Kacheln, 3: A* mit
		// h(n) = Manhattan-Distanzen)
		// args[2]: �ber einen Debugging-Parameter soll gew�hlt werden k�nnen,
		// ob eine vollst�ndige Ausgabe der Suchschritte stattfindet.
		// (0: nein, 1: ja)

		// chheck args:
		if (args.length != 3) {
			usage();
		}
		// Tokenize the Puzzle and generate it
		StringTokenizer toki = new StringTokenizer(args[0], ",");
		int[][] tiles = new int[3][3];
		try {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					tiles[i][j] = Integer.parseInt(toki.nextToken());
				}
			}
			// check args:
		} catch (NumberFormatException e) {
			System.out.println("Please use numbers for the puzzle tiles!");
			usage();
		} catch (NoSuchElementException e){
			System.out.println("Please specify 9 numbers for the tiles and separate them with ','");
			usage();
		} catch (Exception e){
			usage();
		}
		puzzle = new Puzzle(tiles);
		boolean isSolvable = puzzle.isSolvable(Puzzle.SOLUTION);
		puzzle.printPuzzle("Solvable: " + isSolvable); // print once for the
														// start state

		if (isSolvable) {
			try {
				puzzleSolver solver = new puzzleSolver();
				solver.solve(puzzle, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			} catch (NumberFormatException e) {
				// check args
				usage();
			}
		} else {
			System.exit(0);
		}


		// ----------Testing----------------
		/*
		PuzzleGenerator gen = new PuzzleGenerator();
		puzzleSolver solver = new puzzleSolver();

		for (Puzzle p : gen.generatePuzzleList(123, 20)) {
			isSolvable = p.isSolvable(p.SOLUTION);
			p.printPuzzle("Solvable: " + isSolvable);
			if (isSolvable) {
				solver.solve(p.copy(), 1, 0);
				solver.solve(p.copy(), 2, 0);
				solver.solve(p.copy(), 3, 0);
			}
		}*/

	}

	/**
	 * solves the Puzzle from the given startstate to the SOLUTION State defined
	 * in the Puzzle class. When the Puzzle could be solved, the states from
	 * start to the solution will be printed
	 *
	 * @param startState
	 *            the puzzleState to start from
	 * @param method
	 *            the method to use 1: Greedy, 2: A*WithWrongTiles, 3:
	 *            A*WithManhattenDist
	 * @param printSteps
	 *            whether or not to print all taken steps 1=true, 0 = false
	 * @return An ArrayList of all Puzzlestates from start to end
	 */
	public void solve(Puzzle startState, int method, int printSteps) {
		// This will contain all children from the currently inspected State
		// Each node will later construct all possible moves and append those
		// nodes as children
		ArrayList<PuzzleNode> children;

		// Contains all visited Puzzlestates
		ArrayList<PuzzleNode> statesVisited = new ArrayList<>();
		// contains all states that are still open and therefore have to be visited
		PriorityQueue<PuzzleNode> openStates = new PriorityQueue<PuzzleNode>();
		// contains the Puzzlestates from start to the solution
		ArrayList<Puzzle> solution = new ArrayList<>();

		// initialize the currentState to null
		PuzzleNode currentState = null;

		// remember if a solution was found, so we can later decide if we can
		// print it or not
		boolean solutionFound = false;

		// counts how many nodes were created
		int nodeCounter = 0;

		// Create PuzzleNode using start state and add it to the end of the open
		// states
		openStates.add(new PuzzleNode(startState));

		// continue while we still have open states and the solution wasn't found
		// start time measurement here
		System.out.println("Starting to solve with method " + method);
		long startTime = System.nanoTime();
		while (!openStates.isEmpty() && solutionFound == false) {
			// Retrieve and remove the first state and start the loop:
			currentState = openStates.poll();
			nodeCounter++;

			if (printSteps == 1) {
				// print the currently inspected state if we should:
				if (currentState.gn > 20) {
					currentState.puzzleState.printPuzzle(
							"Current = " + nodeCounter + ", score(current) = " + currentState.score,"h(current) = " + currentState.hn, "g(current) = " + currentState.gn, "f(current) = " + currentState.fn);

				}
			}
			// if isSolved() we can stop
			if (currentState.puzzleState.isSolved()) {
				solutionFound = true;
				break;
			}

			// else we need to create all children of the current state
			// (createChildren() checks which moves are possible)
			// createChildren also sets the new value of gn for the A* Algorithm
			children = currentState.createChildren();

			// now compare each child with the visitedList and openList, to see
			// if we already visited or want to visit that exact state:
			boolean stateVisited;
			// iterate through the children
			for (int i = 0; i < children.size(); i++) {
				PuzzleNode child = children.get(i);

				// check if it already has been visited:
				if(openStates.contains(child) || statesVisited.contains(child)){
				} else {
					// else the child hasn't been visited yet:
					// determine the score for the new found state:
					child.determineScore(method);

					// and add it in ascending order of the score, since we
					// wan't to progress on the state with the
					// lowest score next:
					openStates.add(child);
				}
			}

			// update the visited and open Lists according to the currentState:
			statesVisited.add(currentState);
			openStates.remove(currentState);
		}

		// if the while-loop stopped, there are two possibilities
		// The solution was found or all possible 9! States were checked and no solution was found
		// (which shouldn't happen, since the "isSolvable()" Method has to yield true before the algorithm start)
		// However just in case, check if the solution was found:
		if (solutionFound == true) {
			// stop time here, since the algorithm already finished:
			long elapsedTime = System.nanoTime() - startTime;
			// to reproduce all steps from the start to the solution,
			// we can just go from the Solution Node to each parent
			// and add the PuzzleStates to the SolutionList:
			solution.add(currentState.puzzleState); // currentState still holds our solution, so begin here
			boolean pathFound = false;
			while (pathFound == false) {
				currentState = currentState.parent;
				// add all new states at the beginning, so we get the right
				// order at the end:
				solution.add(0, currentState.puzzleState);
				if (currentState.puzzleState.equals(startState))
					//startState has been reached, so we can stop:
					pathFound = true;
			}
			//print the solution:
			 for(Puzzle p : solution){
			 	p.printPuzzle();
			 }
			 //print all information we gathered during the algorithm:
			int minutes = (int)(elapsedTime/1000000000d) / 60;
			double seconds = elapsedTime/1000000000d % 60;
			System.out.println(
					"found a Solution with method " + method + ": " + (solution.size()-1) + " moves, while visiting " + nodeCounter + " nodes" + "\nIt took " + minutes + " minutes and " + seconds + " seconds");
		} else {
			// else print that the solution wasn't found
			System.out.println("Could not find Solution :(");
		}
	}

	/**
	 * Prints the usage.
	 */
	private static void usage() {
		System.out.println(
				"Usage: \"Das Puzzle durch Komma getrennt (1,2,3,4,...)\" \"Suchalgorithmus (1: greedy, 2: A* wrong tiles, 3: A* manhattan\" \"Ausgabe der Suchschritte 0:nein, 1:ja\"");
		System.exit(0);
	}

}