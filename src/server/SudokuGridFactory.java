package server ;

import java.util.Random;

public class SudokuGridFactory {
    public static int[][] generateGrid() {
        int[][] grid = new int[9][9];
        Random rand = new Random();
        int count = 0;
        while (count < 20) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            int num = rand.nextInt(9) + 1;
            if (grid[row][col] == 0 && isValid(grid, row, col, num)) {
                grid[row][col] = num;
                count++;
            }
        }
        return grid;
    }

    private static boolean isValid(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num) return false;
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grid[startRow + i][startCol + j] == num) return false;
        return true;
    }
}

