package Controleur;

import Modele.Board;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MinMaxAlphaBeta {
    public final int ROUGE = 4;
    public final int NOIR = 2;
    public final int KING = 5;
    private static int score = 0;
    private static final int maxProfondeur = 3;

    private static final String FILENAME = "boards.txt";
    private static BufferedWriter bw = null;
    private static FileWriter fw = null;

 // player may be "computer" or "opponent"
    public String doMinMax(Board actualBoard, int player){
        Board maxBoard = MaxMove(actualBoard, player, 0, 0, 0);
        maxBoard.findKingInBoard(maxBoard); // reset les coordonnees du roi
//        System.out.println("Position roi en X: " +maxBoard.getKingPositionX()+ ", position en Y: " +maxBoard.getKingPositionY());
        System.out.println("***************************");
        maxBoard.printBoard();

        return fetchLastMadeMove(actualBoard, maxBoard);
    }

    private String fetchLastMadeMove(Board actualBoard, Board maxBoard) {
        String moveDepart = null;
        String moveArrivee = null;

        for (int i = 0; i < actualBoard.getBOARD_SIZE(); i++) {
            for (int j = 0; j < actualBoard.getBOARD_SIZE(); j++) {
                if(actualBoard.getBoard()[i][j] != maxBoard.getBoard()[i][j]) {
                    if(maxBoard.getBoard()[i][j] == 0 || ((i == 6 && j ==6) && (maxBoard.getBoard()[i][j] == 1))) {
                        moveDepart = String.valueOf((char)(i+65)).toUpperCase()+String.valueOf(13-j);
                    }
                    else {
                        moveArrivee = String.valueOf((char)(i+65)).toUpperCase()+String.valueOf(13-j);
                    }
                }
            }
        }
        if(moveArrivee == null || moveDepart == null){
            System.out.print("test");
        }
        //String move = "3 "+ moveDepart+" - "+moveArrivee; // 3 pour dire qu'un move est effectue
        String move = moveDepart+" - "+moveArrivee;
        System.out.println("Le move est: " +move);

        return  move;
    }

    private Board MaxMove (Board actualBoard, int couleurJoueur, int profondeur, int alpha, int beta){
        if (IsGameOver(actualBoard) || profondeur == maxProfondeur) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = actualBoard;
            Board bestSavedBoard = actualBoard;
            int bestScore = 0;

            ArrayList<Board> boards = new ArrayList<Board>();
            if(couleurJoueur == NOIR) {
                boards = generateMoves(actualBoard, NOIR);
            }else if(couleurJoueur == ROUGE){
                boards = generateMoves(actualBoard, ROUGE);
            }

            for (Board board : boards) {
                Board savedBoard = board;
                board = MinMove(executeMove(board), couleurJoueur, profondeur + 1, alpha, beta);

                int boardScore = 0;
                if(couleurJoueur == ROUGE)
                    boardScore = AttackerStrategy.getInstance().execute(board);
                else if(couleurJoueur == NOIR)
                    boardScore = DefenderStrategy.getInstance().execute(board);
                // board.getScore() will be removed. Code above will be used
//                int boardScore = board.getScore();
                if (boardScore > bestScore) {
                    if(profondeur == 0){
                        bestSavedBoard = savedBoard;
                    }
                    bestScore = boardScore;
                    bestBoard = board;
                    alpha = boardScore;
                }
                // Ignore remaining moves
                if (beta > alpha) {
                    if (profondeur == 0) {
                        return bestSavedBoard;
                    }
                    return bestBoard;
                }
            }
            if(profondeur == 0){
                return bestSavedBoard;
            }
            return bestBoard;
        }
    }
    
    private Board MinMove(Board actualBoard, int couleurJoueur, int profondeur, int alpha, int beta) {
        if (IsGameOver(actualBoard) || profondeur == maxProfondeur) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = actualBoard;
            int bestScore = 0;
            ArrayList<Board> boards = new ArrayList<Board>();
            if(couleurJoueur == NOIR){
                boards = generateMoves(actualBoard, ROUGE);
            }else if(couleurJoueur == ROUGE){
                boards = generateMoves(actualBoard, NOIR);
            }
            for (Board board : boards) {
                board = MaxMove(executeMove(board), couleurJoueur, profondeur + 1, alpha, beta);

                int boardScore = 0;
                if(couleurJoueur == ROUGE)
                    boardScore = DefenderStrategy.getInstance().execute(board);
                else if(couleurJoueur == NOIR)
                    boardScore = AttackerStrategy.getInstance().execute(board);

                // board.getScore() will be removed. Code above will be used
//                int boardScore = board.getScore();
                if (boardScore > bestScore) {
                    bestScore = boardScore;
                    bestBoard = board;
                    beta = boardScore;
                }
                // Ignore remaining moves
                if (beta < alpha)
                    return bestBoard;
            }
            return bestBoard;
        }
    }

    private boolean IsGameOver(Board board) {
        return false;
    }

    private Board executeMove(Board board) {
        return board;
    }

    private ArrayList<Board> generateMoves(Board board, int player){
        try {
            fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
        }
        catch(IOException ex) {

        }
        ArrayList<Board> boardArray = new ArrayList<Board>();
        int[][] actualBoard = board.getBoard();
        int[][] tmpBoard;

        if(player == ROUGE){
            for(int i = 0; i < actualBoard.length;i++){ // todo techniquement tu veux pas commencer a 0 ? vu que la case 0 contient un 1 - Christelle
                for (int j = 0; j < actualBoard[i].length;j++){
                    if (actualBoard[j][i] == ROUGE){
                        for(int k = 0; k < 13; k++){
                            if (k != j){
                                if(isMoveValid(actualBoard, j, i, k, i)){ //colonneinitiale, rangeeinitiale, colonne arrivee, rangeeArrive
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[j][i] = 0;
                                    tmpBoard[k][i] = ROUGE;
                                    tmpBoard = new Board(tmpBoard).mangerJeton(ROUGE, NOIR, k, i);
                                    Board newBoard = new Board((tmpBoard));
                                    if(j == board.getKingPositionX() && i == board.getKingPositionY()){
                                        newBoard.setKingPositionX(k);
                                        newBoard.setKingPositionY(i);
                                    }else {
                                        newBoard.setKingPositionX(board.getKingPositionX());
                                        newBoard.setKingPositionY(board.getKingPositionY());
                                    }
                                    boardArray.add(newBoard);
                                }
                            }
                        }
                        for(int l = 0; l < 13; l++){
                            if (l != i){
                                if(isMoveValid(actualBoard, j, i, j, l)){
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[j][i] = 0;
                                    tmpBoard[j][l] = ROUGE;
                                    tmpBoard = new Board(tmpBoard).mangerJeton(ROUGE, NOIR, j, l);
                                    Board newBoard = new Board((tmpBoard));
                                    if(j == board.getKingPositionX() && i == board.getKingPositionY()){
                                        newBoard.setKingPositionX(j);
                                        newBoard.setKingPositionY(l);
                                    }else {
                                        newBoard.setKingPositionX(board.getKingPositionX());
                                        newBoard.setKingPositionY(board.getKingPositionY());
                                    }
                                    boardArray.add(newBoard);
                                }
                            }
                        }
                    }
                }
            }
        } else if(player == NOIR){
            for(int i = 0; i < actualBoard.length;i++){
                for (int j = 0; j < actualBoard[i].length;j++){
                    if (actualBoard[j][i] == NOIR || actualBoard[j][i] == KING){
                        //tout les move horizontal
                        for(int k = 0; k < 13; k++){
                            if (k != j){
                                if(isMoveValid(actualBoard, j, i, k, i)){
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[k][i] = actualBoard[j][i];
                                    tmpBoard[j][i] = 0;
                                    //si centre de la map et le king a bouger
                                    if(j == 6 && i == 6){
                                        tmpBoard[j][i] = 1;
                                    }
                                    tmpBoard = new Board(tmpBoard).mangerJeton(NOIR, ROUGE, k, i);
                                    Board newBoard = new Board((tmpBoard));
                                    if(j == board.getKingPositionX() && i == board.getKingPositionY()){
                                        newBoard.setKingPositionX(k);
                                        newBoard.setKingPositionY(i);
                                    }else {
                                        newBoard.setKingPositionX(board.getKingPositionX());
                                        newBoard.setKingPositionY(board.getKingPositionY());
                                    }
                                    boardArray.add(newBoard);
                                }
                            }
                        }
                        //tout les move vertical
                        for(int l = 0; l < 13; l++){
                            if (l != i){
                                if(isMoveValid(actualBoard, j, i, j, l)){
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[j][l] = actualBoard[j][i];
                                    tmpBoard[j][i] = 0;
                                    //si centre de la map et le king a bouger
                                    if(j == 6 && i == 6){
                                        tmpBoard[j][i] = 1;
                                    }
                                    tmpBoard = new Board(tmpBoard).mangerJeton(NOIR, ROUGE, j, l);
                                    Board newBoard = new Board((tmpBoard));
                                    if(j == board.getKingPositionX() && i == board.getKingPositionY()){
                                        newBoard.setKingPositionX(j);
                                        newBoard.setKingPositionY(l);
                                    }else {
                                        newBoard.setKingPositionX(board.getKingPositionX());
                                        newBoard.setKingPositionY(board.getKingPositionY());
                                    }
                                    boardArray.add(newBoard);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0 ; i < boardArray.size(); i++) {
            try {
                writeBoardToFile(bw, board, boardArray.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return boardArray;
    }

    /*
    verifie que le mouvement est valide
    en verifiant qu'il n'y est pas de jeton entre le positionnement initial et le positionnement apres mouvement.
     */
    private boolean isMoveValid(int[][] board, int columnInit, int rowInit, int columnMove, int rowMove){
        if(rowInit == rowMove && columnInit == columnMove){ return  false; }
        if(board[columnMove][rowMove] == 1 && board[columnInit][rowInit] != KING){return false;}


        //si le mouvement est a la vertical, verifie qu'il n'y a pas d'autre jeton entre le positionnement initial
        // et le positionnement apres mouvement.
        if (columnInit == columnMove && rowInit != rowMove) {
            if (rowMove > rowInit) {
                for (int i = rowInit + 1; i <= rowMove; i++) {
                    if (board[columnInit][i] > 1) {
                        return false;
                    }
                }
            } else {
                for (int i = rowMove; i < rowInit; i++) {
                    if (board[columnInit][i] > 1) {
                        return false;
                    }
                }
            }
        }
        //si le mouvement est a la horizontal, verifie qu'il n'y a pas d'autre jeton entre le positionnement initial
        // et le positionnement apres mouvement.
        if (rowInit == rowMove && columnInit != columnMove) {
            if (columnMove > columnInit) {
                for (int i = columnInit + 1; i <= columnMove; i++) {
                    if (board[i][rowInit] > 1) {
                        return false;
                    }
                }
            } else {
                for (int i = columnMove; i < columnInit; i++) {
                    if (board[i][rowInit] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * This is for debugging purpose only
     * @param bw
     * @param board
     * @throws IOException
     */
    private static void writeBoardToFile(BufferedWriter bw, Board originalBoard, Board board) throws IOException {
        bw.write("Original board: \n");
        for (int i = 0; i < originalBoard.getBOARD_SIZE(); i++) {
            bw.write(String.format("%3d", i+1)+" |");
            for (int j = 0; j < originalBoard.getBOARD_SIZE(); j++) {
                bw.write(originalBoard.getBoard()[i][j]+"   ");
            }
            bw.write("\n");
        }
        bw.write("Possible boards: \n");
        for (int i = 0; i < board.getBOARD_SIZE(); i++) {
            bw.write(String.format("%3d", i+1)+" |");
            for (int j = 0; j < board.getBOARD_SIZE(); j++) {
                bw.write(board.getBoard()[i][j]+"   ");
            }
            bw.write("\n");
        }
        bw.write("_______________________________________________________ \n");
        bw.write("     A   B   C   D   E   F   G   H   I   J   K   L   M");
        bw.write("\n \n \n");
    }

}
