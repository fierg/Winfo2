package solving;

import structures.Puzzle;

public abstract class SolverStrategy {

    private int delay;

    public abstract void solve(Puzzle puzzle);

    public void delayAlgorithm(){
        if(delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

}
