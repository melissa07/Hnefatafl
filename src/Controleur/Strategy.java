package Controleur;

import Modele.Board;
import com.sun.xml.internal.bind.v2.TODO;

//Cette classe permet de calculer le score d'un board parmi tous les boards générés.
public class Strategy {

    public int attackerStrategy(Board boardGenere/*, int player*/) {
        return buildStrategy(boardGenere);
    }

    //cette méthode retourne un int correspondant au score du board passé en paramètre.
    //Pourquoi on aurait besoin du player ?
    public static int buildStrategy(Board boardGenere/*, int player*/) {
        int[][] board = boardGenere.getBoard();
        int positionRoiX = -1;
        int positionRoiY = -1;
        for (int i=0; i< board.length; i++) {
            for (int j=0; j< board.length; j++) {
                if(board[j][i] == 5) {
                    positionRoiX = i;
                    positionRoiY = j;
                }
            }
        }
        //Todo une fois les aglos fait, ces méthodes devraient nous permettre de calculer un score.
        boolean estEntoure = verifierSiRoiEntoure(board, positionRoiX, positionRoiY); // 1ere strategie de calcul de board
        boolean priorisees = verifierSiCasesPrioritairesOccupees(board); // 2nde strategie de calcul de board
        boolean estEnDanger = verifierSiPionEstEnDanger(boardGenere);

        return 0;
    }
    //Méthode qui permet de savoir si un pion serait en danger s'il bougeait à la position précisée dans le board
    private static boolean verifierSiPionEstEnDanger(Board board){
        //Todo vérifier si DANS LE BOARD un pion (n'importe lequel) a des chances d'être mangé
        return false;
    }

    private static boolean verifierSiCasesPrioritairesOccupees(int[][] board) {
        int counter = 0;
        while(counter < 4) {
            for (int i = 2 ; i < board.length; i--) {
                for (int j = 0; j < 3; j++) {
                    if(board[j][i] == 1)
                        return false; // todo And also check if king could reach position in 2 moves or less
                }
            }
        }
        // todo compter le nombre de cases importantes non protegees afin d'y attribuer un score
        return true;
    }

    private static boolean verifierSiRoiEntoure(int[][] board, int positionRoiX, int positionRoiY) {
        boolean estEntoure = true;
        if(positionRoiX+1 <= 12
                && (board[positionRoiY][positionRoiX+1] != 2
                || board[positionRoiY][positionRoiX+1] != 4)) { estEntoure = false;}

        if(positionRoiX-1 >= 0
                && (board[positionRoiY][positionRoiX-1] != 2
                || board[positionRoiY][positionRoiX-1] != 4)) { estEntoure = false; }

        if(positionRoiY+1 <= 12
                && (board[positionRoiY+1][positionRoiX] != 2
                || board[positionRoiY+1][positionRoiX] != 4)) { estEntoure = false; }

        if(positionRoiY-1 >= 0
                && (board[positionRoiY-1][positionRoiX] != 2
                || board[positionRoiY-1][positionRoiX] != 4)) { estEntoure = false; }
        return estEntoure;
    }

    public void defenderStrategy() {

    }
}
