package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Minefield minefield;
        int numOfMines = -1;
        Scanner sc = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        // TODO: check if mines are less than field size
        boolean valid = false;
        boolean winner = false;

        while(!valid) {
            try {
                numOfMines = Integer.parseInt(sc.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            }
        }

        minefield = new Minefield(numOfMines);
        minefield.plantMines();
        minefield.countNearbyMines();

        while(!winner) {
            minefield.print();
            minefield.setMark(sc);
            if (minefield.checkForWinner()) {
                winner = true;
            }
        }
    }
}
