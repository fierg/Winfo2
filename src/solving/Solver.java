package solving;

import structures.Puzzle;

public class Solver {

    private SolverStrategy strategy;
    private int delay;

    public Solver(int delayBetweenSteps, SolverStrategy strategy){
        this.strategy = strategy;
        strategy.setDelay(delay);
    }

    public void solve(Puzzle puzzle){
        strategy.solve(puzzle);
    }

}
