package Modele;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board {
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
    private int[][] board = null;
    private final int BOARD_SIZE = 13;
    private Random rn = new Random();

    public Board(){}

    public Board(String[] chaineBoard) {
        initBoard(chaineBoard);
    }

    public Board(int[][] board) {
        this.board = board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public int[][] getBoard() { return board; }

    /**
     * Fonction qui recoit en parametre un tableau correspondant
     * au board et qui cree le board a l'aide d'un tableau a 2
     * dimensions
     * @param chaineBoard Board recu du serveur
     */
    private void initBoard(String[] chaineBoard) {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        int x=0,y=0;
        for(int i=0; i< chaineBoard.length;i++){
            board[x][y] = Integer.parseInt(chaineBoard[i]);
            x++;
            if(x == 13){
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
            System.out.print(String.format("%3d", BOARD_SIZE-i)+" |");
            for (int s = 0; s < BOARD_SIZE; s++) {
                System.out.print("  "+ board[s][i]);
            }
            System.out.println();
        }
        System.out.println("       A  B  C  D  E  F  G  H  I  J  K  L  M");
        System.out.println("_____________________________________________");

    }

    public void modifyBoard(String move ,int couleurJoueur) {
        initMap();
        //System.out.println("Nouveau move:" +move);
        int[][] moveDepart = null;
        int[][] moveFinal = null;
        int couleurAdverse = 0;

        if(couleurJoueur == 4){
            couleurAdverse = 2;
        }else{
            couleurAdverse = 4;
        }

        move = move.trim();
        int rangeeDepart = 0;
        int colonneDepart = map.get(move.substring(0,move.indexOf('-')).substring(0,1));
        String moveTmp = move.substring(0,move.indexOf('-'));
        if(moveTmp.length() == 3){
            rangeeDepart = map2.get(Integer.parseInt(moveTmp.substring(1,2)));
        }else if(moveTmp.length() == 4){
            rangeeDepart = map2.get(Integer.parseInt(moveTmp.substring(1,3)));
        }
        System.out.println("Rangee depart: " +rangeeDepart+ " et colonne depart: " +colonneDepart);


        int remainingLength = move.replaceAll("\\s+","").length() - (move.substring(0, move.indexOf('-')).replaceAll("\\s+","").length()+1);
        int colonneFin = map.get(move.substring(move.indexOf('-')+2, move.length()).substring(0,1));
        int rangeeFin = map2.get(Integer.parseInt( move.substring(move.indexOf('-')+2, move.length()).substring(1,remainingLength)));
        System.out.println("Rangee d'arrivee: " +rangeeFin+ " et colonne d'arrivee: " +colonneFin);

        int valeurCaseDepart = board[colonneDepart][rangeeDepart];
        board[colonneDepart][rangeeDepart] = 0;
        board[colonneFin][rangeeFin] = valeurCaseDepart;
        if(colonneFin < 11) {
            if (board[colonneFin + 1][rangeeFin] == couleurJoueur) {
                if (board[colonneFin + 2][rangeeFin] == couleurAdverse || board[colonneFin + 2][rangeeFin] == 1 || board[colonneFin + 2][rangeeFin] == 5) {
                    board[colonneFin + 1][rangeeFin] = 0;
                }
            }
        }
        if(rangeeFin < 11) {
            if (board[colonneFin][rangeeFin + 1] == couleurJoueur) {
                if (board[colonneFin][rangeeFin + 2] == couleurAdverse || board[colonneFin][rangeeFin + 2] == 1 || board[colonneFin][rangeeFin + 2] == 5) {
                    board[colonneFin][rangeeFin + 1] = 0;
                }
            }
        }
        if(colonneFin > 1) {
            if (board[colonneFin - 1][rangeeFin] == couleurJoueur) {
                if (board[colonneFin - 2][rangeeFin] == couleurAdverse || board[colonneFin - 2][rangeeFin] == 1 || board[colonneFin - 2][rangeeFin] == 5) {
                    board[colonneFin - 1][rangeeFin] = 0;
                }
            }
        }
        if(rangeeFin > 1) {
            if (board[colonneFin][rangeeFin - 1] == couleurJoueur) {
                if (board[colonneFin][rangeeFin - 2] == couleurAdverse || board[colonneFin][rangeeFin - 2] == 1 || board[colonneFin][rangeeFin - 2] == 5) {
                    board[colonneFin][rangeeFin - 1] = 0;
                }
            }
        }
        //printBoard();

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
