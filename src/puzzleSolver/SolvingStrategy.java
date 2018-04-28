package puzzleSolver;

public abstract class SolvingStrategy {
    /**
     * Solves the given puzzle according to the strategy the class implements
     * @param puzzle the puzzle to be solved
     * @param printSteps whether or not to print all taken steps 1=true, 0 = false
     */
    public abstract void solve(Puzzle puzzle, int printSteps);
}
