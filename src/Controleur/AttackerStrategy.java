package Controleur;

import Modele.Board;

public class AttackerStrategy implements IStrategy {
    private static AttackerStrategy attackerSingleton = null;
//    private int score;

    public static AttackerStrategy getInstance() {
        if(attackerSingleton == null) {
            attackerSingleton = new AttackerStrategy();
        }
        return attackerSingleton;
    }

    @Override
    /** STRATEGIES + POINTS
     * Nombre de pions : 100
     * Sortie la plus proche du roi: -1500
     * Cases prioritaires occupees: 1000
     * Pion en danger: 100
     * Roi entoure: 1500
     * Roi echape: 10000
     * Manger un pion: ?
     */
    public int execute(Board board) {
        int attackerScore = 0;
        attackerScore += countNbPawnsLeft(board);
        attackerScore += findNearestKingExist(board);
        attackerScore += verifierSiCasesPrioritairesOccupees(board.getBoard());
        attackerScore += verifierSiPionEstEnDanger(board);
        attackerScore += verifierSiRoiEntoure(board);
        attackerScore += entourerLeRoi(board);
        attackerScore += hasKingEscaped(board);
        attackerScore += mangerPion(board);

        return attackerScore;
    }

    @Override
    public int countNbPawnsLeft(Board board) {
        int[][] tabBoard = board.getBoard();
        int nbPawns = 0;

        for(int i=0;i < tabBoard.length; i++){
            for(int j=0;j < tabBoard[i].length; j++){
                if(tabBoard[j][i] == 4 ){
                    nbPawns += 100;
                }
            }
        }
        return nbPawns;
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
        int counter = 0;
        int prioritairesScore = 0;
        int j;

        // Coin superieur gauche
        j=0;
        for (int i = 2; i >= 0; i--) {
            if(board[j][i] == 4)
                prioritairesScore += 1000;
            j++;
        }

        // Coin superieur droit
        j=0;
        for (int i = 10; i <= 12; i++) {
            if(board[j][i] == 4)
                prioritairesScore += 1000;
            j++;
        }

        // Coin inferieur gauche
        j=10;
        for (int i = 2; i >= 0; i--) {
            if(board[j][i] == 4)
                prioritairesScore += 1000;
            j++;
        }

        // Coin inferieur droit
        j=10;
        for (int i = 10; i <= 12; i++) {
            if(board[j][i] == 4)
                prioritairesScore += 1000;
            j++;
        }

        return prioritairesScore;
    }

