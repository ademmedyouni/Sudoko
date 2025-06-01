package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegistryViewer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            String[] names = registry.list();
            System.out.println("Objets enregistrés dans le registre :");
            for (String name : names) {
                System.out.println(" - " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

