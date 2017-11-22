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
    public static Board doMinMax(Board actualBoard){

        Board maxBoard = MaxMove(actualBoard, 0, 0 ,0);
        System.out.println("***************************");
        maxBoard.printBoard();

        fetchLastMadeMove(actualBoard, maxBoard);

        return  maxBoard;
    }

    private static void fetchLastMadeMove(Board actualBoard, Board maxBoard) {
        String moveDepart = null;
        String moveArrivee = null;

        for (int i = 0; i < actualBoard.getBOARD_SIZE(); i++) {
            for (int j = 0; j < actualBoard.getBOARD_SIZE(); j++) {
                if(actualBoard.getBoard()[i][j] != maxBoard.getBoard()[i][j]) {
                    if(maxBoard.getBoard()[i][j] == 0) {
                        moveDepart = String.valueOf((char)(i+65)).toUpperCase()+String.valueOf(j);
                    }
                    else {
                        moveArrivee = String.valueOf((char)(i+65)).toUpperCase()+String.valueOf(j);
                    }
                }
            }
        }
        String move = "3 "+ moveDepart+" - "+moveArrivee; // 3 pour dire qu'un move est effectue
        System.out.println("Le move est: " +move);
    }

    private static Board MaxMove (Board actualBoard, int profondeur, int alpha, int beta){
        if (IsGameOver(actualBoard) || profondeur == maxProfondeur) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = actualBoard;
            int bestScore = 0;

            ArrayList<Board> boards = generateMoves(actualBoard, NOIR/*TODO changer selon le player*/);
            for (Board board : boards) {
                Board boardProfond = MinMove(executeMove(board),profondeur + 1, alpha, beta);

                int boardScore = boardProfond.getScore();
                //if (board.getScore() > bestBoard.getScore()) {
                if (boardScore > bestScore) {
                    //bestScore = board.getScore();
                    bestScore = boardScore;
                    bestBoard = board;
                    //alpha = board.getScore();
                    alpha = boardScore;
                }
                // Ignore remaining moves
                if (beta > alpha)
                    return bestBoard;
            }
            return bestBoard;
        }
    }
    
    private static Board MinMove(Board actualBoard, int profondeur, int alpha, int beta) {
        if (IsGameOver(actualBoard) || profondeur == maxProfondeur) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = actualBoard;
            int bestScore = 0;

            ArrayList<Board> boards = generateMoves(actualBoard, ROUGE /*TODO changer selon le player*/);
            for (Board board : boards) {
                board = MaxMove(executeMove(board), profondeur + 1, alpha, beta);

                int boardScore = board.getScore();
                //if (board.getScore() > bestBoard.getScore()) {
                if (boardScore > bestScore) {
                    //bestScore = board.getScore();
                    bestScore = boardScore;
                    bestBoard = board;
                    //beta = board.getScore();
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
        int[][] tmpBoard = board.getBoard();

        if(player == ROUGE){
            for(int i = 0; i < actualBoard.length;i++){
                for (int j = 0; j < actualBoard[i].length;j++){
                    if (actualBoard[j][i] == ROUGE){
                        for(int k = 0; k < 13; k++){
                            if (k != j){
                                if(isMoveValid(actualBoard, j, i, k, i)){
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[j][i] = 0;
                                    tmpBoard[k][i] = ROUGE;
                                    boardArray.add(new Board(tmpBoard)); // todo this reset the board?
                                }
                            }
                        }
                        for(int l = 0; l < 13; l++){
                            if (l != i){
                                if(isMoveValid(actualBoard, j, i, j, l)){
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[j][i] = 0;
                                    tmpBoard[j][l] = ROUGE;
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
                    if (actualBoard[j][i] == NOIR){
                        for(int k = 0; k < 13; k++){
                            if (k != j){
                                if(isMoveValid(actualBoard, j, i, k, i)){
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[j][i] = 0;
                                    tmpBoard[j][k] = NOIR;
                                    boardArray.add(new Board(tmpBoard));
                                }
                            }
                        }
                        for(int l = 0; l < 13; l++){
                            if (l != i){
                                if(isMoveValid(actualBoard, j, i, j, l)){
                                    tmpBoard = board.copyBoard(board);
                                    tmpBoard[j][i] = 0;
                                    tmpBoard[j][l] = NOIR;
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
                writeBoardToFile(bw, boardArray.get(i));
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
        //si le mouvement est a la vertical, verifie qu'il n'y a pas d'autre jeton entre le positionnement initial
        // et le positionnement apres mouvement.
        if(columnInit == columnMove && rowInit != rowMove){
            if(rowMove > rowInit){
                for(int i = rowInit + 1; i < rowMove; i++){
                    if(board[columnInit][i] != 0 || board[columnInit][i] != 5){
                        return false;
                    }
                }
            }else{
                for(int i = rowMove + 1; i < rowInit; i++){
                    if(board[columnInit][i] != 0){
                        return false;
                    }
                }
            }
        }
        //si le mouvement est a la horizontal, verifie qu'il n'y a pas d'autre jeton entre le positionnement initial
        // et le positionnement apres mouvement.
        if(rowInit == rowMove && columnInit != columnMove){
            if(columnMove > columnInit){
                for(int i = columnInit + 1; i < columnMove; i++){
                    if(board[i][rowInit] != 0 || board[i][rowInit] != 5){
                        return false;
                    }
                }
            }else{
                for(int i = columnMove + 1; i < columnInit; i++){
                    if(board[i][rowInit] != 0){
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
    private static void writeBoardToFile(BufferedWriter bw, Board board) throws IOException {
        for (int i = board.getBOARD_SIZE()-1 ; i >= 0; i--) {
            bw.write(String.format("%3d", 13-i)+" |");
            for (int j = board.getBOARD_SIZE()-1 ; j >= 0; j--) {
                bw.write(board.getBoard()[i][j]+"   ");
            }
            bw.write("\n");
        }
        bw.write("_______________________________________________________ \n");
        bw.write("     A   B   C   D   E   F   G   H   I   J   K   L   M");
        bw.write("\n \n \n");
    }

}
