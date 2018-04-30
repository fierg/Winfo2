package puzzleSolver;

public class AStarNode implements Comparable<Object> {

	private Object NodeData;

	private int g;

	private int h;

	public AStarNode(Object object) {
		this.NodeData = object;
	}

	public Object getNodeData() {
		return NodeData;
	}

	public void setNodeData(Object nodeData) {
		NodeData = nodeData;
	}

	public int getG() {

		return g;
	}

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
			AStarNode other = (AStarNode) o;
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


}