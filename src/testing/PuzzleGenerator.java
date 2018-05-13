package testing;

import puzzleSolver.Puzzle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates random puzzles for testing
 */
public class PuzzleGenerator {

    /**
     * generates a list of random 8Puzzles with a given seed.
     * @param seed the seed which should be used to create the puzzles
     * @param size the amount of puzzles to be created
     * @return a list of the given size with random puzzles
     */
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
