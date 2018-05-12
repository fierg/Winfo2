package puzzleSolver;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * The Class puzzleSolver.
 */
public class puzzleSolver {

	/** The puzzle. */
	private static Puzzle puzzle;

	//Contains all visited Puzzlestates
	private ArrayList<PuzzleNode> statesVisited = new ArrayList<>();
	//contains all states that are still open and therefore have to be visited
	private LinkedList<PuzzleNode> openStates = new LinkedList<>();
	//contains the Puzzlestates from start to the solution
	private ArrayList<Puzzle> solution = new ArrayList<>();

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
			for (int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++){
					tiles[i][j] = Integer.parseInt(toki.nextToken());
				}
			}
		} catch (NumberFormatException e){
			//check args
			printUsage();
		}
		puzzle = new Puzzle(tiles);
		//puzzle.printPuzzle("Solvable: " + puzzle.isSolvable()); //print once for the start state

		try {
			puzzleSolver solver = new puzzleSolver();
			solver.solve(puzzle, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		} catch (NumberFormatException e){
			//check args
			printUsage();
		}

		//-------_Testing
		PuzzleGenerator gen = new PuzzleGenerator();
		puzzleSolver solver = new puzzleSolver();
		for(Puzzle p: gen.generatePuzzleList(123, 50)){
			solver.solve(p, 1, 0);
			solver.solve(p, 2, 0);
			solver.solve(p, 3, 0);
		}

	}

	/**
	 * solves the Puzzle from the given startstate to the SOLUTION State defined in the Puzzle class.
	 * When the Puzzle could be solved, the states from start to the solution will be printed
	 *
	 * @param startState the puzzleState to start from
	 * @param method the method to use 1: Greedy, 2: A*WithWrongTiles, 3: A*WithManhattenDist
	 * @param printSteps whether or not to print all taken steps 1=true, 0 = false
	 * @return An ArrayList of all Puzzlestates from start to end
	 */
	public void solve(Puzzle startState, int method, int printSteps) {
		//This will contain all children from the currently inspected State
		//Each node will later construct all possible moves and append those nodes as children
		ArrayList<PuzzleNode> children;

		//initialize the solutionList
		solution = new ArrayList<Puzzle>();
		//initialize the currentState to null
		PuzzleNode currentState = null;

		//remember if a solution was found, so we can later decide if we can print it or not
		boolean solutionFound = false;

		//counts how many nodes were created
		int nodeCounter = 0;

		// Create PuzzleNode using start state and add it to the end of the open states
		openStates.offer( new PuzzleNode( startState) );

		//continue while we still have open states and the solution wasn't found
		while (!openStates.isEmpty() && solutionFound == false) {
			//Retrieve and remove the first state and start the loop:
			currentState = openStates.poll();
			nodeCounter++;

			if(printSteps == 1) {
				//print the currently inspected state if we should:
				currentState.puzzleState.printPuzzle("Current = " + nodeCounter + ", h(current) = " + currentState.puzzleState.wrongTiles());
			}

			//if isSolved() we can stop
			if (currentState.puzzleState.isSolved()) {
				solutionFound = true;
				break;
			}

			//else we need to create all children of the current state (createChildren() checks which moves are possible)
			//createChildren also sets the new value of gn for the A* Algorithm
			children = currentState.createChildren();

			//now compare each child with the visitedList and openList, to see if we already visited or want to visit that exact state:
			boolean stateVisited;
			for ( int i=0; i < children.size(); i++ ) {
				PuzzleNode child = children.get(i);
				stateVisited = false;

				for ( PuzzleNode vistedNode : statesVisited ) {
					//child has been visited, so break and continue with the next one:
					if ( child.equals( vistedNode ) ) {
						stateVisited = true;
						break;
					}
				}

				for ( PuzzleNode openNode : openStates ) {
					//child is already in the openList, so we don't need to add it again:
					if ( child.equals( openNode ) ) {
						stateVisited = true;
						break;
					}
				}


				//else the child hasn't been visited yet:
				if ( stateVisited == false ) {
					boolean inserted = false;

					//determine the score for the new found state:
					child.determineScore(method);

					//and add it in ascending order of the score, since we wan't to progress on the state with the
					//lowest score next:
					for ( int j = 0; j < openStates.size(); j++ ) {
						if ( child.score < openStates.get(j).score ) {
							openStates.add( j, child); //insert right before a child that would have a higher score
							inserted = true;
							break;
						}
					}
					//if it's the highest score yet, put it at the end:
					if ( inserted == false ) openStates.offer( child );
				}
			}

			//update the visited and open Lists acording to the currentState:
			statesVisited.add( currentState );
			openStates.remove( currentState );
		}

		if ( solutionFound == true ) {
			boolean pathFound = false;
			//to reproduce all steps from the start to the solution,
			//we can just go from the Solution Node recursively to each parent
			//and add the saved Puzzle to the SolutionList:
			solution.add( currentState.puzzleState ); //currentState still holds our solution, so begin here
			while ( pathFound == false ) {
				currentState = currentState.parent;
				//add all new states at the beginning, so we get the right order at the end:
				solution.add( 0, currentState.puzzleState );
				if ( currentState.puzzleState.equals( startState ) ) pathFound = true;
			}
			//for(Puzzle p : solution){
			//	p.printPuzzle();
			//}
			System.out.println("found a Solution with " + solution.size() + " moves, while expanding " + nodeCounter + " nodes");
		}
		else {
			// Solution wasn't found
			System.out.println( "Could not find Solution :(" );
		}
	}

	/**
	 * Prints the usage.
	 */
	private static void printUsage() {
		System.out.println("Usage: \"Das Puzzle durch Komma getrennt (1,2,3,4,...)\" \"Suchalgorithmus (1: greedy, 2: A* wrong tiles, 3: A* manhattan\" \"Ausgabe der Suchschritte 0:nein, 1:ja\"");
	}

}