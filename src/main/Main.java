package main;

import solving.AStarStrategy;
import structures.Puzzle;

public class Main {
    public static void main(String[] args) {
        Puzzle pAlmostFinished = new Puzzle(300, 300, 8255);
        Puzzle pWithSeed = new Puzzle(400, 400, 123);
        Puzzle pRandom = new Puzzle(500, 500);
        pRandom.solve(50, new AStarStrategy());
    }
}
