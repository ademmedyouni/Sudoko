package server;

import shared.SudokuInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SudokuServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            SudokuInterface game = new SudokuServerImpl();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("SudokuService", game);
            System.out.println("🚀 Serveur Sudoku prêt !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
