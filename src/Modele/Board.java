package Modele;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Character> map = new HashMap<Integer, Character>();
    private int[][] board = null;

//    private final int CASE_VIDE = 0;
//    private final int PION_ROUGE = 1;
//    private final int PION_NOIR = 2;
//    private final int PION_ROI = 4;
//    private final int CASE_COIN = 5;
//    private final int CASE_THRONE = 6;
    private final int BOARD_SIZE = 13;


    public Board(int[] chaineBoard) {
        initBoard(chaineBoard);
        createAssociativeMap();
    }

    /**
     * Fonction qui recoit en parametre un tableau correspondant
     * au board et qui cree le board a l'aide d'un tableau a 2
     * dimensions
     * @param chaineBoard Board recu du serveur
     */
    private void initBoard(int[] chaineBoard) {
        int j = 0;
        int k = 0;
        for(int i = 0; i < BOARD_SIZE;i++){
            board[i][j] = chaineBoard[k];
            k++;
            if(i == 12 && j != 12){
                i = 0;
                j++;
            }
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
