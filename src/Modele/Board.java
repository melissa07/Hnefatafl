package Modele;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Character> map = new HashMap<Integer, Character>();
    private int[][] board = null;
    private final int BOARD_SIZE = 13;


    public Board(String[] chaineBoard) {
        initBoard(chaineBoard);
        createAssociativeMap();
    }

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

    /**
     * Cree un hashmap de correspondance entre les indices
     * du tableau et les lettres du board
     */
    private void createAssociativeMap() {
        int valueA = Character.valueOf('A');
        for (int i = 0; i <= 12; i++) {
            map.put(i, (char)(i+'A'));
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }
}
