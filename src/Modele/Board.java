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
        initMap();
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
        /*
        int rangeeDepart = Integer.parseInt(move.substring(0,move.indexOf('-')).substring(0,1));
        int colonneDepart = Integer.parseInt(move.substring(0,move.indexOf('-')).substring(1,2));

        int rangeeArrive = Integer.parseInt( move.substring(move.indexOf('-')+1, move.length()).substring(0,1));
        int colonneFin = Integer.parseInt( move.substring(move.indexOf('-')+1, move.length()).substring(1,2));
*/



        int rangeeDepart = map.get(move.substring(0,move.indexOf('-')).substring(0,1));
        int colonneDepart = map2.get(Integer.parseInt(move.substring(0,move.indexOf('-')).substring(1,2)));

        int rangeeArrive = map.get(move.substring(move.indexOf('-')+2, move.length()).substring(0,1));
        int colonneFin = map2.get(Integer.parseInt( move.substring(move.indexOf('-')+2, move.length()).substring(1,2)));

        int[][] depart = new int[rangeeDepart][colonneDepart];
        int[][] arrivee = new int[rangeeArrive][colonneFin];

        strMoveDepart = move.substring(0, move.indexOf('-'));

        strMoveArivee = move.substring(move.indexOf('-')+1, move.length());
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

}
