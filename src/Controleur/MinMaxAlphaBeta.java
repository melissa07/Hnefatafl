package Controleur;

import Modele.Board;
import java.util.ArrayList;

public class MinMaxAlphaBeta {
    private static final int rouge = 4;
    private static final int noire = 2;
    private static final int king = 5;
    private static int score = 0;
    private static final int maxProfondeur = 3;

 // player may be "computer" or "opponent"
    public static Board doMinMax(Board actualBoard){

        Board maxBoard = MaxMove(actualBoard, 1, 0 ,0);

        return  maxBoard;
    }

    private static Board MaxMove (Board actualBoard, int profondeur, int alpha, int beta){
        if (IsGameOver(actualBoard) || profondeur == maxProfondeur) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = actualBoard;
            ArrayList<Board> boards = generateMoves(actualBoard, noire/*TODO changer selon le player*/);
            for (Board board : boards) {
                board = MinMove(executeMove(actualBoard),profondeur + 1, alpha, beta);
                if (board.getScore() > bestBoard.getScore()) {
                    bestBoard = board;
                    alpha = board.getScore();
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
            Board bestBoard = new Board();
            bestBoard = actualBoard;

            ArrayList<Board> boards = generateMoves(actualBoard, rouge /*TODO changer selon le player*/);
            for (Board board : boards) {
                board = MaxMove(executeMove(actualBoard), profondeur + 1, alpha, beta);
                if (board.getScore() > bestBoard.getScore()) {
                    bestBoard = board;
                    beta = board.getScore();
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



/*        children = all valid moves for this "player"
        if (player is computer, i.e., max's turn){
        // Find max and store in alpha
        for each child {
            score = minimax(level - 1, opponent, alpha, beta)
            if (score > alpha) alpha = score
            if (alpha >= beta) break;  // beta cut-off
        }
        return alpha
    } else (player is opponent, i.e., min's turn)
            // Find min and store in beta
            for each child {
        score = minimax(level - 1, computer, alpha, beta)
        if (score < beta) beta = score
        if (alpha >= beta) break;  // alpha cut-off
    }
       return beta
}*/

    private static ArrayList<Board> generateMoves(Board board, int player){
        ArrayList<Board> boardArray = new ArrayList<Board>();
        int[][] actualBoard = board.getBoard();
        int[][] tmpBoard = board.getBoard();

        if(player == rouge){
            for(int i = 0; i < actualBoard.length;i++){
                for (int j = 0; j < actualBoard[i].length;j++){
                    tmpBoard = actualBoard;
                    if (actualBoard[i][j] == rouge){
                        for(int k = 0; k < 13; k++){
                            if (k != i){
                                if(isMoveValid(actualBoard, i, j, k, j)){
                                    tmpBoard = actualBoard;
                                    tmpBoard[i][j] = 0;
                                    tmpBoard[k][j] = rouge;
                                    boardArray.add(new Board(tmpBoard));
                                }
                            }
                        }
                        for(int l = 0; l < 13; l++){
                            if (l != j){
                                if(isMoveValid(actualBoard, i, j, i, l)){
                                    tmpBoard = actualBoard;
                                    tmpBoard[i][j] = 0;
                                    tmpBoard[i][l] = rouge;
                                    boardArray.add(new Board(tmpBoard));
                                }
                            }
                        }
                    }
                }
            }
        }else if(player == noire){
            for(int i = 0; i < actualBoard.length;i++){
                for (int j = 0; j < actualBoard[i].length;j++){
                    if (actualBoard[i][j] == noire){
                        for(int k = 0; k < 13; k++){
                            if (k != i){
                                if(isMoveValid(actualBoard, i, j, k, j)){
                                    tmpBoard = actualBoard;
                                    tmpBoard[i][j] = 0;
                                    tmpBoard[k][j] = noire;
                                    boardArray.add(new Board(tmpBoard));
                                }
                            }
                        }
                        for(int l = 0; l < 13; l++){
                            if (l != j){
                                if(isMoveValid(actualBoard, i, j, i, l)){
                                    tmpBoard = actualBoard;
                                    tmpBoard[i][j] = 0;
                                    tmpBoard[i][l] = noire;
                                    boardArray.add(new Board(tmpBoard));
                                }
                            }
                        }
                    }
                }
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

}
