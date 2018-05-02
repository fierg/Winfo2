package puzzleSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AStar2Strategy<T extends Heuristic> extends SolvingStrategy {
	
    @Override
    public ArrayList<Puzzle> solve(Puzzle puzzle, int printSteps) {
		return null;

    }
    
    private List<T> aStar(T source, T destination){
    	Queue<T> openQueue = new PriorityQueue<>();    	
    	Set<T> closedSet = new HashSet<>();
    	openQueue.add(source);
    	
    	while(!openQueue.isEmpty()) {
    		T currentNode = openQueue.poll();
    		
    		
    	}
    	return null;
    }
}
