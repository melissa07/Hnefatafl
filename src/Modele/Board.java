package Modele;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board {
    private Map<Integer, Character> map = new HashMap<Integer, Character>();
    private int[][] board = null;
    private final int BOARD_SIZE = 13;
    private Random rn = new Random();

    public Board(){}

    public Board(String[] chaineBoard) {
        initBoard(chaineBoard);
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
        printBoard();

    }

    /**
     * Dessine le board en console
     */
    private void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int s = 0; s < BOARD_SIZE; s++) {
                System.out.print(board[i][s]);
            }
            System.out.println();
        }
    }

    public void modifyBoard(String move) {
        System.out.println("Nouveau move:" +move);
        int[][] moveDepart = null;
        int[][] moveFinal = null;

        move = move.trim();
        String strMoveDepart = "";
        String strMoveArivee = "";
        // todo fix this
        int rangeeDepart = Integer.parseInt(move.substring(0,move.indexOf('-')).substring(0,1));
        int colonneDepart = Integer.parseInt(move.substring(0,move.indexOf('-')).substring(1,2));

        int rangeeArrive = Integer.parseInt( move.substring(move.indexOf('-')+1, move.length()).substring(0,1));
        int colonneFin = Integer.parseInt( move.substring(move.indexOf('-')+1, move.length()).substring(1,2));

        int[][] depart = new int[rangeeDepart][colonneDepart];
        int[][] arrivee = new int[rangeeArrive][colonneFin];


        strMoveDepart = move.substring(0, move.indexOf('-'));

        strMoveArivee = move.substring(move.indexOf('-')+1, move.length());


    }

    /*
    calcule et retourne le score du board.
     */
    public int getScore(){

        int score = rn.nextInt(10) + 1;

        return  score;
    }

}
