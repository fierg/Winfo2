package solving;

import structures.Puzzle;

public class AStarStrategy extends SolverStrategy {
    @Override
    public void solve(Puzzle puzzle) {
        while(puzzle.wrongTiles() != 0){

            //Implement algorithm

            delayAlgorithm();
        }
    }
}
