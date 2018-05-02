package puzzleSolver;

import java.util.List;

public interface Expendable {
	
	public <T extends Expendable> List<T> expandNode();

}
