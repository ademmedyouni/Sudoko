#  Projet Java RMI - Jeu Sudoku en Réseau

Ce projet est une application Java distribuée basée sur **Java RMI (Remote Method Invocation)** qui permet à plusieurs clients de jouer au **jeu de Sudoku** via un serveur central.  
Chaque tentative d'ajout est validée côté serveur, avec des retours en temps réel via un système de **callback** RMI.

---

##  Structure du projet

```
sudoku-rmi/
├── client/
│   └── SudokuClient.java
├── model/
│   └── SudokuGrid.java
├── server/
│   └── SudokuServerImpl.java
├── shared/
│   ├── CallbackInterface.java
│   ├── GameInterface.java
│   └── SudokuGridFactory.java
|__ policy.policy
└── README.md
```

---

##  Instructions de Compilation et d’Exécution

### 1. Compiler tout le projet

Dans le dossier racine du projet (`sudoku-rmi/`), ouvrez un terminal :

```bash

```
javac client/*.java model/*.java server/*.java shared/*.java

---

### 2. Lancer le registre RMI
verifier si le port 1099 s'il est en cours d'execution ou non : 
```bash
netstat -ano | findstr :1099

```
si oui 
```bash
taskkill /PID <PID> /F

```

Dans le dossier `bin/` :

```bash
cd bin
start rmiregistry 
```
(dans notre projet on a deja inserer l'ouverture de l'annuaire rmiregistry à travers la ligne "LocateRegistry.createRegistry(1099);" donc pas besoin de taper cette commande .)

Laissez cette fenêtre ouverte.

---

### 3. Lancer le serveur

Dans un nouveau terminal :

```bash
cd bin
java -Djava.security.policy=policy.policy server.SudokuServerImpl
```

Cela va générer une grille et démarrer le service RMI.

---

### 4. Lancer un client

Dans un autre terminal :

```bash
cd bin
java -Djava.security.policy=policy.policy client.SudokuClient
```

 lancer plusieurs clients dans plusieurs terminaux pour tester en parallèle.

---

##  Instructions d’utilisation

- Saisir les mouvements au format : `ligne colonne valeur`  
  (ex : `1 2 5` pour insérer le chiffre `5` dans la ligne 1, colonne 2)
- Le serveur validera la saisie et notifiera :
  -  Si la saisie est correcte.
  -  Si la case est déjà remplie ou si le chiffre enfreint les règles du Sudoku.
- Une fois la grille complétée correctement, une nouvelle grille est générée automatiquement.

---

##  Technologies utilisées

- Java 8+ (dans notre projet on a utilisé "C:\Program Files\Java\jdk1.8.0_202" )
- Java RMI (Remote Method Invocation)
- Architecture client/serveur
- Design Pattern : Observer (via Callback RMI)

---

##  Réalisé par

-  **Nourhene Ayari** – Étudiante en 3ᵉ année cycle ingénieur, Data Science – FST Tunis
- **Adem Medyouni** – Étudiant en 3ᵉ année cycle ingénieur, Data Science – FST Tunis
-  Projet SAR (Systèmes et Applications Répartis)
-  Encadrants :
  - Heithem Abbes (Cours)
  - Thouraya Louati (TP/TD)


##  Remarques

- Le projet gère les erreurs, les tentatives invalides et les retours utilisateur.
- Les appels sont asynchrones côté serveur avec `ExecutorService`.
- Les callbacks permettent des notifications dynamiques des clients.

---
