package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackInterface extends Remote {
    void notifyWin() throws RemoteException;
    void notifyError(String message) throws RemoteException;
}