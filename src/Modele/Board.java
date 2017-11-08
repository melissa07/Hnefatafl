package Modele;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Character> map = new HashMap<Integer, Character>();
    private int[][] plateauJeu = null;

//    private final int CASE_VIDE = 0;
//    private final int PION_ROUGE = 1;
//    private final int PION_NOIR = 2;
//    private final int PION_ROI = 4;
//    private final int CASE_COIN = 5;
//    private final int CASE_THRONE = 6;
    private final int BOARD_SIZE = 13;


    public Board(int[] chaineBoeard) {

        initBoard(chaineBoeard);
        createAssociativeMap();
    }

    private void initBoard(int[] chaineBoard) {
        // board recu du serveur
        int j = 0;
        int k = 0;
        for(int i = 0; i < BOARD_SIZE;i++){
            plateauJeu[i][j] = chaineBoard[k];
            k++;
            if(i == 12 && j != 12){
                i = 0;
                j++;
            }
        }
    }

    private void createAssociativeMap() {
        int valueA = Character.valueOf('A');
        for (int i = 0; i <= 12; i++) {
            map.put(i, (char)(i+'A'));
        }
    }


}
