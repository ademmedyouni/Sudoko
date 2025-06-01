package server;

import model.SudokuGrid;
import shared.CallbackInterface;
import shared.SudokuInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;

public class SudokuServerImpl extends UnicastRemoteObject implements SudokuInterface {

    private SudokuGrid grid;
    private final ExecutorService pool;
    private final Map<CallbackInterface, Instant> clientActivityMap;
    private final ScheduledExecutorService timeoutChecker;

    protected SudokuServerImpl() throws RemoteException {
        super();
        int[][] generatedGrid = SudokuGridFactory.generateGrid();
        this.grid = new SudokuGrid(generatedGrid);
        this.pool = Executors.newFixedThreadPool(10); 
        this.clientActivityMap = new ConcurrentHashMap<>();

        // Vérifie toutes les 30s si un client est inactif depuis plus de 2 minutes
        this.timeoutChecker = Executors.newSingleThreadScheduledExecutor();
        this.timeoutChecker.scheduleAtFixedRate(this::checkTimeouts, 30, 30, TimeUnit.SECONDS);

        System.out.println("✅ Grille initiale choisie par le serveur :");
        grid.displayGrid();
    }

    @Override
    public synchronized boolean submitNumber(int row, int col, int number, CallbackInterface client) throws RemoteException {
        pool.submit(() -> {
            try {
                clientActivityMap.put(client, Instant.now()); // Mise à jour de l’activité du client
                int[][] gridBefore = cloneGrid(grid.getGrid()); // Sauvegarde de la grille avant modification

                if (number < 1 || number > 9) {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("❌ Erreur : la valeur ajoutée viole les conditions du jeu !\n");
                    errorMessage.append(printGrid(gridBefore));
                    client.notifyError(errorMessage.toString());
                    return;
                }

                if (!grid.isCellEmpty(row, col)) {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("❌ Erreur : cette case est déjà remplie !\n");
                    errorMessage.append(printGrid(gridBefore));
                    client.notifyError(errorMessage.toString());
                    return;
                }

                if (!grid.isValidMove(row, col, number)) {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("❌ Erreur : la valeur ajoutée viole les conditions du jeu !\n");
                    errorMessage.append(printGrid(gridBefore));
                    client.notifyError(errorMessage.toString());
                    return;
                }

                // Placement correct
                grid.setNumber(row, col, number);
                System.out.println("✔️ Le joueur a inséré " + number + " à [" + (row + 1) + "][" + (col + 1) + "]");
                grid.displayGrid();

                if (grid.isComplete()) {
                    client.notifyWin();
                    System.out.println("🎉 Grille complétée !");
                    this.grid = new SudokuGrid(SudokuGridFactory.generateGrid());
                    System.out.println("🆕 Nouvelle grille générée :");
                    grid.displayGrid();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        return true;
    }


    public int[][] getGrid() {
        return grid.getGrid();
    }

    public boolean isComplete() {
        return grid.isComplete();
    }

    private int[][] cloneGrid(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        return copy;
    }

    private String printGrid(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========= Grille Sudoku =========\n");
        for (int[] row : grid) {
            for (int num : row) {
                sb.append(num == 0 ? ". " : num + " ");
            }
            sb.append("\n");
        }
        sb.append("=================================\n");
        return sb.toString();
    }

    private void checkTimeouts() {
        Instant now = Instant.now();
        for (Map.Entry<CallbackInterface, Instant> entry : clientActivityMap.entrySet()) {
            if (now.minusSeconds(120).isAfter(entry.getValue())) { // Inactivité > 2 min
                try {
                    entry.getKey().notifyError("⏱️ Déconnecté pour inactivité prolongée.");
                    clientActivityMap.remove(entry.getKey());
                } catch (RemoteException e) {
                    System.out.println("Client déjà déconnecté.");
                }
            }
        }
    }
} 