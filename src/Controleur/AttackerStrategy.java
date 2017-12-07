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
        attackerScore += hasKingEscaped(board);
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
        int[][] valueBoard = board.getBoard();
        int score = 0;
        int positionPremierNoirX;
        int positionPremierNoirY;
        int positionRougeX;
        int positionRougeY;

        for (int i = 0; i <= board.getBOARD_SIZE(); i++) {
            for (int j = 0; j <= board.getBOARD_SIZE(); j++) {
                positionPremierNoirX = -1;
                positionPremierNoirY = -1;
                positionRougeX = -1;
                positionRougeY = -1;
                // todo check when I hit a wall
                // TROUVE UN NOIR ET UN ROUGE ADJACENTS
                if((valueBoard[j][i] == 4 && valueBoard[j][i+1] == 2) && i+1 <= 12) {
                    positionPremierNoirX = i+1;
                    positionPremierNoirY = j;
                    positionRougeX = i;
                    positionRougeY = j;
                    score -=10;
                }
                else if((valueBoard[j][i] == 2 && valueBoard[j+1][i] == 4) && j+1 <= 12) {
                    positionPremierNoirX = i;
                    positionPremierNoirY = j;
                    positionRougeX = i;
                    positionRougeY = j+1;

                    score -=10;
                }
                else if((valueBoard[j][i] == 2 && valueBoard[j][i+1] == 4) && i+1 <= 12) {
                    positionPremierNoirX = i;
                    positionPremierNoirY = j;
                    positionRougeX = i+1;
                    positionRougeY = j;
                    score -=10;
                }
                else if((valueBoard[j][i] == 4 && valueBoard[j+1][i] == 2) && j+1 <= 12) {
                    positionPremierNoirX = i;
                    positionPremierNoirY = j+1;
                    positionRougeX = i;
                    positionRougeY = j;
                    score -=10;
                }
                // FIN : TROUVE UN NOIR ET UN ROUGE ADJACENTS

                // CHERCHER UN SECOND NOIR QUI POURRAIT MANGER UN PION ROUGE
                if(positionPremierNoirX != -1 && positionPremierNoirX != -1) {
                    if(positionPremierNoirX < positionRougeX) {
                        for (int start = positionRougeX+1 ; start < board.getBOARD_SIZE(); start++ ) {
                            
                        }
                    }
                    if(positionPremierNoirX > positionRougeX) {}
                    if(positionPremierNoirY < positionRougeY) {}
                    if(positionPremierNoirY > positionRougeY) {}
                }
                // FIN: // CHERCHER UN SECOND NOIR QUI POURRAIT MANGER UN PION ROUGE


            }
        }
        //Todo vérifier si DANS LE BOARD un pion (n'importe lequel) a des chances d'être mangé
        return false;
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
                prioritairesScore += 50;
            j++;
        }

        // Coin superieur droit
        j=0;
        for (int i = 10; i <= 12; i++) {
            if(board[j][i] == 4)
                prioritairesScore += 50;
            j++;
        }

        // Coin inferieur gauche
        j=10;
        for (int i = 2; i >= 0; i--) {
            if(board[j][i] == 4)
                prioritairesScore += 50;
            j++;
        }

        // Coin inferieur droit
        j=10;
        for (int i = 10; i <= 12; i++) {
            if(board[j][i] == 4)
                prioritairesScore += 50;
            j++;
        }

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


    public int findNearestKingExist(Board board){
        int kingColonne = board.getKingPositionX();
        int kingRange = board.getKingPositionY();


        int topLeft = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[0][0]);
        int bottLeft = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[0][12]);
        int topRight = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[12][0]);
        int bottRight = Math.abs(board.getBoard()[kingRange][kingColonne]+ board.getBoard()[0][12]);

        int shortestDistance = Math.min(
                Math.min(topLeft, topRight),
                Math.min(bottLeft, bottRight)
        );

//        System.out.println("Distance entre le roi et la sortie: " +Math.negateExact(shortestDistance*100));

        return Math.negateExact(shortestDistance*100);
    }

    public int hasKingEscaped(Board board) {
        int kingColonne = board.getKingPositionX();
        int kingRange = board.getKingPositionY();
        int score = 0;

//        Math.abs(board.getBoard()[3][4]+ board.getBoard()[1][5]);
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
                if(boardGenere[y][x] == 2){
                    positionX = x;
                    positionY = y;

//region X+1 recherche sur même ligne
                    //le if sert à savoir s'il y a un rouge à droite du noir
                    if(positionX+1 <= 12 && board.getBoard()[y][x+1] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = positionX; k >= 0; k--){
                            //Si un pion noir est sur le chemin, on arrête le for
                            if(boardGenere[y][k] == 2){
                                break;
                            }
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[y][k] == 4){
                                score += 20;
                                trouve = true;
                            }
                        }
                    }
