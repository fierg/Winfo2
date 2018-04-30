package puzzleSolver;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AStar2Strategy<T> extends SolvingStrategy {
	
	Queue<T> openQueue = new PriorityQueue<>();
	
	Set<T> closedSet = new HashSet<>();
	
    @Override
    public void solve(Puzzle puzzle, int printSteps) {

    }
    
    private List<T> aStar(T source, T destination){
    	
    }
}
