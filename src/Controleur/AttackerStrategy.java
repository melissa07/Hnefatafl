package Controleur;

import Modele.Board;

public class AttackerStrategy implements IStrategy {
    private static AttackerStrategy attackerSingleton = null;
    private int score;

    public static AttackerStrategy getInstance() {
        if(attackerSingleton == null) {
            attackerSingleton = new AttackerStrategy();
        }
        return attackerSingleton;
    }

    @Override
    public int execute(Board board) {
        int attackerScore = 0;
        attackerScore += 2 * countNbPawnsLeft(board);
        attackerScore += findNearestKingExist(board);
        attackerScore += verifierSiCasesPrioritairesOccupees(board.getBoard());

        return attackerScore;
    }

    @Override
    public int countNbPawnsLeft(Board board) {
        int[][] tabBoard = board.getBoard();
        int nbPawn = 0;

        for(int i=0;i < tabBoard.length; i++){
            for(int j=0;j < tabBoard[i].length; j++){
                if(tabBoard[j][i] == 2 || tabBoard[j][i] == 5){
                    nbPawn++;
                }
            }
        }

        return nbPawn;
    }

    public int buildStrategy(Board boardGenere) {

        int[][] board = boardGenere.getBoard();
        // this get the king position on the board
        for (int i=0; i< board.length; i++) {
            for (int j=0; j< board.length; j++) {
                if(board[j][i] == 5) {
                    boardGenere.setKingPositionX(i);
                    boardGenere.setKingPositionY(j);
                }
            }
        }
        //Todo une fois les algos fait, ces méthodes devraient nous permettre de calculer un score.
        boolean estEntoure = verifierSiRoiEntoure(boardGenere); // 1ere strategie de calcul de board
//        int priorisees = verifierSiCasesPrioritairesOccupees(board); // 2nde strategie de calcul de board
        boolean estEnDanger = verifierSiPionEstEnDanger(boardGenere);

        return 0;
    }

    //Méthode qui permet de savoir si un pion serait en danger s'il bougeait à la position précisée dans le board
    @Override
    public boolean verifierSiPionEstEnDanger(Board board) {
        //Todo vérifier si DANS LE BOARD un pion (n'importe lequel) a des chances d'être mangé
        return false;
    }

    @Override
    public int verifierSiCasesPrioritairesOccupees(int[][] board) {
        int counter = 0;
        int prioritairesScore = 0;
        while(counter < 4) {
            for (int i = 2 ; i < board.length; i--) {
                for (int j = 0; j < 2; j++) {
                    if(board[j][i] == 1) // 1 representent les bordures X
                         continue; // todo And also check if king could reach position in 2 moves or less
                    else {
                        if(board[j][i] == 4)
                            prioritairesScore += 50;
                        else
                            prioritairesScore -= 50;
                    }
                }
            }
        }
        // todo compter le nombre de cases importantes non protegees afin d'y attribuer un score
        return prioritairesScore;
    }

    public int entourerLeRoi(Board board){
        int score = 0;
        int ctr = 0;
        int positionRoiX = board.getKingPositionX();
        int positionRoiY = board.getKingPositionY();

        if(board.getKingPositionX()+1 <= 12 && board.getBoard()[positionRoiY][positionRoiX+1] != 4) {
            ctr++;
            score += 5 * ctr;
        }

        if(board.getKingPositionX()-1 >= 0 && board.getBoard()[positionRoiY][positionRoiX-1] != 4) {
            ctr++;
            score += 5 * ctr;
        }

        if(board.getKingPositionY()+1 <= 12 && board.getBoard()[positionRoiY+1][positionRoiX] != 4) {
            ctr++;
            score += 5 * ctr;
        }

        if(board.getKingPositionY()-1 >= 0 && board.getBoard()[positionRoiY-1][positionRoiX] != 4) {
            ctr++;
            score += 5 * ctr;
        }

        return score;
    }

    @Override
    public boolean verifierSiRoiEntoure(Board board) {
        boolean estEntoure = true;
        int positionRoiX = board.getKingPositionX();
        int positionRoiY = board.getKingPositionY();

        if(board.getKingPositionX()+1 <= 12
                && (board.getBoard()[positionRoiY][positionRoiX+1] != 2
                || board.getBoard()[positionRoiY][positionRoiX+1] != 4)) { estEntoure = false;}

        if(board.getKingPositionX()-1 >= 0
                && (board.getBoard()[positionRoiY][positionRoiX-1] != 2
                || board.getBoard()[positionRoiY][positionRoiX-1] != 4)) { estEntoure = false; }

        if(board.getKingPositionY()+1 <= 12
                && (board.getBoard()[positionRoiY+1][positionRoiX] != 2
                || board.getBoard()[positionRoiY+1][positionRoiX] != 4)) { estEntoure = false; }

        if(board.getKingPositionY()-1 >= 0
                && (board.getBoard()[positionRoiY-1][positionRoiX] != 2
                || board.getBoard()[positionRoiY-1][positionRoiX] != 4)) { estEntoure = false; }
        return estEntoure;
    }

    public int findNearestKingExist(Board board){
        int kingColonne = board.getKingPositionX();
        int kingRange = board.getKingPositionY();
        int score = 0;

        //si le Roi est sur un coin.
        if((kingColonne == 0 && kingRange ==0) || (kingColonne == 12 && kingRange == 0) || (kingColonne == 0 && kingRange == 12) || (kingColonne == 12 & kingRange == 12)){
            score -= 500;
        }

        return 0;
    }
}
