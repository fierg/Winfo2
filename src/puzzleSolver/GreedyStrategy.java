package puzzleSolver;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implements the GreedyStrategy for solving the 8Puzzle
 * The Puzzle is represented as an 2dimensional int Array which holds all the numbers of each tile
 * For Solving the class uses PuzzleNodes, which each contain a unique Puzzlestate,
 * the tile that was moved to get to the state and the heuristic score of the state
 */
public class GreedyStrategy extends SolvingStrategy {

    //Contains all visited Puzzlestates
    private ArrayList<PuzzleNode> statesVisited = new ArrayList<>();
    //contains all states that are still open and therefore have to be visited
    private LinkedList<PuzzleNode> openStates = new LinkedList<>();
    //contains the Puzzlestates from start to the solution
    private ArrayList<Puzzle> solution = new ArrayList<>();

    /**
     * solves the Puzzle from the given startstate to the SOLUTION State defined in the Puzzle class.
     *
     * @param startState the puzzleState from which the GreedySearch should start
     * @param printSteps whether or not to print all taken steps 1=true, 0 = false
     * @return An ArrayList of all Puzzlestates from start to end
     */
    public ArrayList<Puzzle> solve(Puzzle startState, int printSteps) {
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

                    //determine the heuristic score for the new found state:
                    child.determineHeuristic();

                    //and add it in ascending order of the score, since we wan't to progress on the state with the
                    //lowest score next:
                    for ( int j = 0; j < openStates.size(); j++ ) {
                        if ( child.hn < openStates.get(j).hn ) {
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
            System.out.println("Solution found!");
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
        }
        else {
            // Solution wasn't found
            System.out.println( "Could not find Solution :(" );
        }

        return solution;
    }

}
