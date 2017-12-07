package Controleur;

import Modele.Board;

public class DefenderStrategy implements IStrategy {

    private static DefenderStrategy defenderSingleton = null;

    public static DefenderStrategy getInstance() {
        if(defenderSingleton == null) {
            defenderSingleton = new DefenderStrategy();
        }
        return defenderSingleton;
    }

    @Override
    public int execute(Board board) {
        int defenderScore = 0;

        defenderScore += 2 * countNbPawnsLeft(board);
        defenderScore += findNearestKingExist(board);

        return defenderScore;
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

    @Override
    public int verifierSiPionEstEnDanger(Board board) {
        return 0;
    }

    @Override
    public int verifierSiCasesPrioritairesOccupees(int[][] board) {
        return 0;
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

        //verifie si le roi se rend dans un coin en un mouvement horizontal
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

        //verifie si le roi se rend dans un coin en un mouvement vertical
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

        //verifie si le roi se rend dans un coin en deux mouvement vertical ensuite horizontal
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

        //verifie si le roi se rend dans un coin en deux mouvement horizontal ensuite vertical
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

    @Override
    public int verifierSiRoiEntoure(Board board) {
        return 0;
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
