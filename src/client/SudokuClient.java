package client;

import shared.CallbackInterface;
import shared.SudokuInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class SudokuClient extends UnicastRemoteObject implements CallbackInterface {
    private SudokuInterface game;

    protected SudokuClient() throws RemoteException {
        super();
    }

    public void startClient() {
        try {
            game = (SudokuInterface) Naming.lookup("rmi://localhost/Sudoku");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                printGrid(game.getGrid());
                System.out.print("Entrez ligne colonne valeur (ex: 1 2 3): ");
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) continue;
                String[] parts = input.split(" ");
                if (parts.length != 3) {
                    System.out.println("⛔ Format invalide. Utilisez: ligne colonne valeur");
                    continue;
                }
                try {
                    int row = Integer.parseInt(parts[0]) - 1;
                    int col = Integer.parseInt(parts[1]) - 1;
                    int num = Integer.parseInt(parts[2]);
                    game.submitNumber(row, col, num, this);
                } catch (NumberFormatException e) {
                    System.out.println("⛔ Veuillez entrer des nombres valides.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printGrid(int[][] grid) {
        System.out.println("\n========= Grille Sudoku =========");
        for (int[] row : grid) {
            for (int num : row) {
                System.out.print((num == 0 ? ". " : num + " "));
            }
            System.out.println();
        }
        System.out.println("=================================\n");
    }

    @Override
    public void notifyWin() throws RemoteException {
        System.out.println("🎉 Félicitations ! Vous avez complété la grille ! Une nouvelle grille vous sera donnée.");
    }

    @Override
    public void notifyError(String message) throws RemoteException {
        System.out.println("\n===================================");
        System.out.println("⚠️ ERREUR: " + message);
        try {
            System.out.println("\n💡 Grille actuelle après refus :");
            printGrid(game.getGrid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("===================================\n");
    }

    public static void main(String[] args) {
        try {
            new SudokuClient().startClient();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}