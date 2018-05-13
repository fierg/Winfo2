package puzzleSolver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Puzzle.
 */
public class Puzzle {

	/** The Constant SOLUTION. */
	// This is the final state we want to reach in order to solve the puzzle
	public final static int[][] SOLUTION = { { 1, 2, 3 }, { 8, 0, 4 }, { 7, 6, 5 } };

	//Saves the current state of the puzzle
	int [][] puzzle;
	
	public Puzzle(int[][] puzzle){
		this.puzzle = puzzle;		
	}

	/**
	 * Prints the puzzle.
	 */
	public void printPuzzle() {
		for (int i = 0; i < this.puzzle.length; i++) {
			for (int j = 0; j < this.puzzle[i].length; j++) {
				System.out.print(this.puzzle[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-----");
	}

	/**
	 * Prints the puzzle together with the given strings.
	 * Each parameter gets printed in its own line
	 *
	 * @param strings all strings that should also be printed besides the puzzle
	 */
	public void printPuzzle(String... strings) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.puzzle.length; i++) {
			for (int j = 0; j < this.puzzle[i].length; j++) {
				sb.append(this.puzzle[i][j]);
				sb.append(" ");
			}
			sb.append("\n");
		}

		for (String s : strings) {
			sb.append(s);
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("-----");
		System.out.println(sb);
	}

	/**
	 * Tries to move a Tile at the specific coordinates.
	 *
	 * @param x            the x coordinate
	 * @param y            the y coordinate
	 * @return returns the number of the moved tile, else -1
	 */
	public int moveTile(int x, int y){
		//check if tile is != 0
		if (puzzle[x][y] == 0) {
			return -1;
		}
		//check for array bounds:
		if(x < 0 || y < 0 || x > puzzle.length || y > puzzle.length){
			return -1;
		} else {
            int xl, xr, yt, yb; //Variables for xleft, xright, ytop, ybottom
            //Check if an index would be out of bounds
            //If yes, put that index to the tile we want to move because
            //this won't throw an exception when reading puzzle[x][y]
            //and won't swap, because the tile itself isn't 0
            xl = Math.max(x - 1, 0);
            xr = Math.min(x + 1, puzzle.length - 1);
            yt = Math.max(y - 1, 0);
            yb = Math.min(y + 1, puzzle.length - 1);

            //check if 0 is the Neighbourtile:
            if (puzzle[x][yt] == 0) {
                swap(x, y, x, yt);
                return puzzle[x][yt];
            } else if (puzzle[xl][y] == 0) {
                swap(x, y, xl, y);
                return puzzle[xl][y];
            } else if (puzzle[xr][y] == 0) {
                swap(x, y, xr, y);
                return puzzle[xr][y];
            } else if (puzzle[x][yb] == 0) {
                swap(x, y, x, yb);
                return puzzle[x][yb];
            }
        }
        return -1;
	}

	/**
	 * swaps two Tiles. Doesn't check if one of them is 0 or out of bounds, use
	 * moveTile() for public usage
	 *
	 * @param x
	 *            the x coordinate to be swapped
	 * @param y
	 *            the y coordinate to be swapped
	 * @param x2
	 *            the x coordinate to swap with
	 * @param y2
	 *            the y coordinate to swap with
	 */
	private void swap(int x, int y, int x2, int y2) {
		int tmp = puzzle[x][y];
		puzzle[x][y] = puzzle[x2][y2];
		puzzle[x2][y2] = tmp;
	}

	/**
	 * Calculates the number of tiles that are at a wrong position.
	 *
	 * @return the number of tiles at the wrong position
	 */
	public int wrongTiles() {
		int count = 0;
		for (int i = 0; i < this.puzzle.length; i++) {
			for (int j = 0; j < this.puzzle[i].length; j++) {
				if (puzzle[i][j] != SOLUTION[i][j]) {
					count++;
				}
			}
		}
		return count;
	}
	/**
	 * Checks whether the puzzle is solved.
	 *
	 * @return returns true if all tiles are in the same spot as the puzzle defined in the constant SOLUTION
	 */
	public boolean isSolved() {
		return this.wrongTiles() == 0;
	}

	/**
	 * An equals method which compares every single number in the puzzle
	 * @param other the Puzzle to compare with
	 * @return true if all tiles are at the same position
	 */
	public boolean equals(Puzzle other) {
		boolean isEqual = true;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(this.puzzle[i][j] != (other.puzzle[i][j])){
					isEqual = false;
					return isEqual;
				}
			}
		}
		return isEqual;
	}

	/**
	 * Creates a "real" copy of the puzzle, instead of only the references
	 * @return a new Puzzle Object with the same tiles as the instance it was called on
	 */
	public Puzzle copy(){
		int[][] tiles = new int[3][3];
		for(int i = 0; i < 3; i++){
			tiles[i] = puzzle[i].clone();
		}
		return new Puzzle(tiles);
	}

	/**
	 * Manhattan dist. of any given tile to its solution tile.
	 *
	 * @param tile the tile
	 * @param puzzle the puzzle
	 * @param solutionTile the solution tile
	 * @return the int
	 */
	private int manhattanDist(int tile, int[][] puzzle, int solutionTile) {
		Point p1 = getPoint(tile, puzzle);
		Point p2 = getPoint(solutionTile, SOLUTION);

		return (int) (Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY()));
	}

