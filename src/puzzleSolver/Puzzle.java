package puzzleSolver;

import java.awt.Point;

/**
 * The Class Puzzle.
 */
public class Puzzle {

	/** The Constant SOLUTION. */
	// This is the final state we want to reach in order to solve the puzzle
	public final static int[][] SOLUTION = { { 1, 2, 3 }, { 8, 0, 4 }, { 7, 6, 5 } };

	/** The puzzle. */
	// Saves the current state of the puzzle
	int[][] puzzle;

	/**
	 * Instantiates a new puzzle.
	 *
	 * @param puzzle the puzzle
	 */
	public Puzzle(int[][] puzzle) {
		this.puzzle = puzzle;
	}

	/**
	 * Prints the puzzle.
	 */
	// prints the puzzle to console
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
	 * Prints the puzzle.
	 *
	 * @param functionValues the function values
	 */
	// prints the puzzle to console and adds the given function+value strings
	public void printPuzzle(String... functionValues) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.puzzle.length; i++) {
			for (int j = 0; j < this.puzzle[i].length; j++) {
				sb.append(this.puzzle[i][j]);
				sb.append(" ");
			}
			sb.append("\n");
		}

		for (String s : functionValues) {
			sb.append(s);
			sb.append(" ");
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
	 * @return returns true, if the tile was moved
	 */
	public boolean moveTile(int x, int y) {
		// check if tile is != 0
		if (puzzle[y][x] == 0) {
			return false;
		}
		// check for array bounds:
		if (x < 0 || y < 0 || x > puzzle.length || y > puzzle.length) {
			return false;
		} else {
			int xl, xr, yt, yb; // Variables for xleft, xright, ytop, ybottom
			// Check if an index would be out of bounds
			// If yes, put that index to the tile we want to move because
			// this won't throw an exception when reading puzzle[x][y]
			// and won't swap, because the tile itself isn't 0
			xl = Math.max(x - 1, 0);
			xr = Math.min(x + 1, puzzle.length);
			yt = Math.max(y - 1, 0);
			yb = Math.min(y + 1, puzzle.length);

			// check if 0 is the Neighbourtile:
			if (puzzle[yt][x] == 0) {
				swap(x, y, x, yt);
				return true;
			} else if (puzzle[y][xl] == 0) {
				swap(x, y, xl, y);
				return true;
			} else if (puzzle[y][xr] == 0) {
				swap(x, y, xr, y);
				return true;
			} else if (puzzle[yb][x] == 0) {
				swap(x, y, x, yb);
				return true;
			}
		}
		return false;
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
		int tmp = puzzle[y][x];
		puzzle[y][x] = puzzle[y2][x2];
		puzzle[y2][x2] = tmp;
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
	 * Manhattan dist. of any given tile to its solution tile.
	 *
	 * @param tile the tile
	 * @param puzzle the puzzle
	 * @param solutionTile the solution tile
	 * @return the int
	 */
	public int manhattanDist(int tile, int[][] puzzle, int solutionTile) {
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
	public Point getPoint(int tile, int[][] puzzle) {
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
	 * Checks whether the puzzle is solved.
	 *
	 * @return returns true if all tiles are in the correct order
	 *         (1,2,3,8,0,4,7,6,5)
	 */
	public boolean isSolved() {
		return this.wrongTiles() == 0;
	}

}