package minesweeper;

import java.awt.*;
import java.util.*;

public class Minefield {

    private int numOfMines;
    private String[][] minefield;
    private Set<Point> mineLocations;
    private Set<Point> mineGuesses;
    private Set<Point> uncovered;
    private static final int FIELD_SIZE = 9;

    // Constructor
    public Minefield(int numOfMines) {
        this.numOfMines = numOfMines;
        this.mineGuesses = new HashSet<>();
        this.uncovered = new HashSet<>();
        minefield = new String[FIELD_SIZE][FIELD_SIZE];
        for (String[] s : minefield) {
            Arrays.fill(s, "");
        }
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
                        if (mineLocations.contains(p)) {
                            surroundingMines++;
                        }
                    }

                    // Write String to minefield.  Either number or "." if 0.
                    String count = Integer.toString(surroundingMines);
                    minefield[x][y] = count.equals("0") ? "" : count;
                }
            }
        }
    }

    // Ask for user input
    public boolean userInput(Scanner sc) {

        while (true) {
            System.out.println("Set/unset mines marks or claim a cell as free: ");
            String[] input = sc.nextLine().split("\\s+");

            if (input.length != 3) {
                System.out.println("Incorrect input, please try again! (ex: 2 4 mine, or 1 5 free)");
                continue;
            }

            switch (input[2]) {
                case "mine":
                    setMark(Integer.parseInt(input[0]) - 1, Integer.parseInt(input[1]) - 1);
                    return true;
                case "free":
                    boolean safe = setFree(Integer.parseInt(input[0]) - 1, Integer.parseInt(input[1]) - 1);
                    return safe ? true : false;
                default:
                    System.out.println("Not a valid input!");
                    return true;
            }
        }
    }

    // Set/delete mine marks
    public void setMark(int y, int x) {

        Point p = new Point(x, y);

        // First check for coordinate with no info yet (covered)
        if (!uncovered.contains(p)) {
            if (!mineGuesses.contains(p)) {
                mineGuesses.add(p);
            } else if (mineGuesses.contains(p)) {
                mineGuesses.remove(p);
            }
        } else {
            if (minefield[x][y].matches("\\d")) {
                System.out.println("There is a number here!");
            }
        }

    }


    /**
     * User guessing as a free spot (no mine)
     *
     * @param x y-coordinate to check
     * @param y x-coordinate to check
     * @return return true if coordinate was safe, return false if is mine
     */
    public boolean setFree(int y, int x) {

        boolean isSafe = true;

        Point p = new Point(x, y);
        uncovered.add(p);

        if (mineLocations.contains(p)) {
            isSafe = false;
        } else if (minefield[x][y].equals("")) {

            minefield[x][y] = "/";

            // Check to the right
            setFree(y, Math.min(x + 1, FIELD_SIZE - 1));

            // Check to the left
            setFree(y, Math.max(x - 1, 0));

            // Check above
            setFree(Math.min(y + 1, FIELD_SIZE - 1), x);

            // Check below
            setFree(Math.max(y - 1, 0), x);

            // Check above-right
            setFree(Math.max(y - 1, 0), Math.min(x + 1, FIELD_SIZE - 1));

            // Check above-left
            setFree(Math.max(y - 1, 0), Math.max(x - 1, 0));

            // Check below-right
            setFree(Math.min(y + 1, FIELD_SIZE - 1), Math.min(x + 1, FIELD_SIZE - 1));

            // Check below-left
            setFree(Math.min(y + 1, FIELD_SIZE - 1), Math.max(x - 1, 0));

            isSafe = true;
        }
        return isSafe;
    }


    // Check if all guesses match mine locations, if so game is won.
    public boolean checkForWinner() {
        System.out.println(mineGuesses);
        System.out.println(mineLocations);
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
                Point p = new Point(i, j);
                // First check if it is uncovered, if so print what is known
                if (uncovered.contains(p)) {
                    System.out.print(minefield[i][j]);
                } else if (mineGuesses.contains(p)) {    // next check if mine marked
                    System.out.print("*");
                } else {    // otherwise print "."
                    System.out.print(".");
                }
            }
            System.out.println("|");
        }
        // Print footer
        System.out.println("—│—————————│");
    }


}
