package puzzleSolver;

import java.util.ArrayList;

/**
 * a PuzzleNode represents a possible state of the Puzzle
 * it contains the Puzzle itself,
 * the herusticScore hn of the State
 * and a reference to the parent to reconstruct the solution path
 */
public class PuzzleNode implements Comparable<PuzzleNode>{
    Puzzle puzzleState; // saves the puzzleState
    int hn, fn, gn;     // saves the values for h(n), f(n), g(n)
    int score;          // saves the score used for evaluation the best move
    int movedTile;      // saves the tile that was moved to reach this node
    PuzzleNode parent;  // saves the parentnode which holds the previous state

    /**
     * @param state the puzzleState of the node
     */
    public PuzzleNode(Puzzle state) {
        puzzleState = state;
    }

    /**
     * @param state the puzzleState of the node
     */
    public PuzzleNode(Puzzle state, int movedTile) {
        puzzleState = state;
        this.movedTile = movedTile;
    }

    /**
     * creates all possible children (so all possible moves from this state) and appends them to the current node
     * @return an ArrayList of PuzzleNodes with all possible moves you can make
     */
    public ArrayList<PuzzleNode> createChildren(){
        ArrayList<PuzzleNode> children = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                // copy the puzzleState, so we can just move tiles and add the new state
                Puzzle newPuzzle = puzzleState.copy();
                // try to move every tile (moveTile() handles invalid moves)
                int tile = newPuzzle.moveTile(i, j);
                if(tile > 0){
                    // if a tile was moved, create a node with the newly created puzzleState
                    PuzzleNode newChild = new PuzzleNode(newPuzzle, tile);
                    // set the parent
                    newChild.parent = this;
                    // set the gn value
                    newChild.gn = this.gn + 1;
                    // and add it as child
                    children.add(newChild);
                }
            }
        }
        // return all new child, so the solver can keep working on them
        return children;
    }

    /**
     * a simple method which calls equal on the puzzleState of both nodes
     * @see Puzzle
     * @param other the other node
     * @return true, if the puzzleStates are the same
     */
    public boolean equals(Object other) {
        return this.puzzleState.equals(((PuzzleNode)other).puzzleState);
    }

    /**
     * calculates the score of the current puzzleState
     * assumes taht gn is already set, if A* is used
     */
    public void determineScore(int method){
        // switch the method and use the right heuristic,
        // also compute fn for the A* algorithm.
        switch(method){
            case 1: score = hn = puzzleState.wrongTiles(); break;
            case 2: hn = puzzleState.wrongTiles(); score = hn + gn; fn = hn + gn; break;
            case 3: hn = puzzleState.getManhattanHeuristic(); score = hn + gn; fn = hn + gn; break;
        }
    }

    /**
     * Compares two puzzleNodes by comparing the score
     * If two scores are the same, the movedTile gets compared
     * @param o the puzzleNode to compare this with
     * @return 1, if this score is greater, -1 if it's lower, else 0. If scores are the same, the movedTile will be compared in the same way
     */
    @Override
    public int compareTo(PuzzleNode o) {
        if(this.score > o.score){
            return 1;
        } else if ( this.score < o.score){
            return -1;
        } else {
            // puzzleScore is the same, so we want to compare the number that was moved
            if(this.movedTile > o.movedTile){
                return 1;
            } else if( this.movedTile < o.movedTile){
                return -1;
            } else {
                return 0;
            }
        }
    }
}
