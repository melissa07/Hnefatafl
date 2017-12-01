package Controleur;

import Modele.Board;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MinMaxAlphaBeta {
    private static final int ROUGE = 4;
    private static final int NOIR = 2;
    private static final int KING = 5;
    private static int score = 0;
    private static final int maxProfondeur = 2;

    private static final String FILENAME = "boards.txt";
    private static BufferedWriter bw = null;
    private static FileWriter fw = null;

 // player may be "computer" or "opponent"
    public static String doMinMax(Board actualBoard, int player){
        Board maxBoard = MaxMove(actualBoard, player, 0, 0, 0);
        System.out.println("***************************");
        maxBoard.printBoard();

        return fetchLastMadeMove(actualBoard, maxBoard);
    }

    private static String fetchLastMadeMove(Board actualBoard, Board maxBoard) {
        String moveDepart = null;
        String moveArrivee = null;

        for (int i = 0; i < actualBoard.getBOARD_SIZE(); i++) {
            for (int j = 0; j < actualBoard.getBOARD_SIZE(); j++) {
                if(actualBoard.getBoard()[i][j] != maxBoard.getBoard()[i][j]) {
                    if(maxBoard.getBoard()[i][j] == 0 || (i == 6 && j ==6)) {
                        moveDepart = String.valueOf((char)(i+65)).toUpperCase()+String.valueOf(13-j);
                    }
                    else {
                        moveArrivee = String.valueOf((char)(i+65)).toUpperCase()+String.valueOf(13-j);
                    }
                }
            }
        }
        //String move = "3 "+ moveDepart+" - "+moveArrivee; // 3 pour dire qu'un move est effectue
        String move = moveDepart+" - "+moveArrivee;
        System.out.println("Le move est: " +move);

        return  move;
    }

    private static Board MaxMove (Board actualBoard, int couleurJoueur, int profondeur, int alpha, int beta){
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

                int boardScore = board.getScore();
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
    
    private static Board MinMove(Board actualBoard, int couleurJoueur, int profondeur, int alpha, int beta) {
        if (IsGameOver(actualBoard) || profondeur == maxProfondeur) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = actualBoard;
            int bestScore = 0;
            ArrayList<Board> boards = new ArrayList<Board>();
            if(couleurJoueur == NOIR){
                boards = generateMoves(actualBoard, ROUGE /*TODO changer selon le player*/);
            }else if(couleurJoueur == ROUGE){
                boards = generateMoves(actualBoard, NOIR /*TODO changer selon le player*/);
            }
            for (Board board : boards) {
                board = MaxMove(executeMove(board), couleurJoueur, profondeur + 1, alpha, beta);

                int boardScore = board.getScore();
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

    private static boolean IsGameOver(Board board) {
        return false;
    }

    private static Board executeMove(Board board) {
        return board;
    }

    private static ArrayList<Board> generateMoves(Board board, int player){
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
                                    boardArray.add(new Board(tmpBoard));
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
                                    boardArray.add(new Board(tmpBoard));
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
                                    boardArray.add(new Board(tmpBoard));
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
                                    boardArray.add(new Board(tmpBoard));
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
    private static boolean isMoveValid(int[][] board, int columnInit, int rowInit, int columnMove, int rowMove){
        if(rowInit == rowMove && columnInit == columnMove){ return  false; }
        if(board[rowMove][columnMove] == 1 && board[rowInit][columnInit] != KING){
            return false;
    }
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
