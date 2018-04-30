package puzzleSolver;

import java.util.ArrayList;
import java.util.LinkedList;

public class GreedyStrategy extends SolvingStrategy {

    private ArrayList<PuzzleNode> statesVisited = new ArrayList<PuzzleNode>();
    private LinkedList<PuzzleNode> openStates = new LinkedList<PuzzleNode>();
    private ArrayList<Puzzle> solution = new ArrayList<Puzzle>();

    public ArrayList<Puzzle> solve(Puzzle startState, int printSteps) {
        ArrayList<PuzzleNode> children;
        solution = new ArrayList<Puzzle>();
        PuzzleNode currentState = null;

        boolean solutionFound = false, stateVisited = false;
        int nodeCounter = 0;

        // Create PuzzleNode using start state and a GreedyHeuristic and offer it to the end of the open states queue
        openStates.offer( new PuzzleNode( startState) );

        // Continue search while there are still nodes to be searched AND a solution hasn't been found
        while (!openStates.isEmpty() && solutionFound == false) {
            // Poll from head of openStates = currentState
            currentState = openStates.poll();
            nodeCounter++;

            if(printSteps == 1) {
                System.out.println("Current = " + nodeCounter + ", h(current) = " + currentState.puzzleState.wrongTiles());
                currentState.puzzleState.printPuzzle();
            }

            // If currentState == goalState, break and record path
            if (currentState.puzzleState.wrongTiles() == 0) {
                solutionFound = true;
                break;
            }

            // Else, poll currentState for child nodes
            children = currentState.createChildren();
            for ( int i=0; i < children.size(); i++ ) {
                // Instead of using list.contains( child ), the list is manually searched

                PuzzleNode child = children.get(i);
                stateVisited = false;

                // Check whether each child node hasn't already been visited
                for ( PuzzleNode newNode : statesVisited ) {
                    // If statesVisited already contains child
                    if ( child.equals( newNode ) ) {
                        stateVisited = true;
                        break;
                    }
                }

                // Check whether each child node hasn't already been opened, but hasn't been searched
                for ( PuzzleNode newNode : openStates ) {
                    // If openStates already contains child
                    if ( child.equals( newNode ) ) {
                        stateVisited = true;
                        break;
                    }
                }


                // CHILD HAS NOT BEEN VISITED
                if ( stateVisited != true ) {
                    boolean inserted = false;

                    // Calculate heuristic of child, now that it is needed
                    child.determineHeuristic();

                    // Add to openStates in ascending (lowest first) order of heuristic value
                    for ( int j = 0; j < openStates.size(); j++ )
                    {
                        if ( child.hn < openStates.get(j).hn )
                        {
                            openStates.add( j, child);
                            inserted = true;
                            break;
                        }
                    }

                    if ( inserted == false ) openStates.offer( child );
                }
            }

            // Add currentState to statesVisited, remove currentState from openStates
            statesVisited.add( currentState );
            openStates.remove( currentState );
        }

        if ( solutionFound == true ) {
            System.out.println("Solution found!");
            boolean pathFound = false;
            // Recreate path to solution
            // At this point, currentState is the goal
            // Concatenate parent-child chain until startState is reached

            // Add current state to solution list
            // Recursively prepend parent of current state to solution list
            // End recursion when currentState = startState
            solution.add( currentState.puzzleState );
            while ( pathFound == false ) {
                currentState = currentState.parent;
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
