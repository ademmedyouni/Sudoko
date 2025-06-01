package client;

import shared.CallbackInterface;
import shared.SudokuInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SudokuClientGUI extends UnicastRemoteObject implements CallbackInterface {
    private SudokuInterface game;
    private JFrame frame;
    private JTextField[][] cells;

    public SudokuClientGUI() throws RemoteException {
        try {
            game = (SudokuInterface) Naming.lookup("rmi://localhost/SudokuService");
            initGUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur de connexion au serveur RMI");
            System.exit(1);
        }
    }

    private void initGUI() throws RemoteException {
        frame = new JFrame("Sudoku RMI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        cells = new JTextField[9][9];

        int[][] grid = game.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                if (grid[i][j] != 0) {
                    cell.setText(String.valueOf(grid[i][j]));
                    cell.setEditable(false);
                    cell.setBackground(Color.LIGHT_GRAY);
                }
                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        JButton submitButton = new JButton("Envoyer");
        submitButton.addActionListener(e -> sendMove());

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(submitButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void sendMove() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText().trim();
                if (!text.isEmpty() && cells[i][j].isEditable()) {
                    try {
                        int number = Integer.parseInt(text);
                        game.submitNumber(i, j, number, this);
                        return; // Un seul mouvement à la fois
                    } catch (NumberFormatException | RemoteException ex) {
                        JOptionPane.showMessageDialog(frame, "⛔ Veuillez entrer un chiffre entre 1 et 9.");
                    }
                }
            }
        }
    }

    @Override
    public void notifyWin() throws RemoteException {
        JOptionPane.showMessageDialog(frame, "🎉 Félicitations ! Vous avez complété la grille !");
        refreshGrid();
    }

    @Override
    public void notifyError(String message) throws RemoteException {
        JOptionPane.showMessageDialog(frame, "⚠️ " + message);
        refreshGrid();
    }

    private void refreshGrid() throws RemoteException {
        int[][] newGrid = game.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = cells[i][j];
                if (!cell.isEditable()) continue;
                if (newGrid[i][j] != 0) {
                    cell.setText(String.valueOf(newGrid[i][j]));
                    cell.setEditable(false);
                    cell.setBackground(Color.LIGHT_GRAY);
                } else {
                    cell.setText("");
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new SudokuClientGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
