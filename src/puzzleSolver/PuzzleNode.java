package puzzleSolver;

import java.util.ArrayList;

public class PuzzleNode {
    Puzzle puzzleState;
    int hn;
    PuzzleNode parent;

    public PuzzleNode(Puzzle state) {
        puzzleState = state;
    }

    public ArrayList<PuzzleNode> createChildren(){
        ArrayList<PuzzleNode> children = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Puzzle newPuzzle = puzzleState.copy();
                if(newPuzzle.moveTile(i, j) > 0){
                    PuzzleNode newChild = new PuzzleNode(newPuzzle);
                    newChild.parent = this;
                    children.add(newChild);
                }
            }
        }
        return children;
    }

    public boolean equals(PuzzleNode other) {
        return this.puzzleState.equals(other.puzzleState);
    }

    public void determineHeuristic() {
        hn = puzzleState.wrongTiles();
    }
}
