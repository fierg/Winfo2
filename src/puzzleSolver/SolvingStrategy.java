package puzzleSolver;

import java.util.ArrayList;

public abstract class SolvingStrategy {
    /**
     * Solves the given puzzle according to the strategy the class implements
     * @param puzzle the puzzle to be solved
     * @param printSteps whether or not to print all taken steps 1=true, 0 = false
     */
    public abstract ArrayList<Puzzle> solve(Puzzle puzzle, int printSteps);
}
