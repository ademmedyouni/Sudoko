package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuInterface extends Remote {
	int[][] getGrid() throws RemoteException;
    boolean submitNumber(int row, int col, int number, CallbackInterface client) throws RemoteException;
}