package model;

public class SudokuGrid {
    private int[][] grid;

    public SudokuGrid(int[][] grid) {
        this.grid = grid;
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean isValidMove(int row, int col, int number) {
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == number || grid[i][col] == number) return false;
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grid[startRow + i][startCol + j] == number) return false;
        return true;
    }

    public boolean isCellEmpty(int row, int col) {
        return grid[row][col] == 0;
    }

    public void setNumber(int row, int col, int number) {
        grid[row][col] = number;
    }

    public boolean isComplete() {
        for (int[] row : grid)
            for (int num : row)
                if (num == 0) return false;
        return true;
    }

    public void displayGrid() {
        System.out.println("\n========= Grille Sudoku =========");
        for (int[] row : grid) {
            for (int num : row) {
                System.out.print((num == 0 ? ". " : num + " "));
            }
            System.out.println();
        }
        System.out.println("=================================\n");
    }
}