    //Le ctr fait en sorte que plus il y a de pions autour du roi, plus le score est élevé.
    public int entourerLeRoi(Board board){
        int score = 0;
        float ctr = 0;
        int positionRoiX = board.getKingPositionX();
        int positionRoiY = board.getKingPositionY();

//region position du roi en X sur les bords
        if(board.getKingPositionX() == 0 || board.getKingPositionX() == 12) {
            //Si en haut ou en bas du roi c'est un X de sortie
            if(board.getBoard()[positionRoiY+1][positionRoiX] == 1 || board.getBoard()[positionRoiY-1][positionRoiX] == 1){
                //si en haut ou en bas du roi c'est un pion rouge (si X en haut alors rouge en bas et vice versa)
                if(board.getBoard()[positionRoiY+1][positionRoiX] == 4 || board.getBoard()[positionRoiY-1][positionRoiX] == 4){
                    //si le roi est sur le bord à droite
                    if(board.getKingPositionX() == 12){
                        //si à la gauche du roi c'est un rouge
                        if(board.getBoard()[positionRoiY][positionRoiX-1] == 4){
                            score += 500; //VICTOIRE roi entouré par bord, pion haut ou bas et pion gauche
                        }
                    }
                    //si le roi est sur le bord gauche
                    if(board.getKingPositionX() == 0){
                        //si à la droite du roi c'est un pion rouge
                        if(board.getBoard()[positionRoiY][positionRoiX+1] == 4){
                            score += 500; //VICTOIRE roi entouré par bord, pion haut ou bas et pion droite
                        }
                    }
                }
            }
            //si le roi est sur le bord à droite
            if (board.getKingPositionX() == 12) {
                //si à gauche du roi y'a un rouge OU un X de sortie
                if (board.getBoard()[positionRoiY][positionRoiX - 1] == 4) {
                    ctr = ctr + 1 + (1/3);
                    score += 5 * ctr;
                }
            }
            //si le roi est sur le bord à gauche
            if (board.getKingPositionX() == 0) {
                //si à droite du roi y'a un rouge
                if (board.getBoard()[positionRoiY][positionRoiX + 1] == 4) {
                    ctr = ctr + 1 + (1/3);
                    score += 5 * ctr;
                }
            }
            //si en haut du roi y'a un rouge
            if (board.getBoard()[positionRoiY + 1][positionRoiX] == 4) {
                ctr = ctr + 1 + (1/3);
                score += 5 * ctr;
            }
            //si en bas du roi y'au un rouge
            if (board.getBoard()[positionRoiY - 1][positionRoiX] == 4) {
                ctr = ctr + 1 + (1/3);
                score += 5 * ctr;
            }
        }
//endregion

//region position du roi en Y sur les bords
        if(board.getKingPositionY() == 0 || board.getKingPositionY() == 12){
            //si à la droite ou à la gauche du roi c'est un X de sortie
            if(board.getBoard()[positionRoiY][positionRoiX+1] == 1 || board.getBoard()[positionRoiY][positionRoiX-1] == 1){
                //si à la droite ou à la gauche du roi c'est un pion rouge (si X à droite alors rouge à gauche et vice versa)
                if(board.getBoard()[positionRoiY][positionRoiX+1] == 4 || board.getBoard()[positionRoiY][positionRoiX-1] == 4){
                    //si le roi est sur le bord en haut
                    if(board.getKingPositionY() == 12){
                        //si en bas du roi c'est un rouge
                        if(board.getBoard()[positionRoiY-1][positionRoiX] == 4){
                            score += 500; //VICTOIRE
                        }
                    }
                    //si le roi est sur le bord en bas
                    if(board.getKingPositionX() == 0){
                        //si en haut  du roi c'est un pion rouge
                        if(board.getBoard()[positionRoiY+1][positionRoiX] == 4){
                            score += 500; //VICTOIRE
                        }
                    }
                }
            }
            //si le roi est sur le bord à droite
            if(board.getKingPositionY() == 12) {
                //si à gauche du roi y'a un rouge
                if(board.getBoard()[positionRoiY-1][positionRoiX] == 4) {
                    ctr = ctr + 1 + (1/3);
                    score += 5 * ctr;
                }
            }
            //si le roi est sur le bord à gauche
            if(board.getKingPositionY() == 0) {
                //si à droite du roi y'a un rouge
                if(board.getBoard()[positionRoiY+1][positionRoiX] == 4) {
                    ctr = ctr + 1 + (1/3);
                    score += 5 * ctr;
                }
            }
            //si en haut du roi y'a un rouge
            if(board.getBoard()[positionRoiY][positionRoiX+1] == 4){
                ctr = ctr + 1 + (1/3);
                score += 5 * ctr;
            }
            //si en bas du roi y'au un rouge
            if(board.getBoard()[positionRoiY][positionRoiX-1] == 4){
                ctr = ctr + 1 + (1/3);
                score += 5 * ctr;
            }

            if(ctr == 3){
                score += 500; //VICTOIRE
            }
        }
//endregion

//region position roi à côté du centre
        //todo vérifier si le roi est sur le côté du centre et s'il peut être entouré
//endregion

//region position du roi n'importe où ailleurs dans le board
        else{
            if(board.getKingPositionX()+1 <= 12 && board.getBoard()[positionRoiY][positionRoiX+1] == 4) {
                ctr++;
                score += 5 * ctr;
            }

            if(board.getKingPositionX()-1 >= 0 && board.getBoard()[positionRoiY][positionRoiX-1] == 4) {
                ctr++;
                score += 5 * ctr;
            }

            if(board.getKingPositionY()+1 <= 12 && board.getBoard()[positionRoiY+1][positionRoiX] == 4) {
                ctr++;
                score += 5 * ctr;
            }

            if(board.getKingPositionY()-1 >= 0 && board.getBoard()[positionRoiY-1][positionRoiX] == 4) {
                ctr++;
                score += 5 * ctr;
            }
            //If permettant de savoir si, dans le board, le roi serait entouré de quatre pions. Si oui, boost le score
            if(ctr == 4){
                score += 500; //VICTOIRE
            }
        }
//endregion


        return score;
    }

    @Override
    public int verifierSiRoiEntoure(Board board) {
        int score = 0;
        int positionRoiX = board.getKingPositionX();
        int positionRoiY = board.getKingPositionY();

        if(board.getKingPositionX()+1 <= 12
                && (board.getBoard()[positionRoiY][positionRoiX+1] == 2
                || board.getBoard()[positionRoiY][positionRoiX+1] == 4)) { score += 1500 ;}

        if(board.getKingPositionX()-1 >= 0
                && (board.getBoard()[positionRoiY][positionRoiX-1] == 2
                || board.getBoard()[positionRoiY][positionRoiX-1] == 4)) { score += 1500 ; }

        if(board.getKingPositionY()+1 <= 12
                && (board.getBoard()[positionRoiY+1][positionRoiX] == 2
                || board.getBoard()[positionRoiY+1][positionRoiX] == 4)) { score += 1500 ; }

        if(board.getKingPositionY()-1 >= 0
                && (board.getBoard()[positionRoiY-1][positionRoiX] == 2
                || board.getBoard()[positionRoiY-1][positionRoiX] == 4)) { score += 1500 ; }
        return score;
    }

