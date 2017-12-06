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
        attackerScore += entourerLeRoi(board);
        attackerScore += mangerPion(board);

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
    //Le ctr fait en sorte que plus il y a de pions autour du roi, plus le score est élevé.
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

    //useful ?
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

    public int mangerPion(Board board){
        int[][] boardGenere = board.getBoard();
        int positionX;
        int positionY;
        int score = 0;

        //double for pour parcourir le tableau
        for (int i = 0 ; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                //le if sert à savoir s'il y a un noir à cette position
                if(boardGenere[j][i] == 2){
                    positionX = j;
                    positionY = i;
                    //le if sert à savoir s'il y a un rouge à droite du noir
                    if(positionX+1 <= 12 && board.getBoard()[j][i+1] != 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = i; k >= 0; k--){
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[j][k] == 4){
                                score += 20;
                            }
                        }
                    }
                    //le if sert à savoir s'il y a un rouge à gauche du noir
                    if(positionX-1 >= 0 && board.getBoard()[j][i-1] != 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = i; k <= 12; k++){
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[j][k] == 4){
                                score += 20;
                            }
                        }
                    }
                    //le if sert à savoir s'il y a un rouge en haut du noir
                    if(positionY+1 <= 12 && board.getBoard()[j+1][i] != 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = i; k >= 0; k--){
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[k][i] == 4){
                                score += 20;
                            }
                        }
                    }
                    //le if sert à savoir s'il y a un rouge en bas du noir
                    if(positionY-1 >= 0 && board.getBoard()[j-1][i] != 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = i; k >= 12; k--){
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[k][i] == 4){
                                score += 20;
                            }
                        }
                    }
                }
            }
        }

        return score;
    }
}
