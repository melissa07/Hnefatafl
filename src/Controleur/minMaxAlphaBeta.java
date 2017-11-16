package Controleur;

import Modele.Board;
import java.util.ArrayList;

public class minMaxAlphaBeta {
    private final int rouge = 1;
    private final int noire = 2;
    private int score = 0;

 // player may be "computer" or "opponent"
    public Board doMinMax(Board actualBoard){

        Board maxBoard = MaxMove(actualBoard);

        return  maxBoard;
    }

    private Board MaxMove (Board actualBoard){
        if (IsGameOver(actualBoard)) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = actualBoard;
            ArrayList<Board> boards = generateMoves(actualBoard, rouge/*TODO changer selon le player*/);
            for (Board board : boards) {
                board = MaxMove(executeMove(actualBoard));
                if (board.getScore() > bestBoard.getScore()) {
                    bestBoard = board;
                }
            }
            return bestBoard;
        }
    }
    private Board MinMove(Board actualBoard) {
        if (IsGameOver(actualBoard)) {
            //je sais pas encore quoi return
            return actualBoard;
        } else {
            Board bestBoard = new Board();
            bestBoard = actualBoard;

            ArrayList<Board> boards = generateMoves(actualBoard, noire /*TODO changer selon le player*/);
            for (Board board : boards) {
                board = MinMove(executeMove(actualBoard));
                if (board.getScore() > bestBoard.getScore()) {
                    bestBoard = board;
                }
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

    private ArrayList<Board> generateMoves(Board board, int player){
        ArrayList<Board> boardArray = new ArrayList<Board>();
        int[][] actualBoard = board.getBoard();

        if(player == rouge){
            for(int i = 0; i < actualBoard.length;i++){
                for (int j = 0; j < actualBoard[i].length;j++){
                    if (actualBoard[i][j] == rouge){
                        for(int k = 0; k < 26; k++){
                            if (k != i){
                                isMoveValid(actualBoard, i, j, k, j);
                            }
                        }
                        for(int l = 0; l < 26; l++){
                            if (l != j){
                                isMoveValid(actualBoard, i, j, i, l);
                            }
                        }
                    }
                }
            }
        }

        return boardArray;
    }

    private boolean isMoveValid(int[][] board, int columnInit, int rowInit, int columnMove, int rowMove){

        return true;
    }

}