    public int findNearestKingExist(Board board) {
        int kingColonne = board.getKingPositionX();
        int kingRange = board.getKingPositionY();


        int topLeft = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[0][0]);
        int bottLeft = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[0][12]);
        int topRight = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[12][0]);
        int bottRight = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[12][12]);

        return  Math.min(
                Math.min(topLeft, topRight),
                Math.min(bottLeft, bottRight)
        )*1000;

//        return Math.negateExact(shortestDistance*100);
    }

    public int hasKingEscaped(Board board) {
        int kingColonne = board.getKingPositionX();
        int kingRange = board.getKingPositionY();
        int score = 0;

        //si le Roi est sur un coin.
        if((kingColonne == 0 && kingRange ==0) || (kingColonne == 12 && kingRange == 0) || (kingColonne == 0 && kingRange == 12) || (kingColonne == 12 & kingRange == 12)){
            score -= 10000; // worst case ever
        }
        return score;
    }

    public int mangerPion(Board board){
        int[][] boardGenere = board.getBoard();
        int positionX;
        int positionY;
        int score = 0;
        boolean trouve = false;

        //double for pour parcourir le tableau
        for (int x = 0 ; x < 13; x++) {
            for (int y = 0; y < 13; y++) {

                //le if sert à savoir s'il y a un noir à cette position
                if (boardGenere[y][x] == 2) {
                    positionX = x;
                    positionY = y;

                    // region recherche sur la même colonne le haut et ligne du haut vers la gauche et vers la droite
                    //le if sert à savoir s'il y a un rouge en bas du noir
                    if (positionX + 1 <= 12 && board.getBoard()[y][x + 1] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la colonne à gauche
                        if(positionX + 2 >= 2) {
                            for (int rechercheColonneVersLeHaut = positionX - 2; rechercheColonneVersLeHaut >= 0; rechercheColonneVersLeHaut--) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[y][rechercheColonneVersLeHaut] == 2) {
                                    break;
                                }
                                //Si sur la colonne de gauche y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[y][rechercheColonneVersLeHaut] == 4) {
                                    score += 20;
                                    trouve = true;
                                    break;
                                }
                            }
                        }

                        //region recherche sur ligne du haut vers la gauche
                        if (trouve == false) {
                            if(positionX >= 1) {
                                for (int rechercheLigneSupVersLaGauche = positionY - 1; rechercheLigneSupVersLaGauche > 0; rechercheLigneSupVersLaGauche--) {
                                    if (boardGenere[rechercheLigneSupVersLaGauche][x - 1] == 2) {
                                        break;
                                    }
                                    if (boardGenere[rechercheLigneSupVersLaGauche][x - 1] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion

                        //region recherche sur ligne du haut vers la droite
                        if (trouve == false) {
                            if(positionX >= 1) {
                                for (int rechercheLigneSupVersLaDroite = positionY + 1; rechercheLigneSupVersLaDroite <= 12; rechercheLigneSupVersLaDroite++) {
                                    //x - 1 pour signifer vers le haut
                                    if (boardGenere[rechercheLigneSupVersLaDroite][x - 1] == 2) {
                                        break;
                                    }
                                    //x - 1 pour signifer vers le haut
                                    if (boardGenere[rechercheLigneSupVersLaDroite][x - 1] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion
                    }
                    //endregion

                    //region recherche sur la même colonne le bas et ligne du bas vers la gauche et vers la droite
                    //le if sert à savoir s'il y a un rouge en bas du noir
                    if (positionX - 1 >= 0 && board.getBoard()[y][x - 1] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la colonne à gauche
                        if(positionX - 2 <= 10) {
                            for (int rechercheColonneVersLeBas = positionX + 2; rechercheColonneVersLeBas <= 12; rechercheColonneVersLeBas++) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[y][rechercheColonneVersLeBas] == 2) {
                                    break;
                                }
                                //Si vers le bas y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[y][rechercheColonneVersLeBas] == 4) {
                                    score += 20;
                                    trouve = true;
                                    break;
                                }
                            }
                        }
                        //region recherche sur ligne du bas vers la gauche
                        if (trouve == false) {
                            if(positionX <= 11){
                                for (int rechercheLigneInfVersLaGauche = positionY - 1; rechercheLigneInfVersLaGauche >= 0; rechercheLigneInfVersLaGauche--) {
                                    if (boardGenere[rechercheLigneInfVersLaGauche][x + 1] == 2) {
                                        break;
                                    }
                                    if (boardGenere[rechercheLigneInfVersLaGauche][x + 1] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion

                        //region recherche sur ligne du haut vers la droite
                        if (trouve == false) {
                            if(positionX <= 11) {
                                for (int rechercheLigneSupVersLaDroite = positionY + 1; rechercheLigneSupVersLaDroite <= 12; rechercheLigneSupVersLaDroite++) {
                                    //x - 1 pour signifer vers le haut
                                    if (boardGenere[rechercheLigneSupVersLaDroite][x + 1] == 2) {
                                        break;
                                    }
                                    //x - 1 pour signifer vers le haut
                                    if (boardGenere[rechercheLigneSupVersLaDroite][x + 1] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion
                    }
                    //endregion

                    //region recherche sur la même ligne la gauche et colonne de gauche vers le haut et vers le bas
                    //le if sert à savoir s'il y a un rouge en bas du noir
                    if (positionY + 1 <= 12 && board.getBoard()[y + 1][x] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;

                        //le for sert à chercher un rouge sur la colonne à gauche
                        if(positionY + 2 >= 2 ) {
                            for (int rechercheLigneVersLaGauche = positionY - 2; rechercheLigneVersLaGauche >= 0; rechercheLigneVersLaGauche--) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[rechercheLigneVersLaGauche][x] == 2) {
                                    break;
                                }
                                if (boardGenere[rechercheLigneVersLaGauche][x] == 4) {
                                    score += 20;
                                    trouve = true;
                                    break;
                                }
                            }
                        }

                        //region recherche sur colonne de gauche vers le haut
                        if (trouve == false) {
                            if(positionY >= 1) {
                                for (int rechercheColonneGaucheVersHaut = positionX - 1; rechercheColonneGaucheVersHaut >= 0; rechercheColonneGaucheVersHaut--) {
                                    if (boardGenere[y - 1][rechercheColonneGaucheVersHaut] == 2) {
                                        break;
                                    }
                                    if (boardGenere[y - 1][rechercheColonneGaucheVersHaut] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion

                        //region recherche sur colonne de gauche vers le bas
                        if (trouve == false) {
                            if(positionY >= 1) {
                                for (int rechercheColonneGaucheVersBas = positionX + 1; rechercheColonneGaucheVersBas <= 12; rechercheColonneGaucheVersBas++) {
                                    if (boardGenere[y - 1][rechercheColonneGaucheVersBas] == 2) {
                                        break;
                                    }
                                    if (boardGenere[y - 1][rechercheColonneGaucheVersBas] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion
                    }
                    //endregion

                    //region recherche sur la même ligne la droite et colonne de droite vers le haut et vers le bas
                    //le if sert à savoir s'il y a un rouge en bas du noir
                    if (positionY - 1 >= 0 && board.getBoard()[y - 1][x] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la colonne à gauche

                        if(positionY -2 <= 10) {
                            for (int rechercheLigneVersLaDroite = positionY + 2; rechercheLigneVersLaDroite <= 12; rechercheLigneVersLaDroite++) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[rechercheLigneVersLaDroite][x] == 2) {
                                    break;
                                }
                                if (boardGenere[rechercheLigneVersLaDroite][x] == 4) {
                                    score += 20;
                                    trouve = true;
                                    break;
                                }
                            }
                        }

                        //region recherche sur colonne de droite vers le haut
                        if (trouve == false) {
                            if(positionY <= 11) {
                                for (int rechercheColonneDroiteVersHaut = positionX - 1; rechercheColonneDroiteVersHaut >= 0; rechercheColonneDroiteVersHaut--) {
                                    if (boardGenere[y + 1][rechercheColonneDroiteVersHaut] == 2) {
                                        break;
                                    }
                                    if (boardGenere[y + 1][rechercheColonneDroiteVersHaut] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion

                        //region recherche sur colonne de droite vers le bas
                        if (trouve == false) {
                            if(positionY <= 11) {
                                for (int rechercheColonneDroiteVersBas = positionX + 1; rechercheColonneDroiteVersBas <= 12; rechercheColonneDroiteVersBas++) {
                                    if (boardGenere[y + 1][rechercheColonneDroiteVersBas] == 2) {
                                        break;
                                    }
                                    if (boardGenere[y + 1][rechercheColonneDroiteVersBas] == 4) {
                                        score += 20;
                                        trouve = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //endregion
                    }
                    //endregion

                }
            }
        }
        return score;
    }
}
