package puzzleSolver;

import java.awt.*;
import java.util.ArrayList;

public class AStarNode<T extends Expendable> implements Comparable<Object> {

	private T NodeData;

	private int g;

	private int h;

	AStarNode<Puzzle> parent;

	public AStarNode(T object) {
		this.NodeData = object;
	}

	public T getNodeData() {
		return NodeData;
	}

	public void setNodeData(T nodeData) {
		NodeData = nodeData;
	}

	public int getG() { return g; }

	public int getF() {
		return getG() + getH();
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public void setG(int g) {
		this.g = g;
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof AStarNode) {
			@SuppressWarnings("unchecked")
			AStarNode<? extends Expendable> other = ((AStarNode<? extends Expendable>) o);
			if(getF() > other.getF()) {
				return -1;
			} else if (getF() < other.getF()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			throw new IllegalArgumentException("Given Object not of type AStarNode!");
		}
	}

	/**
	 * creates all possible children (so all possible moves from this state) and appends them to the current node
	 * @return an ArrayList of PuzzleNodes with all possible moves you can make
	 */
	public ArrayList<AStarNode<Puzzle>> createChildren(){
		ArrayList<AStarNode<Puzzle>> children = new ArrayList<>();
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				//copy the puzzleState
				Puzzle newPuzzle = ((Puzzle) NodeData).copy();
				//try to move every tile (makeTile() handles invalid moves)
				if(newPuzzle.moveTile(i, j) > 0){
					//if a tile was moved, create a node with the newly created puzzleState
					AStarNode<Puzzle> newChild = new AStarNode<Puzzle>(newPuzzle);
					//set the parent
					newChild.parent = (AStarNode<Puzzle>) this;
					//and add it as child
					children.add(newChild);
				}
			}
		}
		//return all new child, so the solver can keep working on them
		return children;
	}


}
