package puzzleSolver;

import java.util.ArrayList;

/**
 * a PuzzleNode represents a possible state of the Puzzle
 * it contains the Puzzle itself,
 * the herusticScore hn of the State
 * and a reference to the parent to reconstruct the solution path
 */
public class PuzzleNode {
    Puzzle puzzleState;
    int hn;
    PuzzleNode parent;

    /**
     *
     * @param state the puzzleState of the node
     */
    public PuzzleNode(Puzzle state) {
        puzzleState = state;
    }

    /**
     * creates all possible children (so all possible moves from this state) and appends them to the current node
     * @return an ArrayList of PuzzleNodes with all possible moves you can make
     */
    public ArrayList<PuzzleNode> createChildren(){
        ArrayList<PuzzleNode> children = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                //copy the puzzleState
                Puzzle newPuzzle = puzzleState.copy();
                //try to move every tile (makeTile() handles invalid moves)
                if(newPuzzle.moveTile(i, j) > 0){
                    //if a tile was moved, create a node with the newly created puzzleState
                    PuzzleNode newChild = new PuzzleNode(newPuzzle);
                    //set the parent
                    newChild.parent = this;
                    //and add it as child
                    children.add(newChild);
                }
            }
        }
        //return all new child, so the solver can keep working on them
        return children;
    }

    /**
     * a simple method which calls equal on the puzzleState of both nodes
     * @see Puzzle
     * @param other the other node
     * @return true, if the puzzleStates are the same
     */
    public boolean equals(PuzzleNode other) {
        return this.puzzleState.equals(other.puzzleState);
    }

    /**
     * calculates the heuristic score of the current puzzleState
     * for GreedySearch this is the amount of wrong tiles
     */
    public void determineHeuristic() {
        hn = puzzleState.wrongTiles();
    }
}
