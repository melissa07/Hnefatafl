package Controleur;

import Modele.Board;

public class AttackerStrategy implements IStrategy {
    private static AttackerStrategy attackerSingleton = null;


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
        boolean priorisees = verifierSiCasesPrioritairesOccupees(board); // 2nde strategie de calcul de board
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
    public boolean verifierSiCasesPrioritairesOccupees(int[][] board) {
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

    public int findNearestKingExist(Board board) {
        int[][] tabBoard = board.getBoard();
        int kingColonne = board.getKingPositionX();
        int kingRange = board.getKingPositionY();
        int score = 0;

        //si le Roi est sur un coin.
        if((kingColonne == 0 && kingRange ==0) || (kingColonne == 12 && kingRange == 0) || (kingColonne == 0 && kingRange == 12) || (kingColonne == 12 & kingRange == 12)){
            score += 500;
        }

        //verifie si se rend dans un coin en un mouvement horizontal
        if(kingRange == 0 || kingRange == 12) {
            if (isRangeDroitLibre(tabBoard, kingColonne, kingRange)) {
                score += 20;
            }
            if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange)) {
                score += 20;
            }
            if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, kingRange)) {
                score += 60;
            }
        }

        //verifie si se rend dans un coin en un mouvement vertical
        if(kingColonne == 0 || kingColonne == 12) {
            if (isColoneHautLibre(tabBoard, kingColonne, kingRange)) {
                score += 20;
            }
            if (isColoneBasLibre(tabBoard, kingColonne, kingRange)) {
                score += 20;
            }
            if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isColoneBasLibre(tabBoard, kingColonne, kingRange)) {
                score += 60;
            }
        }

        //verifie si se rend dans un coin en deux mouvement vertical ensuite horizontal
        if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isRangeDroitLibre(tabBoard, kingColonne, 0)){
            score += 5;
        }
        if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 0)){
            score += 5;
        }
        if (isColoneBasLibre(tabBoard, kingColonne, kingRange) && isRangeDroitLibre(tabBoard, kingColonne, 12)){
            score += 5;
        }
        if (isColoneBasLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 12)){
            score += 5;
        }
        if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 0) && isRangeDroitLibre(tabBoard, kingColonne, 0)){
            score += 10;
        }
        if (isColoneBasLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 12) && isRangeDroitLibre(tabBoard, kingColonne, 12)){
            score += 10;
        }

        //verifie si se rend dans un coin en deux mouvement horizontal ensuite vertical
        if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 0, kingRange)){
            score += 5;
        }
        if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange) && isColoneBasLibre(tabBoard, 0, kingRange)){
            score += 5;
        }
        if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 12, kingRange)){
            score += 5;
        }
        if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isColoneBasLibre(tabBoard, 12, kingRange)){
            score += 5;
        }
        if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 0, kingRange) && isColoneBasLibre(tabBoard, 0, kingRange)){
            score += 10;
        }
        if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 12, kingRange) && isColoneBasLibre(tabBoard, 12, kingRange)){
            score += 10;
        }

        //si est entourrer de rouge
        if(kingColonne > 0 && tabBoard[kingColonne - 1][kingRange] == 4)
            score -= 5;
        if(kingColonne < 12 && tabBoard[kingColonne + 1][kingRange] == 4)
            score -= 5;
        if(kingRange > 0 && tabBoard[kingColonne][kingRange - 1] == 4)
            score -= 5;
        if(kingColonne < 12 && tabBoard[kingColonne][kingRange + 1] == 4)
            score -= 5;

        //si est entourrer de noire
        if(kingColonne > 0 && tabBoard[kingColonne - 1][kingRange] == 2)
            score -= 2;
        if(kingColonne < 12 && tabBoard[kingColonne + 1][kingRange] == 2)
            score -= 2;
        if(kingRange > 0 && tabBoard[kingColonne][kingRange - 1] == 2)
            score -= 2;
        if(kingColonne < 12 && tabBoard[kingColonne][kingRange + 1] == 2)
            score -= 2;


        return score;
    }

    private boolean isColoneHautLibre(int[][] tabBoard, int colonne, int range){
        boolean isLibre = true;

        for(int i=range - 1; i >= 0;i--){
            if(tabBoard[colonne][i] > 1){
                isLibre = false;
            }
        }

        return isLibre;
    }

    private boolean isColoneBasLibre(int[][] tabBoard, int colonne, int range){
        boolean isLibre = true;

        for(int i=range + 1; i < 12;i++){
            if(tabBoard[colonne][i] > 1){
                isLibre = false;
            }
        }

        return isLibre;
    }

    private boolean isRangeGaucheLibre(int[][] tabBoard, int colonne, int range){
        boolean isLibre = true;

        for(int i=colonne - 1; i >= 0;i--){
            if(tabBoard[i][range] > 1){
                isLibre = false;
            }
        }

        return isLibre;
    }

    private boolean isRangeDroitLibre(int[][] tabBoard, int colonne, int range){
        boolean isLibre = true;

        for(int i=colonne + 1; i < 12;i++){
            if(tabBoard[i][range] > 1){
                isLibre = false;
            }
        }

        return isLibre;
    }

}
