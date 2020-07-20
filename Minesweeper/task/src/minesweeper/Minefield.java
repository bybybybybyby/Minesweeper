package minesweeper;

import java.awt.*;
import java.util.*;

public class Minefield {

    private int numOfMines;
    private String[][] minefield;
    private Set<Point> mineLocations;
    private Set<Point> mineGuesses;
    private static final int FIELD_SIZE = 9;
    private boolean hideMines;

    // Constructor
    public Minefield(int numOfMines) {
        this.numOfMines = numOfMines;
        this.mineGuesses = new HashSet<>();
        minefield = new String[FIELD_SIZE][FIELD_SIZE];
        for (String[] s : minefield) {
            Arrays.fill(s, "");
        }
        this.hideMines = true;
    }

    // Set random mine location points
    public void plantMines() {
        mineLocations = new HashSet<>();
        Random random = new Random();

        while (mineLocations.size() < numOfMines) {
            int x = random.nextInt(FIELD_SIZE);
            int y = random.nextInt(FIELD_SIZE);
            Point p = new Point(x, y);
            mineLocations.add(p);
            if (hideMines) {
                minefield[x][y] = ".";
            } else {
                minefield[x][y] = "X";
            }
        }
    }

    // Calculate how many mines are around an empty cell
    public void countNearbyMines() {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                // If on safe point, count surrounding mines and update with int count
                if (minefield[x][y].equals("")) {
                    // Help decide which points to check when on the edges
                    int left = Math.max(0, x - 1);
                    int right = Math.min(FIELD_SIZE - 1, x + 1);
                    int above = Math.max(0, y - 1);
                    int below = Math.min(FIELD_SIZE - 1, y + 1);

                    // Points needed to check
                    Set<Point> checkPoints = new HashSet<>();
                    checkPoints.add(new Point(left, above));
                    checkPoints.add(new Point(x, above));
                    checkPoints.add(new Point(right, above));
                    checkPoints.add(new Point(right, y));
                    checkPoints.add(new Point(right, below));
                    checkPoints.add(new Point(x, below));
                    checkPoints.add(new Point(left, below));
                    checkPoints.add(new Point(left, y));

                    // Check points and count mines
                    int surroundingMines = 0;
                    for (Point p : checkPoints) {
//                        if (minefield[p.x][p.y].contains("X")) {
                        if (mineLocations.contains(p)) {
                            surroundingMines++;
                        }
                    }

                    // Write String to minefield.  Either number or "." if 0.
                    String count = Integer.toString(surroundingMines);
                    minefield[x][y] = count.equals("0") ? "." : count;
                }
            }
        }
    }

    // Set/delete mine marks
    public void setMark(Scanner sc) {
        while (true) {
            System.out.println("Set/delete mines marks (x and y coordinates): ");
            int y = sc.nextInt() - 1;  //Coordinates show 1 higher than array index
            int x = sc.nextInt() - 1;
            if (minefield[x][y].matches("\\d")) {
                System.out.println("There is a number here!");
            } else {
//                System.out.println("x="+x+" y=" + y + " minefield=" + minefield[x][y]);
//                minefield[x][y] = (minefield[x][y].equals(".")) ? "*" : ".";
                Point p = new Point(x, y);
                switch(minefield[x][y]) {
                    case ".":
                        minefield[x][y] = "*";
                        mineGuesses.add(p);
                        break;
                    case "*":
                        minefield[x][y] = ".";
                        mineGuesses.remove(p);
                        break;
                }

                break;
            }
        }
    }

    // Check if all guesses match mine locations, if so game is won.
    public boolean checkForWinner() {
        if (mineGuesses.equals(mineLocations)) {
            System.out.println("Congratulations! You found all mines!");
            return true;
        }
        return false;
    }

    // Print out minefield
    public void print() {
        // Print header
        System.out.println(" │123456789│\n—│—————————│");
        // Print minefield with rows
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < FIELD_SIZE; j++) {
                System.out.print(minefield[i][j]);
            }
            System.out.println("|");
        }
        // Print footer
        System.out.println("—│—————————│");
    }


}