	/**
	 * Gets the point coordinates of a tile
	 *
	 * @param tile the tile
	 * @param puzzle the puzzle
	 * @return the point
	 */
	private Point getPoint(int tile, int[][] puzzle) {
		int collumn = 0;
		int row = 0;
		for (int[] col : puzzle) {
			row = 0;
			for (int entry : col) {
				if (entry == tile) {
					return new Point(collumn, row);
				}
				row++;
			}
			collumn++;
		}
		return null;
	}

	/**
	 * calculates the ManhattenDistance for each tile in the puzzle and returns the sum
	 * @return the summed up manhattendistance of each tile
	 */
	public int getManhattanHeuristic() {
		int sum = 0;
		for (int[] is : puzzle) {
			for (int i : is) {
				sum += manhattanDist(i, puzzle, i);
			}
		}
		return sum;
	}

	/**
	 * calculates if the given puzzle is solvable in regard to the given solution
	 * @param solution the solutionState which should be reachable
	 * @return return true if the solution state can be reached by only moving one tile at a time
	 */
	public boolean isSolvable(int[][] solution) {
		//this method works by comparing the number of inversions in the current puzzleState
		// and the number of inversions in the solutionState
		// If both inversions are even or both ar odd, the solutionState can be reached
		if ((countInversions(this.puzzle) % 2 == 0 && countInversions(solution) % 2 == 0)
				|| ((countInversions(this.puzzle) + 1) % 2 == 0 && (countInversions(solution) + 1) % 2 == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * counts the inversions of the given puzzle
	 * @param puzzle the puzzle to check the inversions of
	 * @return the number of inversions in the puzzle
	 */
	public static int countInversions(int[][] puzzle) {
		int inversions = 0;
		// put all tiles in a one dimensional array for easy calculations:
		ArrayList<Integer> puzzleList = new ArrayList<>();
		for (int[] row : puzzle) {
			for (int i : row) {
				puzzleList.add(i);
			}
		}
		Integer[] puzzleArray = puzzleList.toArray(new Integer[puzzleList.size()]);

		for (int i = 0; i < puzzleArray.length; i++) {
			// ignore the 0 tile
			if (puzzleArray[i] == 0) {
				continue;
			}
			// sum up any inversions appearing in the other tiles:
			for (int j = i; j < puzzleArray.length; j++) {
				if (puzzleArray[j] == 0) {
					continue;
				}

				if (puzzleArray[j] < puzzleArray[i]) {
					inversions++;
				}
			}
		}

		return inversions;
	}

}