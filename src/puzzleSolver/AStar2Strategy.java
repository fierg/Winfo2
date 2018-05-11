package puzzleSolver;

import java.util.*;

public class AStar2Strategy<T extends Heuristic> extends SolvingStrategy {
	//Contains all visited Puzzlestates
	private ArrayList<AStarNode<Puzzle>> statesVisited = new ArrayList<>();
	//contains all states that are still open and therefore have to be visited
	private LinkedList<AStarNode<Puzzle>> openStates = new LinkedList<>();
	//contains the Puzzlestates from start to the solution
	private ArrayList<Puzzle> solution = new ArrayList<>();

	@Override
	public ArrayList<Puzzle> solve(Puzzle puzzle, int printSteps) {

		ArrayList<AStarNode<Puzzle>> children;

		solution = new ArrayList<Puzzle>();

		AStarNode<Puzzle> currentState = null;

		boolean solutionFound = false;

		int nodeCounter = 0;

		AStarNode<Puzzle> startNode = new AStarNode<Puzzle>(puzzle);
		startNode.setG(0);
		int distance = 0;
		for (int i = 1; i < 9; i++) {
			distance += startNode.getNodeData().manhattanDist(i, startNode.getNodeData().puzzle, i);
		}
		startNode.setH(distance);
		openStates.offer(startNode);

		while (!openStates.isEmpty()) {

			currentState = openStates.poll();
			nodeCounter++;

			if (printSteps == 1) {
				currentState.getNodeData().printPuzzle("Current = " + nodeCounter + ", h(current) = " + ((Puzzle) currentState.getNodeData()).wrongTiles());
			}

			if (currentState.getNodeData().isSolved()) {
				solutionFound = true;
				break;
			}

			statesVisited.add(currentState);

			children = currentState.createChildren();

			boolean stateVisited;
			for (AStarNode<Puzzle> child : children) {

				stateVisited = false;

				for (AStarNode<Puzzle> visitedNode : statesVisited) {
					if (child.equals(visitedNode)) {
						stateVisited = true;
						break;
					}
				}

				for (AStarNode<Puzzle> openNode : openStates) {
					if (child.equals(openNode) && currentState.getG() + 1 >= openNode.getG()) {
						stateVisited = true;
						break;
					} else if (child.equals(openNode)) {
						openNode.setG(currentState.getG() + 1);
						stateVisited = true;
						break;
					}
				}

				if (!stateVisited) {
					child.parent = currentState;
					child.setG(currentState.getG() + 1);
					for (int i = 1; i < 9; i++) {
						distance += child.getNodeData().manhattanDist(i, child.getNodeData().puzzle, i);
					}
					child.setH(distance);
					openStates.offer(child);
				}
			}
		}

		if ( solutionFound == true ) {
			System.out.println("Solution found!");
			boolean pathFound = false;
			//to reproduce all steps from the start to the solution,
			//we can just go from the Solution Node recursively to each parent
			//and add the saved Puzzle to the SolutionList:
			solution.add( currentState.getNodeData() ); //currentState still holds our solution, so begin here
			while ( pathFound == false ) {
				currentState = currentState.parent;
				//add all new states at the beginning, so we get the right order at the end:
				solution.add( 0, currentState.getNodeData() );
				if ( currentState.getNodeData().equals( puzzle ) ) pathFound = true;
			}
		}
		else {
			// Solution wasn't found
			System.out.println( "Could not find Solution :(" );
		}

		return solution;
	}
}
