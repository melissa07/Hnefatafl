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
    public float execute(Board board) {
        float defenderScore = 0;

        defenderScore += countNbPawnsLeft(board);
        defenderScore -= countNbPawnsAdverseLeft(board);
        //defenderScore += findNearestKingExist(board);
        defenderScore -= 0.5f*findNearestKingExistv2(board);


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
    public int countNbPawnsAdverseLeft(Board board) {
        int[][] tabBoard = board.getBoard();
        int nbPawn = 0;

        for(int i=0;i < tabBoard.length; i++){
            for(int j=0;j < tabBoard[i].length; j++){
                if(tabBoard[j][i] == 4){
                    nbPawn++;
                }
            }
        }

        return nbPawn;
    }

    //Méthode qui permet de savoir si un pion serait en danger s'il bougeait à la position précisée dans le board
    @Override
    public int verifierSiPionEstEnDanger(Board board) {
        int[][] valueBoard = board.getBoard();
        int score = 0;
        int positionPremierNoirX;
        int positionPremierNoirY;
        int positionRougeX;
        int positionRougeY;

        for (int i = 0; i < board.getBOARD_SIZE(); i++) {
            for (int j = 0; j < board.getBOARD_SIZE(); j++) {
                positionPremierNoirX = -1;
                positionPremierNoirY = -1;
                positionRougeX = -1;
                positionRougeY = -1;

                // TROUVE UN NOIR ET UN ROUGE ADJACENTS
                if(( i+1 <= 12) && (valueBoard[j][i] == 4 && valueBoard[j][i+1] == 2)) {
                    positionPremierNoirX = i+1;
                    positionPremierNoirY = j;
                    positionRougeX = i;
                    positionRougeY = j;
//                    score -=100;
                }
                else if((j+1 <= 12) && (valueBoard[j][i] == 2 && valueBoard[j+1][i] == 4)) {
                    positionPremierNoirX = i;
                    positionPremierNoirY = j;
                    positionRougeX = i;
                    positionRougeY = j+1;

//                    score -=100;
                }
                else if((i+1) <= 12 && (valueBoard[j][i] == 2 && valueBoard[j][i+1] == 4)) {
                    positionPremierNoirX = i;
                    positionPremierNoirY = j;
                    positionRougeX = i+1;
                    positionRougeY = j;
//                    score -=100;
                }
                else if((j+1) <= 12 && (valueBoard[j][i] == 4 && valueBoard[j+1][i] == 2)) {
                    positionPremierNoirX = i;
                    positionPremierNoirY = j+1;
                    positionRougeX = i;
                    positionRougeY = j;
//                    score -=100;
                }
                // FIN : TROUVE UN NOIR ET UN ROUGE ADJACENTS

                // CHERCHER UN SECOND NOIR QUI POURRAIT MANGER UN PION ROUGE
                if(positionPremierNoirX != -1 && positionPremierNoirX != -1) {
                    if(positionPremierNoirX < positionRougeX) {
                        for (int start = positionRougeX+1 ; start < board.getBOARD_SIZE(); start++ ) {
                            int rangee = 0;
                            while(rangee <= 12) {
                                if(valueBoard[rangee][start] == 2) { // Si l'on trouve un noir sur la colonne adjacente
                                    int positionSecondNoirX = start;
                                    int positionSecondNoirY = rangee;

                                    score += verifierSiPionRougeEntoureenX(valueBoard, positionRougeX,positionSecondNoirX, positionPremierNoirY);
                                }
                                rangee++;
                            }
                        }
                    }
                    if(positionPremierNoirX > positionRougeX) {
                        for (int start = positionRougeX-1 ; start >= 0; start-- ) {
                            int rangee = 0;
                            while(rangee <= 12) {
                                if(valueBoard[rangee][start] == 2) { // Si l'on trouve un noir sur la colonne adjacente
                                    int positionSecondNoirX = start;
                                    int positionSecondNoirY = rangee;

                                    score += verifierSiPionRougeEntoureenX(valueBoard, positionRougeX,positionSecondNoirX, positionPremierNoirY);
                                }
                                rangee++;
                            }
                        }
                    }
                    if(positionPremierNoirY < positionRougeY) {
                        for (int start = positionRougeY+1; start < board.getBOARD_SIZE(); start++) {
                            int colonne = 0;
                            while(colonne <= 12) {
                                if(valueBoard[start][colonne] == 2) {
                                    int positionSecondNoirY = start;
                                    int positionSecondNoirX = colonne;

                                    score += verifierSiPionRougeEntoureenY(valueBoard, positionRougeY, positionSecondNoirX, positionSecondNoirY);
                                }
                                colonne++;
                            }
                        }
                    }
                    if(positionPremierNoirY > positionRougeY) {
                        for (int start = positionRougeY-1; start >= 0; start--) {
                            int colonne = 0;
                            while(colonne <= 12) {
                                if(valueBoard[start][colonne] == 2) {
                                    int positionSecondNoirY = start;
                                    int positionSecondNoirX = colonne;

                                    score += verifierSiPionRougeEntoureenY(valueBoard, positionRougeY, positionSecondNoirX, positionSecondNoirY);

                                }
                                colonne++;
                            }
                        }
                    }
                }
                // FIN: // CHERCHER UN SECOND NOIR QUI POURRAIT MANGER UN PION ROUGE


            }
        }
        return score;
    }


    private int verifierSiPionRougeEntoureenX(int[][]valueBoard, int positionRougeX, int positionSecondNoirX, int positionSecondNoirY) {
        boolean hasObstacle = false;

        if(positionSecondNoirX > positionRougeX) {
            int curseurX = positionSecondNoirX;
            while(curseurX >= positionRougeX+1) {
                curseurX --;
                if(valueBoard[positionSecondNoirY][curseurX] != 0 && valueBoard[positionSecondNoirY][curseurX] != 1 ) { // Sil ne trouve pas de pion qui bloque le chemin continuer la loop
                    hasObstacle = true;
                }
                if(hasObstacle) {
                    return 500; // Score positif car il y a un obstacle et le pion ne peut se faire manger
                }
                else
                    return -500;
            }
        }
        else if(positionSecondNoirX < positionRougeX) {
            int curseurX = positionSecondNoirX;
            while(curseurX <= positionRougeX-1) {
                curseurX ++;
                if(valueBoard[positionSecondNoirY][curseurX] != 0 && valueBoard[positionSecondNoirY][curseurX] != 1) { // Sil ne trouve pas de pion qui bloque le chemin continuer la loop
                    hasObstacle = true;
                }
                if(hasObstacle) {
                    return 500; // Score positif car il y a un obstacle et le pion ne peut se faire manger
                }
                else
                    return -500;
            }
        }
        return 0;
    }

    private int verifierSiPionRougeEntoureenY(int[][]valueBoard, int positionRougeY, int positionSecondNoirX, int positionSecondNoirY) {
        boolean hasObstacle = false;
        if(positionSecondNoirY > positionRougeY) {
            int curseurY = positionSecondNoirY;
            while(curseurY >= positionRougeY+1) {
                curseurY --;
                if(valueBoard[curseurY][positionSecondNoirX] != 0 && valueBoard[curseurY][positionSecondNoirX] != 1 ) { // Sil ne trouve pas de pion qui bloque le chemin continuer la loop
                    hasObstacle = true;
                }
                if(hasObstacle) {
                    return 500; // Score positif car il y a un obstacle et le pion ne peut se faire manger
                }
                else
                    return -500;
            }
        }
        else if(positionSecondNoirY < positionRougeY) {
            int curseurY = positionSecondNoirY;
            while(curseurY <= positionRougeY-1) {
                curseurY ++;
                if(valueBoard[curseurY][positionSecondNoirX] != 0 && valueBoard[curseurY][positionSecondNoirX] != 1) { // Sil ne trouve pas de pion qui bloque le chemin continuer la loop
                    hasObstacle = true;
                }
                if(hasObstacle) {
                    return 500; // Score positif car il y a un obstacle et le pion ne peut se faire manger
                }
                else
                    return -500;
            }
        }

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
            score += 100000; //VICTOIRE
        }

        //verifie si le roi se rend dans un coin en un mouvement horizontal
        if(kingRange == 0 || kingRange == 12) {
            if (isRangeDroitLibre(tabBoard, kingColonne, kingRange)) {
                score += 10000;
            }
            if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange)) {
                score += 10000;
            }
            if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, kingRange)) {
                score += 30000;
            }
        }

        //verifie si le roi se rend dans un coin en un mouvement vertical
        if(kingColonne == 0 || kingColonne == 12) {
            if (isColoneHautLibre(tabBoard, kingColonne, kingRange)) {
                score += 10000;
            }
            if (isColoneBasLibre(tabBoard, kingColonne, kingRange)) {
                score += 10000;
            }
            if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isColoneBasLibre(tabBoard, kingColonne, kingRange)) {
                score += 30000;
            }
        }

        //verifie si le roi se rend dans un coin en deux mouvement vertical ensuite horizontal
        if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isRangeDroitLibre(tabBoard, kingColonne, 0)){
            score += 1000;
        }
        if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 0)){
            score += 1000;
        }
        if (isColoneBasLibre(tabBoard, kingColonne, kingRange) && isRangeDroitLibre(tabBoard, kingColonne, 12)){
            score += 1000;
        }
        if (isColoneBasLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 12)){
            score += 1000;
        }
        if (isColoneHautLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 0) && isRangeDroitLibre(tabBoard, kingColonne, 0)){
            score += 2000;
        }
        if (isColoneBasLibre(tabBoard, kingColonne, kingRange) && isRangeGaucheLibre(tabBoard, kingColonne, 12) && isRangeDroitLibre(tabBoard, kingColonne, 12)){
            score += 2000;
        }

        //verifie si le roi se rend dans un coin en deux mouvement horizontal ensuite vertical
        if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 0, kingRange)){
            score += 1000;
        }
        if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange) && isColoneBasLibre(tabBoard, 0, kingRange)){
            score += 1000;
        }
        if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 12, kingRange)){
            score += 1000;
        }
        if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isColoneBasLibre(tabBoard, 12, kingRange)){
            score += 1000;
        }
        if (isRangeGaucheLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 0, kingRange) && isColoneBasLibre(tabBoard, 0, kingRange)){
            score += 2000;
        }
        if (isRangeDroitLibre(tabBoard, kingColonne, kingRange) && isColoneHautLibre(tabBoard, 12, kingRange) && isColoneBasLibre(tabBoard, 12, kingRange)){
            score += 2000;
        }


        //si le king a bouge
        if(kingColonne != 6 && kingRange != 6)
            score += 100;

        int counter = 0;

        //si est entourrer de rouge
        if(kingColonne > 0 && tabBoard[kingColonne - 1][kingRange] == 4)
            counter++;
        if(kingColonne < 12 && tabBoard[kingColonne + 1][kingRange] == 4)
            counter++;
        if(kingRange > 0 && tabBoard[kingColonne][kingRange - 1] == 4)
            counter++;
        if(kingRange < 12 && tabBoard[kingColonne][kingRange + 1] == 4)
            counter++;

        if(counter >= 2) { score -= 75; }
        else if(counter >= 3) { score -=1000; }

        //si est entourrer de rien
        if(kingColonne > 0 && tabBoard[kingColonne - 1][kingRange] == 0)
            score += 100;
        if(kingColonne < 12 && tabBoard[kingColonne + 1][kingRange] == 0)
            score += 100;
        if(kingRange > 0 && tabBoard[kingColonne][kingRange - 1] == 0)
            score += 100;
        if(kingRange < 12 && tabBoard[kingColonne][kingRange + 1] == 0)
            score += 100;


        return score;
    }

    public int findNearestKingExistv2(Board board) {
        int kingColonne = board.getKingPositionX();
        int kingRange = board.getKingPositionY();


        int topLeft = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[0][0]);
        int bottLeft = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[0][12]);
        int topRight = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[12][0]);
        int bottRight = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[12][12]);

        int shortestDistance =  Math.min(
                                Math.min(topLeft, topRight),
                                Math.min(bottLeft, bottRight));

//        return Math.negateExact(shortestDistance*100);
        return shortestDistance;
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
