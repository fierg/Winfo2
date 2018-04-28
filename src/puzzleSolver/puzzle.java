package puzzleSolver;

public class puzzle {
	
	int [][] puzzle;
	
	public puzzle(int[][] puzzle){
		this.puzzle = puzzle;		
	}
	
	public void printPuzzle(){
		for(int i = 0; i < this.puzzle.length; i++){
			for(int j = 0; j < this.puzzle[i].length; j++){
				System.out.print(this.puzzle[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-----");
	}
}