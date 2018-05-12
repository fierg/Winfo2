package puzzleSolver;

import java.util.ArrayList;
import java.util.Random;

public class PuzzleGenerator {

    public ArrayList<Puzzle> generatePuzzleList (long seed, int size) {
        ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
        Random random = new Random(seed);
        int randomPosition;
        for (int i = 0; i < size; i++) {
            int [][] puzzleArray = new int[3][3];
            for (int number = 1; number < 9; number++) {
                randomPosition = (int) (random.nextFloat() * 9);
                while (puzzleArray[randomPosition/3][randomPosition%3] != 0) {
                    randomPosition = (int) (random.nextFloat() * 9);
                }
                puzzleArray[randomPosition/3][randomPosition%3] = number;
            }
            puzzles.add( new Puzzle(puzzleArray) );
        }
        return puzzles;
    }

}
