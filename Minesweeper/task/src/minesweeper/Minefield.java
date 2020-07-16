package minesweeper;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Minefield {

    private int numOfMines;
    private boolean[][] minefield;
    private Set<Point> mineLocations;
    private static final int SIZE = 9;

    // Constructor
    public Minefield(int numOfMines) {
        this.numOfMines = numOfMines;
        minefield = new boolean[SIZE][SIZE];
    }

    // Set random mine location points
    public void plantMines() {
        mineLocations = new HashSet<>();
        Random random = new Random();

        while (mineLocations.size() < numOfMines) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            Point p = new Point(x, y);
            mineLocations.add(p);
        }
    }

    // Print out minefield
    public void print() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Point curr = new Point(i, j);
                if (mineLocations.contains(curr)) {
                    System.out.print("X");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

}
