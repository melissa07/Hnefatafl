package Modele;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import Controleur.Strategy;

public class Board {
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
    private int[][] board = null;
    private final int BOARD_SIZE = 13;
    private Random rn = new Random();
    private int kingPositionX = -1;
    private int kingPositionY = -1;


    public Board() {
    }

    public Board(String[] chaineBoard) {
        initBoard(chaineBoard);
    }

    public Board(int[][] board) {
        this.board = board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getKingPositionX() {
        return kingPositionX;
    }

    public void setKingPositionX(int kingPositionX) {
        this.kingPositionX = kingPositionX;
    }

    public int getKingPositionY() {
        return kingPositionY;
    }

    public void setKingPositionY(int kingPositionY) {
        this.kingPositionY = kingPositionY;
    }

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public int[][] getBoard() {
        return board;
    }

    /**
     * Fonction qui recoit en parametre un tableau correspondant
     * au board et qui cree le board a l'aide d'un tableau a 2
     * dimensions
     *
     * @param chaineBoard Board recu du serveur
     */
    private void initBoard(String[] chaineBoard) {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        int x = 0, y = 0;
        for (int i = 0; i < chaineBoard.length; i++) {
            board[x][y] = Integer.parseInt(chaineBoard[i]);
            x++;
            if (x == 13) {
                x = 0;
                y++;
            }
        }
        //les coin
        board[0][0] = 1;
        board[0][12] = 1;
        board[12][0] = 1;
        board[12][12] = 1;

        printBoard();

    }

    /**
     * Dessine le board en console
     */
    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(String.format("%3d", BOARD_SIZE - i) + " |");
            for (int s = 0; s < BOARD_SIZE; s++) {
                System.out.print("  " + board[s][i]);
            }
            System.out.println();
        }
        System.out.println("       A  B  C  D  E  F  G  H  I  J  K  L  M");
        System.out.println("_____________________________________________");

    }

    public void modifyBoard(String move, int couleurJoueur) {
        initMap();
        //System.out.println("Nouveau move:" +move);
        int[][] moveDepart = null;
        int[][] moveFinal = null;
        int couleurAdverse = 0;

        if (couleurJoueur == 4) {
            couleurAdverse = 2;
        } else {
            couleurAdverse = 4;
        }

        move = move.trim();
        int rangeeDepart = 0;
        int colonneDepart = map.get(move.substring(0, move.indexOf('-')).substring(0, 1));
        String moveTmp = move.substring(0, move.indexOf('-'));
        if (moveTmp.length() == 3) {
            rangeeDepart = map2.get(Integer.parseInt(moveTmp.substring(1, 2)));
        } else if (moveTmp.length() == 4) {
            rangeeDepart = map2.get(Integer.parseInt(moveTmp.substring(1, 3)));
        }
        System.out.println("Rangee depart: " + rangeeDepart + " et colonne depart: " + colonneDepart);


        int remainingLength = move.replaceAll("\\s+", "").length() - (move.substring(0, move.indexOf('-')).replaceAll("\\s+", "").length() + 1);
        int colonneFin = map.get(move.substring(move.indexOf('-') + 2, move.length()).substring(0, 1));
        int rangeeFin = map2.get(Integer.parseInt(move.substring(move.indexOf('-') + 2, move.length()).substring(1, remainingLength)));
        System.out.println("Rangee d'arrivee: " + rangeeFin + " et colonne d'arrivee: " + colonneFin);

        int valeurCaseDepart = board[colonneDepart][rangeeDepart];
        this.board[colonneDepart][rangeeDepart] = 0;
        if(colonneDepart == 6 && rangeeDepart == 6){
            this.board[colonneDepart][rangeeDepart] = 1;
        }
        this.board[colonneFin][rangeeFin] = valeurCaseDepart;
        board = mangerJeton(couleurJoueur, couleurAdverse, colonneFin, rangeeFin);
        //printBoard();

    }

    public int[][] mangerJeton(int couleurJoueur, int couleurAdverse, int colonneFin, int rangeeFin) {
        int king = 5;
        //change la couleur du king pour rouge si c est le joueur rouge qui a fait un movement pour manger
        // afin qu un joueur rouge ne puisse pas manger un noir a laide du king.
        if(couleurJoueur == 4){
            king = 4;
        }
        if (colonneFin < 11) {
            if (this.board[colonneFin + 1][rangeeFin] == couleurAdverse) {
                if (this.board[colonneFin + 2][rangeeFin] == couleurJoueur || this.board[colonneFin + 2][rangeeFin] == 1 || this.board[colonneFin + 2][rangeeFin] == king || (colonneFin + 2 == 6 && rangeeFin == 6)) {
                    this.board[colonneFin + 1][rangeeFin] = 0;
                }
            }
        }
        if (rangeeFin < 11) {
            if (this.board[colonneFin][rangeeFin + 1] == couleurAdverse) {
                if (this.board[colonneFin][rangeeFin + 2] == couleurJoueur || this.board[colonneFin][rangeeFin + 2] == 1 || this.board[colonneFin][rangeeFin + 2] == king || (colonneFin == 6 && rangeeFin + 2 == 6)) {
                    this.board[colonneFin][rangeeFin + 1] = 0;
                }
            }
        }
        if (colonneFin > 1) {
            if (this.board[colonneFin - 1][rangeeFin] == couleurAdverse) {
                if (this.board[colonneFin - 2][rangeeFin] == couleurJoueur || this.board[colonneFin - 2][rangeeFin] == 1 || this.board[colonneFin - 2][rangeeFin] == king || (colonneFin - 2 == 6 && rangeeFin == 6)) {
                    this.board[colonneFin - 1][rangeeFin] = 0;
                }
            }
        }
        if (rangeeFin > 1) {
            if (this.board[colonneFin][rangeeFin - 1] == couleurAdverse) {
                if (this.board[colonneFin][rangeeFin - 2] == couleurJoueur || this.board[colonneFin][rangeeFin - 2] == 1 || this.board[colonneFin][rangeeFin - 2] == king || (colonneFin == 6 && rangeeFin - 2 == 6)) {
                    this.board[colonneFin][rangeeFin - 1] = 0;
                }
            }
        }
        return this.board;
    }


    public void initMap(){
        this.map.put("A",0);
        this.map.put("B",1);
        this.map.put("C",2);
        this.map.put("D",3);
        this.map.put("E",4);
        this.map.put("F",5);
        this.map.put("G",6);
        this.map.put("H",7);
        this.map.put("I",8);
        this.map.put("J",9);
        this.map.put("K",10);
        this.map.put("L",11);
        this.map.put("M",12);


        this.map2.put(13,0);
        this.map2.put(12,1);
        this.map2.put(11,2);
        this.map2.put(10,3);
        this.map2.put(9,4);
        this.map2.put(8,5);
        this.map2.put(7,6);
        this.map2.put(6,7);
        this.map2.put(5,8);
        this.map2.put(4,9);
        this.map2.put(3,10);
        this.map2.put(2,11);
        this.map2.put(1,12);
    }

    /*
    calcule et retourne le score du board.
     */
    public int getScore(){
        // todo change this for appropriate strategy
        // todo ex: Strategy attack = new AttackerStrategy();
        Strategy strategieCalculScore = new Strategy();

        //Méthode qui va permettre de calculer le score du board avec les stratégies.
        //int score = strategieCalculScore.attackerStrategy(this);

        int score = rn.nextInt(10) + 1;
        return  score;
    }

    public int[][] copyBoard(Board board) {
        int[][] tmpBoard = board.getBoard();
        int[][] myInt = new int[tmpBoard.length][]; // todo myInt ? - Christelle
        for (int i = 0; i < tmpBoard.length; i++){
            myInt[i] = tmpBoard[i].clone();
        }
        return  myInt;
    }
}