//region recherche sur colonne ajdacente
                    //If permettant de savoir si on a trouvé un rouge sur la même ligne, sinon le for qui suit
                    //permet de regarder la colonne avant le pion noir
                    if(trouve == false) {
                        //le for sert à chercher un rouge sur la colonne avant le pion noir. (vers le haut)
                        for (int k = positionX - 1; k >= 12; k++) {
                            //Si un pion noir est sur le chemin, on arrête le for
                            if (boardGenere[k][x] == 2) {
                                break;
                            }
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if (boardGenere[k][x] == 4) {
                                score += 20;
                                trouve = true;
                            }
                        }
                    }
                    if(trouve == false){
                        for(int k = positionX-1; k <= 0; k--){
                            //Si un pion noir est sur le chemin, on arrête le for
                            if(boardGenere[k][x] == 2){
                                break;
                            }
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[k][x] == 4){
                                score += 20;
                                trouve = true;
                            }
                        }
                    }
//endregion
//endregion

//region X-1 recherche sur même ligne

                    //le if sert à savoir s'il y a un rouge à gauche du noir
                    if(positionX-1 >= 0 && board.getBoard()[y][x-1] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = positionX; k <= 12; k++){
                            //Si un pion noir est sur le chemin, on arrête le for
                            if(boardGenere[y][k] == 2){
                                break;
                            }
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[y][k] == 4){
                                score += 20;
                                trouve = true;
                            }
                        }

                        //If permettant de savoir si on a trouvé un rouge sur la même ligne
                        if(trouve == false) {
                            //le for sert à chercher un rouge sur la colonne avant le pion noir. (vers le haut)
                            for (int k = positionX + 1; k >= 12; k++) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[k][x] == 2) {
                                    break;
                                }
                                //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[k][x] == 4) {
                                    score += 20;
                                    trouve = true;
                                }
                            }
                        }

                        if(trouve == false) {
                            //le for sert à chercher un rouge sur la colonne avant le pion noir. (vers le haut)
                            for (int k = positionX + 1; k <= 0; k--) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[k][x] == 2) {
                                    break;
                                }
                                //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[k][x] == 4) {
                                    score += 20;
                                    trouve = true;
                                }
                            }
                        }
                    }
//endregion

//region Y-1 recherche sur la même colonne
                    //le if sert à savoir s'il y a un rouge en haut du noir
                    if(positionY+1 <= 12 && board.getBoard()[y+1][x] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = positionY; k >= 0; k--){
                            //Si un pion noir est sur le chemin, on arrête le for
                            if(boardGenere[k][x] == 2){
                                break;
                            }
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[k][x] == 4){
                                score += 20;
                                trouve = true;
                            }
                        }

                        //If permettant de savoir si on a trouvé un rouge sur la même ligne
                        if(trouve == false) {
                            //le for sert à chercher un rouge sur la colonne avant le pion noir. (vers le haut)
                            for (int k = positionY + 1; k >= 12; k++) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[y][k] == 2) {
                                    break;
                                }
                                //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[y][k] == 4) {
                                    score += 20;
                                    trouve = true;
                                }
                            }
                        }

                        if(trouve == false) {
                            //le for sert à chercher un rouge sur la colonne avant le pion noir. (vers le haut)
                            for (int k = positionY + 1; k <= 0 ; k--) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[y][k] == 2) {
                                    break;
                                }
                                //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[y][k] == 4) {
                                    score += 20;
                                    trouve = true;
                                }
                            }
                        }
                    }
//endregion

//region Y+1 recherche sur la même colonne
                    //le if sert à savoir s'il y a un rouge en bas du noir
                    if(positionY-1 >= 0 && board.getBoard()[y-1][x] == 4) {
                        //plus 10 parce qu'on a un pion collé dessus, donc possibilité de le manger
                        score += 10;
                        //le for sert à chercher un rouge sur la même ligne
                        for(int k = positionY; k >= 12; k--){
                            //Si un pion noir est sur le chemin, on arrête le for
                            if(boardGenere[k][x] == 2){
                                break;
                            }
                            //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                            if(boardGenere[k][x] == 4){
                                score += 20;
                                trouve = true;
                            }
                        }

                        //If permettant de savoir si on a trouvé un rouge sur la même ligne, sinon le for qui suit
                        //permet de regarder la colonne avant le pion noir
                        if(trouve == false) {
                            //le for sert à chercher un rouge sur la colonne avant le pion noir. (vers le haut)
                            for (int k = positionY + 1; k >= 12; k++) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[y][k] == 2) {
                                    break;
                                }
                                //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[y][k] == 4) {
                                    score += 20;
                                    trouve = true;
                                }
                            }
                        }

                        if(trouve == false) {
                            //le for sert à chercher un rouge sur la colonne avant le pion noir. (vers le haut)
                            for (int k = positionY + 1; k <= 0 ; k--) {
                                //Si un pion noir est sur le chemin, on arrête le for
                                if (boardGenere[y][k] == 2) {
                                    break;
                                }
                                //Si sur la même ligne y'a un rouge, +20 parce que possiblité de le manger confirme
                                if (boardGenere[y][k] == 4) {
                                    score += 20;
                                    trouve = true;
                                }
                            }
                        }
                    }
                    //endregion
                }
            }
        }

        return score;
    }
}
